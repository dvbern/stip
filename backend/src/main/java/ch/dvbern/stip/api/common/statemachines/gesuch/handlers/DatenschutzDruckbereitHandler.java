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

package ch.dvbern.stip.api.common.statemachines.gesuch.handlers;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class DatenschutzDruckbereitHandler implements GesuchStatusChangeHandler {
    private final GesuchStatusService gesuchStatusService;

    @Transactional
    @Override
    public void handle(Gesuch gesuch) {
        // todo KSTIP-2685: generate Datenschutzbriefe here
        // automatic status change, if no Datenschutzblaetter required ( = no Elterns exist)
        if (gesuch.getLatestGesuchTranche().getGesuchFormular().getElterns().isEmpty()) {
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
        }
    }
}
