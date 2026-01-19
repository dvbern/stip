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

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VerfuegungDruckbereitHandler implements GesuchStatusChangeHandler {
    private final ConfigService configService;
    private final BerechnungService berechnungService;
    private final BuchhaltungService buchhaltungService;
    private final VerfuegungPdfService verfuegungPdfService;
    private final VerfuegungService verfuegungService;

    @Override
    public void handle(Gesuch gesuch) {
        final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
            gesuch,
            configService.getCurrentDmnMajorVersion(),
            configService.getCurrentDmnMinorVersion()
        );

        final int berechnungsresultat = stipendien.getBerechnungReduziert() != null
            ? stipendien.getBerechnungReduziert()
            : stipendien.getBerechnungTotal();

        final var latestVerfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());

        if ((berechnungsresultat > 0 || !gesuch.isFirstVerfuegung()) && !latestVerfuegung.isNegativeVerfuegung()) {
            buchhaltungService.createStipendiumBuchhaltungEntry(
                gesuch,
                berechnungsresultat
            );
        }

        verfuegungPdfService.createVerfuegungsDocuments(gesuch, stipendien);
    }
}
