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

package ch.dvbern.stip.integration.gemeindelookup.domain.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class GemeindeLookupService {
    private final TenantService tenantService;
    private final Scheduler scheduler;

    private static final String GEMEINDE_LOOKUP_FETCH_SCHEDULED_JOB_PREFIX = "GemeindeLookupFetchScheduledJob-";

    @Transactional
    public void createFetchGemeindeDataScheduledJob(final Gesuch gesuch) {
        final var jobData = new GemeindeLookupRequest(
            gesuch,
            tenantService.getCurrentTenantIdentifier()
        );

        final JobDetail jobDetail = JobBuilder.newJob(GemeindeLookupScheduledJob.class)
            .withIdentity(GEMEINDE_LOOKUP_FETCH_SCHEDULED_JOB_PREFIX + gesuch.getId().toString())
            .usingJobData(jobData.toMap())
            .build();
        final Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(GEMEINDE_LOOKUP_FETCH_SCHEDULED_JOB_PREFIX + "trigger-" + gesuch.getId().toString())
            .startNow()
            .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("Could not schedule {}", GEMEINDE_LOOKUP_FETCH_SCHEDULED_JOB_PREFIX, e);
        }
    }
}
