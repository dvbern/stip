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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheOverrideDokumentService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchDokumentKommentarService gesuchDokumentKommentarService;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;

    void overrideJahreswertDokumente(final Gesuch gesuch, final GesuchTranche newTranche) {
        final var targetTranchen = gesuch.getTranchenTranchen()
            .filter(tranche -> !tranche.getId().equals(newTranche.getId()))
            .toList();

        if (targetTranchen.isEmpty()) {
            return;
        }

        // Create a Map<Tranche ID, Map<DokumentTyp, GesuchDokument>>
        // to simplify lookup of GesuchDokumente by Tranche ID and Dokument Typ
        final var targetTranchenLUT = targetTranchen.stream()
            .collect(
                Collectors.toMap(
                    AbstractEntity::getId,
                    tranche -> tranche.getGesuchDokuments()
                        .stream()
                        .collect(Collectors.toMap(GesuchDokument::getDokumentTyp, gesuchDokument -> gesuchDokument))
                )
            );

        // Called when a new dokument is found on a different Jahreswert field on a different Tranche
        // This overrides the old GesuchDokument with the content of the new one
        final BiConsumer<DokumentTyp, GesuchDokument> newDokumentFound = (jahreswertDokument, newDokument) -> {
            for (final var targetTranche : targetTranchen) {
                var oldDokument = targetTranchenLUT.get(targetTranche.getId()).get(jahreswertDokument);
                if (oldDokument == null) {
                    oldDokument = gesuchDokumentService.copyGesuchDokument(newDokument, targetTranche);
                } else {
                    oldDokument.getDokumente().clear();
                }

                gesuchDokumentService.copyDokumente(oldDokument, newDokument.getDokumente());
            }
        };

        // Called when a Jahreswert field had no Dokument previously but now has one
        // This syncs the new Dokument to all other Tranchen
        final Consumer<DokumentTyp> noNewDokument = (jahreswertDokument) -> {
            for (final var targetTranche : targetTranchen) {
                final var oldDokumente = targetTranche.getGesuchDokuments()
                    .stream()
                    .filter(gesuchDokument -> gesuchDokument.getDokumentTyp() == jahreswertDokument)
                    .toList();

                for (final var oldDokument : oldDokumente) {
                    oldDokument.getDokumente().clear();
                }
            }
        };

        final var newDokumente = newTranche.getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> gesuchDokument.getDokumentTyp() != null)
            .filter(gesuchDokument -> gesuchDokumentService.isDokumentOfJahreswert(gesuchDokument.getDokumentTyp()))
            .collect(Collectors.toMap(GesuchDokument::getDokumentTyp, gesuchDokument -> gesuchDokument));

        for (final var jahreswertDokument : gesuchDokumentService.getDokumentTypsOfJahreswerte()) {
            if (newDokumente.containsKey(jahreswertDokument)) {
                newDokumentFound.accept(jahreswertDokument, newDokumente.get(jahreswertDokument));
            } else {
                noNewDokument.accept(jahreswertDokument);
            }
        }

        final var jahresfeldKommentare = newTranche.getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> gesuchDokumentService.isDokumentOfJahreswert(gesuchDokument.getDokumentTyp()))
            .flatMap(
                gesuchDokument -> gesuchDokumentKommentarRepository.getByGesuchDokumentId(gesuchDokument.getId())
                    .stream()
            )
            .toList();

        for (final var targetTranche : targetTranchen) {
            // Override the Kommentare with the ones on the incoming Aenderung
            // this ensures that Jahresfeld Kommentare are properly synchronised
            gesuchDokumentKommentarService.overrideKommentareOnTranche(jahresfeldKommentare, targetTranche);
        }
    }

    @Transactional
    public void jahresfeldGesuchDokumentAblehnen(
        UUID gesuchDokumentId,
        GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto
    ) {
        forEachOtherTrancheOfDokument(
            gesuchDokumentId,
            (otherGesuchDokument) -> gesuchDokumentService
                .gesuchDokumentAblehnen(otherGesuchDokument, gesuchDokumentAblehnenRequestDto)
        );
    }

    @Transactional
    public void jahresfeldGesuchDokumentAkzeptieren(UUID gesuchDokumentId) {
        forEachOtherTrancheOfDokument(gesuchDokumentId, gesuchDokumentService::gesuchDokumentAkzeptieren);
    }

    private void forEachOtherTrancheOfDokument(UUID gesuchDokumentId, Consumer<GesuchDokument> withGesuchDokument) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        final var gesuchTranche = gesuchDokument.getGesuchTranche();

        if (
            Objects.isNull(gesuchDokument.getDokumentTyp())
            || !gesuchDokumentService.isDokumentOfJahreswert(gesuchDokument.getDokumentTyp())
            || gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
        ) {
            return;
        }

        final var otherTranchen = gesuchTranche.getGesuch()
            .getTranchenTranchen()
            .filter(tranche -> !tranche.getId().equals(gesuchTranche.getId()));

        otherTranchen.forEach(otherTranche -> {
            final var otherGesuchDokumentOpt = otherTranche
                .getGesuchDokuments()
                .stream()
                .filter(d -> Objects.requireNonNull(d.getDokumentTyp()).equals(gesuchDokument.getDokumentTyp()))
                .findFirst();

            otherGesuchDokumentOpt.ifPresent(withGesuchDokument);
        });
    }
}
