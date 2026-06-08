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

package ch.dvbern.stip.api.unterschriftenblatt.scheduledtask;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenantsScheduledTask;
import ch.dvbern.stip.api.common.type.ScheduledTaskCronKey;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UnterschriftenblattUploadCheckScheduledTask extends RunForTenantsScheduledTask {
    private static final String NAME = "UnterschriftenblattUploadCheck";

    @Inject
    UnterschriftenblattService unterschriftenblattService;

    public UnterschriftenblattUploadCheckScheduledTask() {
        super(NAME, ScheduledTaskCronKey.UNTERSCHRIFTENBLATT, TenantIdentifier.values());
    }

    @Override
    @Transactional
    protected void run() {
        try {
            LOG.info("Checking Unterschriftenblaetter for Bern");
            unterschriftenblattService.checkForUnterschriftenblaetterOnAllGesuche();
            LOG.info("Done checking Unterschriftenblaetter for Bern");
        } catch (Throwable e) {
            LOG.error(e.toString(), e);
        }
    }
}
