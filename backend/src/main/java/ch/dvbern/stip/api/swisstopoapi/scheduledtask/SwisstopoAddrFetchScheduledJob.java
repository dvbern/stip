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

package ch.dvbern.stip.api.swisstopoapi.scheduledtask;

import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.swisstopoapi.entity.SwisstopoAddrFetchJobData;
import ch.dvbern.stip.api.swisstopoapi.service.SwisstopoService;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@RequiredArgsConstructor
@Singleton
public class SwisstopoAddrFetchScheduledJob implements Job {
    private final SwisstopoService swisstopoService;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final var jobData = new SwisstopoAddrFetchJobData(
            jobExecutionContext.getJobDetail().getJobDataMap()
        );
        QuarkusTransactionUtil.runForTenantInNewTransaction(
            jobData.getMandantIdentifier(),
            () -> swisstopoService.getGemeindeDataOfGesuch(
                jobData.getGesuchId(),
                jobData.getStrasse(),
                jobData.getHausnummer(),
                jobData.getPlz(),
                jobData.getOrt()
            )
        );
    }
}
