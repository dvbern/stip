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

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.sap.service.SapService;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VerfuegtHandler implements GesuchStatusStateChangeHandler {
    private final ConfigService configService;
    private final BerechnungService berechnungService;
    private final BuchhaltungService buchhaltungService;
    private final GesuchService gesuchService;
    private final SapService sapService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getDestination() == Gesuchstatus.VERFUEGT;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        final var gesuchToUse = gesuchService.getGesuchById(gesuch.getId());
        final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
            gesuchToUse,
            configService.getCurrentDmnMajorVersion(),
            configService.getCurrentDmnMinorVersion()
        );

        final int berechnungsresultat = stipendien.getBerechnungReduziert() != null
            ? stipendien.getBerechnungReduziert()
            : stipendien.getBerechnung();

        if (berechnungsresultat > 0) {
            buchhaltungService.createStipendiumBuchhaltungEntry(
                gesuchToUse,
                berechnungsresultat
            );
        }
        sapService.createInitialAuszahlungOrGetStatus(
            gesuch.getLatestGesuchTranche()
                .getGesuchFormular()
                .getAuszahlung()
        );
    }
}
