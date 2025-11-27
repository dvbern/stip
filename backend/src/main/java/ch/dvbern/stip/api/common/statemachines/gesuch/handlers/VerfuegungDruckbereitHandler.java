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
import java.io.IOException;

import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.BerechnungsblattService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class VerfuegungDruckbereitHandler implements GesuchStatusChangeHandler {
    private final VerfuegungService verfuegungService;
    private final VerfuegungPdfService verfuegungPdfService;
    private final BerechnungsblattService berechnungsblattService;

    @Override
    public void handle(Gesuch gesuch) {
        final var verfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());

        final var verfuegungsbriefDocument = getVerfuegungsbriefDocument(verfuegung);
        final var verfuegungsbriefBytes = verfuegungService.getVerfuegungDokumentFromS3(verfuegungsbriefDocument);
        final var verfuegungsbriefOutput = new ByteArrayOutputStream();
        try {
            verfuegungsbriefOutput.write(verfuegungsbriefBytes);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to process Verfuegungsbrief", e);
        }
        final var locale = LocaleUtil.getLocale(gesuch);

        final var versendeteVerfuegungOutput = verfuegungPdfService.createVersendeteVerfuegung(
            verfuegungsbriefOutput,
            berechnungsblattService.getAllBerechnungsblaetterOfGesuch(gesuch, locale)
        );
        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG,
            versendeteVerfuegungOutput
        );
    }

    private VerfuegungDokument getVerfuegungsbriefDocument(Verfuegung verfuegung) {
        return verfuegung.getDokumente()
            .stream()
            .filter(
                d -> d.getTyp() == VerfuegungDokumentTyp.VERFUEGUNGSBRIEF
            )
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Verfuegungsbrief document not found"));
    }

    private VerfuegungDokumentTyp mapToBerechnungsblattTyp(UnterschriftenblattDokumentTyp typ) {
        return switch (typ) {
            case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
            case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
            case GEMEINSAM -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
        };
    }
}
