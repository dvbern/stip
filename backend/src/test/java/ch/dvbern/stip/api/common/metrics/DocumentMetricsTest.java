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
import java.net.SocketTimeoutException;

import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class DocumentMetricsTest {
    private SimpleMeterRegistry registry;
    private DocumentMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        final var tenantService = Mockito.mock(TenantService.class);
        when(tenantService.getCurrentStringIdentifier()).thenReturn("bern");
        metrics = new DocumentMetrics(registry, tenantService);
    }

    @Test
    void recordDocumentGenerationRecordsDurationAndOutputSize() {
        final var output = new ByteArrayOutputStream();
        output.writeBytes(new byte[] { 1, 2, 3 });

        assertThat(
            metrics.recordDocumentGeneration(
                DocumentMetrics.DocumentType.DATENSCHUTZBRIEF,
                DocumentMetrics.DocumentEngine.ITEXT,
                () -> output
            )
        ).isSameAs(output);

        assertThat(
            registry.get(DocumentMetrics.DOCUMENT_GENERATION)
                .tags(
                    "tenant",
                    "bern",
                    "document_type",
                    "datenschutzbrief",
                    "engine",
                    "itext",
                    "outcome",
                    "success"
                )
                .timer()
                .count()
        ).isEqualTo(1);
        assertThat(
            registry.get(DocumentMetrics.DOCUMENT_OUTPUT_SIZE)
                .tags("tenant", "bern", "document_type", "datenschutzbrief")
                .summary()
                .totalAmount()
        ).isEqualTo(3);
    }

    @Test
    void recordDocumentGenerationRecordsErrorOutcome() {
        assertThatThrownBy(
            () -> metrics.recordDocumentGeneration(
                DocumentMetrics.DocumentType.VERFUEGUNG,
                DocumentMetrics.DocumentEngine.TYPST,
                () -> {
                    throw new IllegalStateException("boom");
                }
            )
        ).isInstanceOf(IllegalStateException.class);

        assertThat(
            registry.get(DocumentMetrics.DOCUMENT_GENERATION)
                .tags(
                    "tenant",
                    "bern",
                    "document_type",
                    "verfuegung",
                    "engine",
                    "typst",
                    "outcome",
                    "error"
                )
                .timer()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordDocumentStorageRecordsDurationAndOutputSize() {
        assertThat(
            metrics.recordDocumentStorage(DocumentMetrics.DocumentType.STATISTIK, 42, () -> "stored")
        ).isEqualTo("stored");

        assertThat(
            registry.get(DocumentMetrics.DOCUMENT_STORAGE)
                .tags(
                    "tenant",
                    "bern",
                    "category",
                    "statistik",
                    "operation",
                    "put",
                    "outcome",
                    "success"
                )
                .timer()
                .count()
        ).isEqualTo(1);
        assertThat(
            registry.get(DocumentMetrics.DOCUMENT_OUTPUT_SIZE)
                .tags("tenant", "bern", "document_type", "statistik")
                .summary()
                .totalAmount()
        ).isEqualTo(42);
    }

    @Test
    void recordTypstRenderRecordsDuration() {
        metrics.recordTypstRender(DocumentMetrics.DocumentEngine.TYPST, () -> new ByteArrayOutputStream());

        assertThat(
            registry.get(DocumentMetrics.TYPST_RENDER)
                .tags("tenant", "bern", "template", "typst", "outcome", "success")
                .timer()
                .count()
        ).isEqualTo(1);
        assertThat(registry.get(DocumentMetrics.TYPST_ACTIVE).longTaskTimer().activeTasks()).isZero();
    }

    @Test
    void recordMassPrintJobRecordsDurationAndOutputSize() {
        metrics.recordMassPrintJob(MassendruckJobTyp.VERFUEGUNG, () -> {
        });
        metrics.recordMassPrintOutputSize(MassendruckJobTyp.VERFUEGUNG, 7);

        assertThat(
            registry.get(DocumentMetrics.MASS_PRINT_JOBS)
                .tags("tenant", "bern", "type", "verfuegung", "outcome", "success")
                .timer()
                .count()
        ).isEqualTo(1);
        assertThat(
            registry.get(DocumentMetrics.MASS_PRINT_OUTPUT_SIZE)
                .tags("tenant", "bern", "type", "verfuegung")
                .summary()
                .totalAmount()
        ).isEqualTo(7);
    }

    @Test
    void recordTypstRenderClassifiesTimeoutFromCause() {
        assertThatThrownBy(
            () -> metrics.recordTypstRender(DocumentMetrics.DocumentEngine.TYPST, () -> {
                throw new IllegalStateException("render failed", new SocketTimeoutException("slow"));
            })
        ).isInstanceOf(IllegalStateException.class);

        assertThat(
            registry.get(DocumentMetrics.TYPST_RENDER)
                .tags("tenant", "bern", "template", "typst", "outcome", "timeout")
                .timer()
                .count()
        ).isEqualTo(1);
    }
}
