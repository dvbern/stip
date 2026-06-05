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

import java.time.LocalDate;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.generator.entities.gesuch.GesuchTestBuilder;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusComponentTest
public class GemeindeLookupServiceTest {

    private static final String JOB_PREFIX = "GemeindeLookupFetchScheduledJob-";

    @Inject
    GemeindeLookupService gemeindeLookupService;

    @InjectMock
    TenantService tenantService;

    @InjectMock
    Scheduler scheduler;

    @BeforeEach
    void setup() {
        when(tenantService.getCurrentTenantIdentifier()).thenReturn(TenantIdentifier.BERN);
    }

    @Test
    void createFetchGemeindeDataScheduledJob_schedulesJobWithExpectedData() throws SchedulerException {
        final var gesuch = GesuchTestBuilder.standardWithNestedDeps(LocalDate.now()).build();

        gemeindeLookupService.createFetchGemeindeDataScheduledJob(gesuch);

        final var jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        final var triggerCaptor = ArgumentCaptor.forClass(Trigger.class);

        verify(scheduler).scheduleJob(jobDetailCaptor.capture(), triggerCaptor.capture());

        final var jobDetail = jobDetailCaptor.getValue();
        final var trigger = triggerCaptor.getValue();

        assertThat(jobDetail, notNullValue());
        assertThat(trigger, notNullValue());

        assertThat(jobDetail.getKey().getName(), equalTo(JOB_PREFIX + gesuch.getId()));

        final var jobDataMap = jobDetail.getJobDataMap();
        final var adresse = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung().getAdresse();

        assertThat(jobDataMap.getString("gesuchId"), equalTo(gesuch.getId().toString()));
        assertThat(jobDataMap.getString("tenantIdentifier"), equalTo(TenantIdentifier.BERN.getIdentifier()));
        assertThat(jobDataMap.getString("strasse"), equalTo(adresse.getStrasse()));
        assertThat(jobDataMap.getString("hausnummer"), equalTo(adresse.getHausnummer()));
        assertThat(jobDataMap.getString("plz"), equalTo(adresse.getPlz()));
        assertThat(jobDataMap.getString("ort"), equalTo(adresse.getOrt()));
    }

    @Test
    void createFetchGemeindeDataScheduledJob_doesNotThrowWhenSchedulerFails() throws SchedulerException {
        final var gesuch = GesuchTestBuilder.standardWithNestedDeps(LocalDate.now()).build();

        doThrow(new SchedulerException("Could not schedule job"))
            .when(scheduler)
            .scheduleJob(any(JobDetail.class), any(Trigger.class));

        assertDoesNotThrow(() -> gemeindeLookupService.createFetchGemeindeDataScheduledJob(gesuch));

        verify(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }
}
