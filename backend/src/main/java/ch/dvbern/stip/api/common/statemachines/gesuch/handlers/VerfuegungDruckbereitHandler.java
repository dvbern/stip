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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VerfuegungDruckbereitHandler implements GesuchStatusChangeHandler {
    private final VerfuegungService verfuegungService;
    private final UnterschriftenblattService unterschriftenblattService;
    private final VerfuegungPdfService verfuegungPdfService;

    @Override
    public void handle(Gesuch gesuch) {
        final var verfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());

        final VerfuegungDokument verfuegungstextDocument = getVerfuegungstextDocument(verfuegung);
        final byte[] verfuegungstextBytes = verfuegungService.downloadVerfuegungDokument(verfuegungstextDocument);
        final ByteArrayOutputStream verfuegungstext = new ByteArrayOutputStream();
        try {
            verfuegungstext.write(verfuegungstextBytes);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to process Verfuegungstext", e);
        }

        final List<ByteArrayOutputStream> berechnungsblaetter = new ArrayList<>();
        final Set<UnterschriftenblattDokumentTyp> uploadedTyps =
            unterschriftenblattService.getExistingUnterschriftenblattTypsForGesuch(gesuch.getId());

        for (UnterschriftenblattDokumentTyp unterschriftenblattTyp : uploadedTyps) {
            final VerfuegungDokumentTyp berechnungsblattTyp = mapToBerechnungsblattTyp(unterschriftenblattTyp);

            try {
                final VerfuegungDokument berechnungsblatt =
                    verfuegungService.getBerechnungsblattByType(verfuegung, berechnungsblattTyp);

                final byte[] berechnungsblattBytes = verfuegungService.downloadVerfuegungDokument(berechnungsblatt);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(berechnungsblattBytes);
                berechnungsblaetter.add(baos);
            } catch (Exception e) {
                final var message = String.format(
                    "Berechnungsblatt %s not found for Gesuch %s, skipping",
                    berechnungsblattTyp,
                    gesuch.getId()
                );
                throw new InternalServerErrorException(message, e);
            }
        }

        final ByteArrayOutputStream versendeteVerfuegung = verfuegungPdfService.createVersendeteVerfuegung(
            verfuegungstext,
            berechnungsblaetter
        );

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG,
            versendeteVerfuegung
        );

    }

    private VerfuegungDokument getVerfuegungstextDocument(Verfuegung verfuegung) {
        return verfuegung.getDokumente()
            .stream()
            .filter(
                d -> VerfuegungDokumentTyp.VERFUEGUNGSBRIEF.contains(d.getDokumentTyp())
            )
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Verfuegungstext document not found"));
    }

    private VerfuegungDokumentTyp mapToBerechnungsblattTyp(UnterschriftenblattDokumentTyp typ) {
        return switch (typ) {
            case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
            case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
            case GEMEINSAM -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
        };
    }
}
