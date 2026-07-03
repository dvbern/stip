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

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.tenancy.service.TenantContext;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.pdf.domain.model.PdfAdapterType;
import ch.dvbern.stip.integration.pdf.domain.model.PdfTemplateType;
import ch.dvbern.stip.integration.pdf.domain.port.PdfPortFactory;
import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypstStartupService {

    private final PdfPortFactory pdfPortFactory;
    private final TenantService tenantService;

    public TypstStartupService(PdfPortFactory pdfPortFactory, TenantService tenantService) {
        this.pdfPortFactory = pdfPortFactory;
        this.tenantService = tenantService;
    }

    // void startup(@Observes StartupEvent ev) {
    //     Arc.container().requestContext().activate();
    //     Arc.container().instance(TenantContext.class).get().setTenantIdentifier(TenantIdentifier.BERN);
    //     QuarkusTransaction.requiringNew().run(() -> {
    //         final var pdfAdapter = pdfPortFactory.getPdfAdapter();

    //         final var tenantAdapterConfig = tenantService.getConfigForCurrentTenant().adapter().pdf().get(PdfAdapterType.TYPST);

    //         final JsonObject json = Json.createObjectBuilder()
    //             .add("template", tenantAdapterConfig.templatePath().get(PdfTemplateType.BERECHNUNGSBLATT_PIA))
    //             .add("lang", "fr")
    //             .add("amount", 23)
    //             .build();

    //         final var start = Instant.now();

    //         final var outputStream = pdfAdapter.renderPdf(json.toString());

    //         try {
    //             Duration elapsed = Duration.between(start, Instant.now());
    //             LOG.info("Generation took: {}ms", elapsed.toMillis());

    //             final Path target = Path.of("output.pdf");
    //             Files.write(target, outputStream.toByteArray());
    //         } catch (Exception e) {
    //             throw new RuntimeException("Failed to write PDF to disk", e);
    //         }
    //     });
    //     Arc.container().requestContext().deactivate();
    // }
}
