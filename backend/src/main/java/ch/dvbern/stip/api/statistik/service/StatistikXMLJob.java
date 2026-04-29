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

package ch.dvbern.stip.api.statistik.service;

import ch.dvbern.stip.api.common.util.QuarkusTransactionUtil;
import ch.dvbern.stip.api.statistik.util.StatistikConstants;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Singleton
@RequiredArgsConstructor
public class StatistikXMLJob implements Job {
    private final StatistikXMLService statistikXMLService;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final int year = Integer
            .parseInt(context.getMergedJobDataMap().getString(StatistikConstants.STATISTIK_JOB_CONTEXT_MAP_YEAR_KEY));
        final String triggeredBy =
            context.getMergedJobDataMap().getString(StatistikConstants.STATISTIK_JOB_CONTEXT_MAP_USER_KEY);
        final String tenant =
            context.getMergedJobDataMap().getString(StatistikConstants.STATISTIK_JOB_CONTEXT_MAP_TENANT_KEY);

        QuarkusTransactionUtil.runForTenantInNewTransaction(
            tenant,
            () -> statistikXMLService.createAndSave(year, triggeredBy)
        );
    }
}
