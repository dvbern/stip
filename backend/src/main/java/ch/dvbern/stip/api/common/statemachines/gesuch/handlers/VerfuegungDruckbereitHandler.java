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

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.BerechnungsblattService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class VerfuegungDruckbereitHandler implements GesuchStatusChangeHandler {
    private final ConfigService configService;
    private final BerechnungService berechnungService;
    private final BuchhaltungService buchhaltungService;
    private final VerfuegungService verfuegungService;
    private final VerfuegungPdfService verfuegungPdfService;
    private final BerechnungsblattService berechnungsblattService;

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

        ByteArrayOutputStream verfuegungsBrief = new ByteArrayOutputStream();

        if (berechnungsresultat == 0) {
            verfuegungsBrief = verfuegungPdfService.createVerfuegungOhneAnspruchPdf(
                verfuegungService.getLatestVerfuegung(gesuch.getId())
            );
        }

        if (berechnungsresultat > 0) {
            buchhaltungService.createStipendiumBuchhaltungEntry(
                gesuch,
                berechnungsresultat
            );

            verfuegungsBrief = verfuegungPdfService
                .createVerfuegungMitAnspruchPdf(verfuegungService.getLatestVerfuegung(gesuch.getId()));
        }

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERFUEGUNGSBRIEF,
            verfuegungsBrief
        );

        createAndStoreBerechnungsblattPdf(gesuch, locale, verfuegung, stipendien, SteuerdatenTyp.MUTTER);
        createAndStoreBerechnungsblattPdf(gesuch, locale, verfuegung, stipendien, SteuerdatenTyp.VATER);
        createAndStoreBerechnungsblattPdf(gesuch, locale, verfuegung, stipendien, SteuerdatenTyp.FAMILIE);

        var berechnungsBlaetterPia =
            berechnungsblattService.getBerechnungsblattPersonInAusbildung(gesuch, locale, stipendien);

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.BERECHNUNGSBLATT_PIA,
            berechnungsBlaetterPia
        );

        var berechnungsBlaetter = berechnungsblattService.getAllBerechnungsblaetterOfGesuch(gesuch, locale, stipendien);

        final var versendeteVerfuegungOutput = verfuegungPdfService.createVersendeteVerfuegung(
            verfuegungsBrief,
            berechnungsBlaetter
        );
        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG,
            versendeteVerfuegungOutput
        );
    }

    private void createAndStoreBerechnungsblattPdf(
        final Gesuch gesuch,
        final Locale locale,
        final Verfuegung verfuegung,
        final BerechnungsresultatDto berechnungsResultat,
        final SteuerdatenTyp steuerdatenTyp
    ) {
        ByteArrayOutputStream berechnungsBlaetter = berechnungsblattService.getAllElternTypeBerechnungsblaetterOfGesuch(
            gesuch,
            locale,
            berechnungsResultat,
            steuerdatenTyp
        );
        if (berechnungsBlaetter != null) {
            verfuegungService.createAndStoreVerfuegungDokument(
                verfuegung,
                mapToVerfuegungDokumentTyp(steuerdatenTyp),
                berechnungsBlaetter
            );
        }
    }

    private VerfuegungDokumentTyp mapToVerfuegungDokumentTyp(final SteuerdatenTyp steuerdatenTyp) {
        return switch (steuerdatenTyp) {
            case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
            case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
            case FAMILIE -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
        };
    }
}
