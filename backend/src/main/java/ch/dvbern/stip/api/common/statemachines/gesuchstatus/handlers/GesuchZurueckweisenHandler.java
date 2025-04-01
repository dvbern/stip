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
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheOverrideUtil;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class GesuchZurueckweisenHandler implements GesuchStatusStateChangeHandler {
    private final GesuchService gesuchService;
    private final GesuchTrancheService gesuchTrancheService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        final var source = transition.getSource();
        final var handlesSource = switch (source) {
            case IN_BEARBEITUNG_SB, FEHLENDE_DOKUMENTE -> true;
            default -> false;
        };

        return handlesSource && transition.getDestination() == Gesuchstatus.IN_BEARBEITUNG_GS;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        gesuch.setEinreichedatum(null);
        gesuch.setNachfristDokumente(null);
        resetGesuchFormular(gesuch);
    }

    private void resetGesuchFormular(final Gesuch gesuch) {
        final var gesuchOfStateEingereicht = gesuchService.getLatestEingereichtVersion(gesuch.getId())
            .orElseThrow(NotFoundException::new);

        if (gesuchOfStateEingereicht.getGesuchTranchen().size() != 1) {
            throw new IllegalStateException("Trying to reset to a Gesuch which has more than 1 Tranchen");
        }

        final var trancheOfStateEingereicht = gesuchOfStateEingereicht.getGesuchTranchen().get(0);

        final var trancheToReset = gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getId().equals(trancheOfStateEingereicht.getId()))
            .findFirst()
            .orElseGet(gesuch::getLatestGesuchTranche);

        final var formularOfStateEingereicht = trancheOfStateEingereicht.getGesuchFormular();

        trancheToReset.setGueltigkeit(trancheOfStateEingereicht.getGueltigkeit());

        GesuchTrancheOverrideUtil.overrideGesuchFormular(
            trancheToReset.getGesuchFormular(),
            formularOfStateEingereicht
        );

        final var allOtherTranchen = gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> !tranche.getId().equals(trancheToReset.getId()))
            .toList();

        for (final var trancheToDrop : allOtherTranchen) {
            gesuchTrancheService.dropGesuchTranche(trancheToDrop);
        }
    }
}
