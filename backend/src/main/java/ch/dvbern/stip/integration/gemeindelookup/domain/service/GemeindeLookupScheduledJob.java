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

import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.gesuch.service.StatisticsdataService;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import ch.dvbern.stip.integration.gemeindelookup.domain.port.GemeindeLookupPortFactory;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Singleton
@RequiredArgsConstructor
public class GemeindeLookupScheduledJob implements Job {
    private final GemeindeLookupPortFactory gemeindeLookupPortFactory;
    private final StatisticsdataService statisticsdataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final var request = new GemeindeLookupRequest(
            context.getJobDetail().getJobDataMap()
        );

        QuarkusTransactionUtil.runForTenantInNewTransaction(
            request.tenantIdentifier().getIdentifier(),
            () -> {
                final var gemeindeData =
                    gemeindeLookupPortFactory.getGemeindeLookupPort().findGemeindeData(request).orElseThrow();
                statisticsdataService.setOrCreateGemeindeStatisticsDataOfGesuch(
                    request.gesuchId(),
                    gemeindeData.bfsNummer(),
                    gemeindeData.name()
                );
            }
        );
    }
}
