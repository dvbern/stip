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

import java.util.Set;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AenderungZurueckweisenHandler implements GesuchStatusStateChangeHandler {
    private final GesuchService gesuchService;

    private static final Set<GesuchStatusChangeEvent> POSSIBLE_TRIGGERS = Set.of(
        GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH,
        GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH
    );
    private static final Set<Gesuchstatus> POSSIBLE_DESTINATIONS =
        Set.of(Gesuchstatus.STIPENDIENANSPRUCH, Gesuchstatus.KEIN_STIPENDIENANSPRUCH);

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getSource() == Gesuchstatus.IN_BEARBEITUNG_SB
        && POSSIBLE_TRIGGERS.contains(transition.getTrigger())
        && POSSIBLE_DESTINATIONS.contains(transition.getDestination());
    }

    @Transactional
    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        if (!gesuch.isVerfuegt()) {
            illegalHandleCall();
        }
        gesuchService.resetGesuchZurueckweisen(gesuch);
    }
}
