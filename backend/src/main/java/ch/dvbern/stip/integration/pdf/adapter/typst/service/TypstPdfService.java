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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfCompilationException;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfGenerationException;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfQueueFullException;
import ch.dvbern.stip.integration.pdf.adapter.typst.exceptions.PdfTimeoutException;
import ch.dvbern.stip.integration.pdf.domain.model.PdfAdapterType;
import ch.dvbern.stip.integration.pdf.domain.port.PdfPort;
import ch.dvbern.stip.integration.pdf.domain.qualifier.PdfQualifier;
import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@PdfQualifier(PdfAdapterType.TYPST)
public class TypstPdfService implements PdfPort {

    private static final String RENDER_EXECUTOR_NAME = "typst-render-";
    private static final String IO_EXECUTOR_NAME = "typst-io-";

    private final StipConfig config;
    private final TenantService tenantService;
    private final ExecutorService renderExecutor;
    private final ExecutorService ioExecutor;

    public TypstPdfService(StipConfig config, TenantService tenantService) {
        this.config = config;
        this.tenantService = tenantService;

        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);

        this.renderExecutor = new ThreadPoolExecutor(
            adapterConfig.minThreads(),
            adapterConfig.maxThreads(),
            adapterConfig.threadKeepAlive(),
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(adapterConfig.queueSize()),
            Thread.ofPlatform()
                .name(RENDER_EXECUTOR_NAME, 0)
                .factory(),
            new ThreadPoolExecutor.AbortPolicy()
        );

        this.ioExecutor = Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual()
                .name(IO_EXECUTOR_NAME, 0)
                .factory()
        );
    }

    @Override
    public CompletionStage<ByteArrayOutputStream> renderPdf(final String templatePath, final String jsonData) {
        try {
            return CompletableFuture.supplyAsync(
                () -> renderBlocking(templatePath, jsonData),
                renderExecutor
            );
        } catch (RejectedExecutionException e) {
            return CompletableFuture.failedFuture(
                new PdfQueueFullException("PDF generation queue is full")
            );
        }
    }

    private ByteArrayOutputStream renderBlocking(final String templatePath, final String jsonData) {
        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);

        Process process = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(buildCommand(templatePath, jsonData));
            pb.redirectErrorStream(false);

            process = pb.start();

            Process finalProcess = process;

            CompletableFuture<ByteArrayOutputStream> stdout =
                CompletableFuture.supplyAsync(
                    () -> readBounded(finalProcess.getInputStream()),
                    ioExecutor
                );

            CompletableFuture<ByteArrayOutputStream> stderr =
                CompletableFuture.supplyAsync(
                    () -> readBounded(finalProcess.getErrorStream()),
                    ioExecutor
                );

            stdout.whenComplete((ignored, error) -> {
                if (error != null) {
                    kill(finalProcess);
                }
            });

            stderr.whenComplete((ignored, error) -> {
                if (error != null) {
                    kill(finalProcess);
                }
            });

            boolean finished = process.waitFor(
                Duration.ofSeconds(adapterConfig.timeout()).toMillis(),
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

    private List<String> buildCommand(final String templatePath, final String jsonData) {
        final var adapterConfig = config.globalAdapter().pdf().get(PdfAdapterType.TYPST);
        final var typstResourcePath = TypstPdfService.class.getResource("/typst").getPath();
        final var templateResourcePath = Path.of(typstResourcePath, templatePath).toString();

        List<String> command = new ArrayList<>();

        command.add(adapterConfig.binary());
        command.add("compile");

        command.add("--format");
        command.add("pdf");

        command.add("--jobs");
        command.add("1");

        command.add("--root");
        command.add(typstResourcePath);

        command.add("--font-path");
        command.add(TypstPdfService.class.getResource("/fonts").getPath());

        command.add("--input");
        command.add("data=" + jsonData);

        command.add("--ignore-system-fonts");

        command.add(templateResourcePath);
        command.add("-");

        return command;
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
        shutdownGracefully(renderExecutor, RENDER_EXECUTOR_NAME);
        shutdownGracefully(ioExecutor, IO_EXECUTOR_NAME);
    }

    private void shutdownGracefully(ExecutorService executor, String name) {
        if (executor.isShutdown()) {
            return;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                final var dropped = executor.shutdownNow();
                LOG.warn("Executor '{}' did not terminate in time, {} pending tasks dropped", name, dropped.size());
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOG.error("Executor '{}' failed to terminate after shutdownNow()", name);
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
