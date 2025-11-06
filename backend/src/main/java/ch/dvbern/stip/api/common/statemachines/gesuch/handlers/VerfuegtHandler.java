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

import java.io.IOException;
import java.util.Locale;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.BerechnungsblattService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VerfuegtHandler implements GesuchStatusChangeHandler {
    private final ConfigService configService;
    private final BerechnungService berechnungService;
    private final BuchhaltungService buchhaltungService;
    private final VerfuegungService verfuegungService;
    private final BerechnungsblattService berechnungsblattService;
    private final VerfuegungPdfService verfuegungPdfService;

    @Override
    public void handle(Gesuch gesuch) {
        final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
            gesuch,
            configService.getCurrentDmnMajorVersion(),
            configService.getCurrentDmnMinorVersion()
        );

        final int berechnungsresultat = stipendien.getBerechnungReduziert() != null
            ? stipendien.getBerechnungReduziert()
            : stipendien.getBerechnung();

        final var verfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());
        final Locale locale = LocaleUtil.getLocale(gesuch);

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.BERECHNUNGSBLATT_PIA,
            berechnungsblattService.getBerechnungsblattPersonInAusbildung(gesuch, locale)
        );

        for (var tranchenResultat : stipendien.getTranchenBerechnungsresultate()) {
            for (var familienBudget : tranchenResultat.getFamilienBudgetresultate()) {
                final SteuerdatenTyp typ = familienBudget.getFamilienBudgetTyp();
                final VerfuegungDokumentTyp dokumentTyp = switch (typ) {
                    case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
                    case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
                    case FAMILIE -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
                };

                verfuegungService.createAndStoreVerfuegungDokument(
                    verfuegung,
                    dokumentTyp,
                    berechnungsblattService.getBerechnungsblattFamilie(gesuch, locale, typ)
                );
            }
        }

        if (berechnungsresultat == 0) {
            try {
                verfuegungService.createPdfForVerfuegungOhneAnspruch(
                    verfuegungService.getLatestVerfuegung(gesuch.getId())
                );
            } catch (IOException e) {
                throw new InternalServerErrorException(e);
            }
        }

        if (berechnungsresultat > 0) {
            buchhaltungService.createStipendiumBuchhaltungEntry(
                gesuch,
                berechnungsresultat
            );

            verfuegungService.createPdfForVerfuegungMitAnspruch(
                verfuegungService.getLatestVerfuegung(gesuch.getId())
            );
        }
    }
}
