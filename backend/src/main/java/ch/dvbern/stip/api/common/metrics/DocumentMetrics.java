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

package ch.dvbern.stip.api.common.metrics;

import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.inject.Singleton;

@Singleton
public class DocumentMetrics extends AbstractTimedMetrics {
    static final String DOCUMENT_GENERATION = "kstip.document.generation";
    static final String DOCUMENT_OUTPUT_SIZE = "kstip.document.output.size";
    static final String DOCUMENT_STORAGE = "kstip.document.storage";
    static final String TYPST_RENDER = "kstip.typst.render";
    static final String TYPST_ACTIVE = "kstip.typst.active";
    static final String MASS_PRINT_JOBS = "kstip.mass.print.jobs";
    static final String MASS_PRINT_OUTPUT_SIZE = "kstip.mass.print.output.size";

    public DocumentMetrics(final MeterRegistry meterRegistry, final TenantService tenantService) {
        super(meterRegistry, tenantService);
    }

    public void recordDocumentGeneration(
        final DocumentType documentType,
        final DocumentEngine engine,
        final Runnable action
    ) {
        recordTimed(DOCUMENT_GENERATION, generationTags(documentType, engine), action);
    }

    public ByteArrayOutputStream recordDocumentGeneration(
        final DocumentType documentType,
        final DocumentEngine engine,
        final Supplier<ByteArrayOutputStream> action
    ) {
        final var tags = generationTags(documentType, engine);
        final var result = recordTimed(DOCUMENT_GENERATION, tags, action);
        recordSize(DOCUMENT_OUTPUT_SIZE, Tags.of("document_type", tag(documentType)), result.size());
        return result;
    }

    public ByteArrayOutputStream recordTypstRender(
        final Enum<?> template,
        final Supplier<ByteArrayOutputStream> action
    ) {
        final var active = LongTaskTimer.builder(TYPST_ACTIVE)
            .description("Currently active Typst render operations")
            .tag("tenant", currentTenant())
            .register(registry())
            .start();
        try {
            return recordTimed(TYPST_RENDER, Tags.of("template", tag(template)), action);
        } finally {
            active.stop();
        }
    }

    public <T> T recordDocumentStorage(
        final DocumentType category,
        final int bytes,
        final Supplier<T> action
    ) {
        final var tags =
            Tags.of("category", tag(category), "operation", tag(StorageOperation.PUT));
        final var result = recordTimed(DOCUMENT_STORAGE, tags, action);
        recordSize(DOCUMENT_OUTPUT_SIZE, Tags.of("document_type", tag(category)), bytes);
        return result;
    }

    public void recordMassPrintJob(final Enum<?> type, final Runnable action) {
        recordTimed(MASS_PRINT_JOBS, Tags.of("type", tag(type)), action);
    }

    public void recordMassPrintOutputSize(final Enum<?> type, final int bytes) {
        recordSize(MASS_PRINT_OUTPUT_SIZE, Tags.of("type", tag(type)), bytes);
    }

    private static Tags generationTags(final DocumentType documentType, final DocumentEngine engine) {
        return Tags.of("document_type", tag(documentType), "engine", tag(engine));
    }

    public enum DocumentType {
        VERFUEGUNG,
        DATENSCHUTZBRIEF,
        DARLEHEN_VERFUEGUNG,
        STATISTIK
    }

    public enum DocumentEngine {
        ITEXT,
        TYPST,
        COMPOSITE
    }

    private enum StorageOperation {
        PUT
    }
}
