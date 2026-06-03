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

package ch.dvbern.stip.api.common.scheduledtask;

import java.util.TimeZone;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.config.type.StipConfig;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

@Slf4j
public abstract class RunForTenantsScheduledTask implements Job {
    private static final String SCHEDULED_TASK_NAME_SUFFIX = "ScheduledTask";
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Zurich");

    private final String name;
    private final String schedulerConfigKey;
    private final TenantIdentifier[] tenantIdentifiers;

    @Inject
    Scheduler scheduler;

    @Inject
    StipConfig config;

    protected RunForTenantsScheduledTask(
    final String name,
    final String schedulerConfigKey,
    final TenantIdentifier[] tenantIdentifiers
    ) {
        this.name = name;
        this.schedulerConfigKey = schedulerConfigKey;
        this.tenantIdentifiers = tenantIdentifiers;
    }

    protected RunForTenantsScheduledTask(
    final String name,
    final String schedulerConfigKey,
    final TenantIdentifier tenantIdentifier
    ) {
        this(name, schedulerConfigKey, new TenantIdentifier[] { tenantIdentifier });
    }

    @Transactional
    void onStart(@Observes StartupEvent startupEvent) {
        final var jobDetail = JobBuilder.newJob(this.getClass())
            .withIdentity(name)
            .build();

        final var schedule =
            CronScheduleBuilder.cronSchedule(config.scheduler().get(schedulerConfigKey).cron()).inTimeZone(TIME_ZONE);

        final var trigger = TriggerBuilder.newTrigger()
            .withIdentity(String.format("%s%s-trigger", SCHEDULED_TASK_NAME_SUFFIX, name))
            .startNow()
            .withSchedule(schedule)
            .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error(String.format("Error scheduling %s%s", SCHEDULED_TASK_NAME_SUFFIX, name), e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) {
        QuarkusTransactionUtil.runForTenantsInNewTransaction(tenantIdentifiers, this::run);
    }

    protected abstract void run();

}
