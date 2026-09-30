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

import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ScheduledJobMetricsTest {
    private SimpleMeterRegistry registry;
    private ScheduledJobMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        final var tenantService = Mockito.mock(TenantService.class);
        when(tenantService.getCurrentStringIdentifier()).thenReturn("bern");
        metrics = new ScheduledJobMetrics(registry, tenantService);
    }

    @Test
    void recordScheduledJobRecordsDurationAndRunsAction() {
        final var invoked = new boolean[1];

        metrics.recordScheduledJob(ScheduledJobMetricsTest.class, () -> invoked[0] = true);

        assertThat(invoked[0]).isTrue();
        assertThat(
            registry.get(ScheduledJobMetrics.SCHEDULED_JOBS)
                .tags("tenant", "bern", "job", "scheduledjobmetricstest", "outcome", "success")
                .timer()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordScheduledJobRecordsErrorOutcomeAndRethrows() {
        assertThatThrownBy(
            () -> metrics.recordScheduledJob(ScheduledJobMetricsTest.class, () -> {
                throw new IllegalStateException("boom");
            })
        ).isInstanceOf(IllegalStateException.class);

        assertThat(
            registry.get(ScheduledJobMetrics.SCHEDULED_JOBS)
                .tags("tenant", "bern", "job", "scheduledjobmetricstest", "outcome", "error")
                .timer()
                .count()
        ).isEqualTo(1);
    }

    @Test
    void recordScheduledJobFallsBackToUnknownTenant() {
        final var tenantService = Mockito.mock(TenantService.class);
        when(tenantService.getCurrentStringIdentifier()).thenReturn(null);
        final var unknownTenantMetrics = new ScheduledJobMetrics(registry, tenantService);

        unknownTenantMetrics.recordScheduledJob(ScheduledJobMetricsTest.class, () -> {
        });

        assertThat(
            registry.get(ScheduledJobMetrics.SCHEDULED_JOBS)
                .tags("tenant", "unknown", "job", "scheduledjobmetricstest", "outcome", "success")
                .timer()
                .count()
        ).isEqualTo(1);
    }
}
