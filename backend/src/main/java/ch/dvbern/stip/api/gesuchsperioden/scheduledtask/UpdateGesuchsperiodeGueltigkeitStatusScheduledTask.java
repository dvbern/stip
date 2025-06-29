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

package ch.dvbern.stip.api.gesuchsperioden.scheduledtask;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenant;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Singleton
@Slf4j
public class UpdateGesuchsperiodeGueltigkeitStatusScheduledTask {
    private final GesuchsperiodenService gesuchsperiodenService;

    @Transactional
    @Scheduled(cron = "{kstip.gesuchsperiode.cron}", timeZone = "Europe/Zurich")
    @RunForTenant(MandantIdentifier.BERN)
    public void runForBern() {
        run();
    }

    @Transactional
    @Scheduled(cron = "{kstip.gesuchsperiode.cron}", timeZone = "Europe/Zurich")
    @RunForTenant(MandantIdentifier.DV)
    public void runForDV() {
        run();
    }

    private void run() {
        try {
            LOG.info("Start checking for any Gesuchperioden to be archived");
            gesuchsperiodenService.setOutdatedGesuchsperiodenToArchiviert();
            LOG.info("Stopped checking for any Gesuchperioden to be archived");
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
