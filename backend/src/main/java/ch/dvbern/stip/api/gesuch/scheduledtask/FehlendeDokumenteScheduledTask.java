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

package ch.dvbern.stip.api.gesuch.scheduledtask;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenantsScheduledTask;
import ch.dvbern.stip.api.common.type.ScheduledTaskCronKey;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@UnlessBuildProfile("test")
public class FehlendeDokumenteScheduledTask extends RunForTenantsScheduledTask {
    @Inject
    GesuchService gesuchService;

    FehlendeDokumenteScheduledTask() {
        super(ScheduledTaskCronKey.FEHLENDE_DOKUMENTE, TenantIdentifier.values());
    }

    @Override
    @Transactional
    protected void run() {
        try {
            LOG.info("Processing gesuchs in FEHLENDE_DOKUMENTE");
            gesuchService.checkForFehlendeDokumenteOnAllGesuche();
            gesuchService.checkForFehlendeDokumenteOnAllAenderungen();
            LOG.info("Done processing gesuchs in FEHLENDE_DOKUMENTE");
        } catch (Throwable e) {
            LOG.error(e.toString(), e);
        }
    }
}
