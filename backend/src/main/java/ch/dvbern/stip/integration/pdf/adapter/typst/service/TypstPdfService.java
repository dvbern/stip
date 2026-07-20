/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.integration.pdf.adapter.typst.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfCompilationException;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfGenerationException;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfTimeoutException;
import ch.dvbern.stip.integration.pdf.domain.model.PdfAdapterType;
import ch.dvbern.stip.integration.pdf.domain.model.PdfPayload;
import ch.dvbern.stip.integration.pdf.domain.port.PdfPort;
import ch.dvbern.stip.integration.pdf.domain.qualifier.PdfQualifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@PdfQualifier(PdfAdapterType.TYPST)
public class TypstPdfService implements PdfPort {

    private static final String IO_EXECUTOR_NAME = "typst-io-";
    private static final String FONTS_PATH = "/fonts";
    private static final String TYPST_PATH = "/typst";
    private static final String MAIN_TEMPLATE_NAME = "main.typ";

    private final StipConfig config;
    private final TenantService tenantService;
    private final ExecutorService ioExecutor;
    private final ObjectMapper objectMapper;

    public TypstPdfService(
    final StipConfig config, final TenantService tenantService, final ObjectMapper objectMapper
    ) {
        this.config = config;
        this.tenantService = tenantService;
        this.objectMapper = objectMapper;

        this.ioExecutor = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual()
                .name(IO_EXECUTOR_NAME, 0)
                .factory()
        );
    }

    @Override
    public ByteArrayOutputStream renderPdf(final PdfPayload<?> pdfPayload) {
        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);

        Process process = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(buildCommand(pdfPayload));
            pb.redirectErrorStream(false);

            process = pb.start();

            Process finalProcess = process;

            CompletableFuture<ByteArrayOutputStream> stdout =
                CompletableFuture.supplyAsync(
                    () -> readBounded(finalProcess.getInputStream()),
                    ioExecutor
                ).whenComplete((ignored, error) -> {
                    if (error != null) {
                        kill(finalProcess);
                    }
                });

            CompletableFuture<ByteArrayOutputStream> stderr =
                CompletableFuture.supplyAsync(
                    () -> readBounded(finalProcess.getErrorStream()),
                    ioExecutor
                ).whenComplete((ignored, error) -> {
                    if (error != null) {
                        kill(finalProcess);
                    }
                });

            boolean finished = process.waitFor(
                Duration.ofSeconds(adapterConfig.compileTimeout()).toMillis(),
                TimeUnit.MILLISECONDS
            );

            if (!finished) {
                kill(process);
                throw new PdfTimeoutException("Typst compilation timed out");
            }

            ByteArrayOutputStream pdfOutputStream = stdout.get();
            ByteArrayOutputStream stderrOutputStream = stderr.get();

            int exitCode = process.exitValue();

            if (exitCode != 0) {
                String diagnostics = stderrOutputStream.toString(StandardCharsets.UTF_8);
                throw new PdfCompilationException(exitCode, diagnostics);
            }

            if (!isPdf(pdfOutputStream.toByteArray())) {
                throw new PdfGenerationException("Typst did not return a valid PDF");
            }

            return pdfOutputStream;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            kill(process);
            throw new PdfGenerationException("PDF generation was interrupted", e);

        } catch (ExecutionException e) {
            kill(process);
            throw new PdfGenerationException("Failed to read Typst output", e.getCause());

        } catch (IOException e) {
            throw new PdfGenerationException("Failed to run Typst", e);
        }
    }

    private List<String> buildCommand(final PdfPayload<?> pdfPayload) {
        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);
        final var tenantConfig = tenantService.getConfigForCurrentTenant().adapter().pdf().get(PdfAdapterType.TYPST);

        final var fontsPath = adapterConfig.fontsPath();
        final var typstPath = adapterConfig.rootPath();

        final var mainTemplatePath =
            Path.of(
                typstPath,
                tenantConfig.rootTemplatePath()
                    .orElseThrow(
                        () -> new PdfGenerationException(
                            "Root template path is not set for tenant " + tenantService.getCurrentTenantIdentifier()
                        )
                    ),
                MAIN_TEMPLATE_NAME
            ).toString();

        try {
            final var jsonPayload = pdfPayload.toJson(objectMapper, tenantConfig);

            List<String> command = new ArrayList<>();

            command.add(adapterConfig.binary());
            command.add("compile");

            command.add("--format");
            command.add("pdf");

            command.add("--jobs");
            command.add("1");

            command.add("--root");
            command.add(typstPath);

            command.add("--font-path");
            command.add(fontsPath);

            command.add("--input");
            command.add("data=" + jsonPayload);

            command.add("--ignore-system-fonts");

            command.add(mainTemplatePath);
            command.add("-");

            return command;
        } catch (JsonProcessingException e) {
            throw new PdfGenerationException("Failed to parse pdf payload to json", e);
        }
    }

    private boolean isPdf(byte[] bytes) {
        return bytes.length >= 5
        && bytes[0] == '%'
        && bytes[1] == 'P'
        && bytes[2] == 'D'
        && bytes[3] == 'F'
        && bytes[4] == '-';
    }

    private ByteArrayOutputStream readBounded(InputStream input) {
        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            long total = 0;

            if (total > adapterConfig.maxOutputBytes()) {
                throw new PdfGenerationException("Typst output too large");
            }

            int read;
            while ((read = input.read(buffer)) != -1) {
                total += read;

                output.write(buffer, 0, read);
            }

            return output;

        } catch (IOException e) {
            throw new PdfGenerationException("Failed to read Typst output", e);
        }
    }

    private void kill(Process process) {
        process.toHandle()
            .descendants()
            .forEach(ProcessHandle::destroyForcibly);

        process.destroyForcibly();
    }

    void onStop(@Observes ShutdownEvent event) {
        close();
    }

    @Override
    public void close() {
        shutdownGracefully(ioExecutor, IO_EXECUTOR_NAME);
    }

    private void shutdownGracefully(ExecutorService executor, String name) {
        if (executor.isShutdown()) {
            return;
        }

        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);

        executor.shutdown();
        try {
            if (!executor.awaitTermination(adapterConfig.shutdownWaitTimeout(), TimeUnit.SECONDS)) {
                final var dropped = executor.shutdownNow();
                LOG.warn("Executor '{}' did not terminate in time, {} pending tasks dropped", name, dropped.size());
                if (!executor.awaitTermination(adapterConfig.shutdownForceTimeout(), TimeUnit.SECONDS)) {
                    LOG.error("Executor '{}' failed to terminate after shutdownNow()", name);
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
