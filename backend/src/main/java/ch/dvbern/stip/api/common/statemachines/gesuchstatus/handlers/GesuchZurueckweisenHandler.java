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

package ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class GesuchZurueckweisenHandler implements GesuchStatusStateChangeHandler {
    private final GesuchService gesuchService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        final var source = transition.getSource();
        final var handlesSource = switch (source) {
            case IN_BEARBEITUNG_SB, FEHLENDE_DOKUMENTE -> true;
            default -> false;
        };

        return handlesSource && transition.getDestination() == Gesuchstatus.IN_BEARBEITUNG_GS;
    }

    @Transactional
    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        if (gesuch.isVerfuegt()) {
            illegalHandleCall();
        }
        gesuch.setEinreichedatum(null);
        gesuch.setNachfristDokumente(null);
        gesuchService.resetGesuchZurueckweisen(gesuch);
    }
}
