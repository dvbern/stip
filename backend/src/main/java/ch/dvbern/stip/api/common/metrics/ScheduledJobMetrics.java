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
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.inject.Singleton;

@Singleton
public class ScheduledJobMetrics extends AbstractTimedMetrics {
    static final String SCHEDULED_JOBS = "kstip.scheduled.job";

    public ScheduledJobMetrics(final MeterRegistry meterRegistry, final TenantService tenantService) {
        super(meterRegistry, tenantService);
    }

    public void recordScheduledJob(final Class<?> job, final Runnable action) {
        recordTimed(SCHEDULED_JOBS, Tags.of("job", normalize(job.getSimpleName())), action);
    }
}
