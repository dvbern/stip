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

package ch.dvbern.stip.api.dokument.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentKommentarCopyUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchStatusUtil;
import ch.dvbern.stip.api.gesuchhistory.repository.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final GesuchDokumentKommentarHistoryRepository gesuchDokumentKommentarHistoryRepository;
    private final GesuchDokumentKommentarMapper gesuchDokumentKommentarMapper;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentHistoryRepository gesuchDokumentHistoryRepository;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchHistoryRepository gesuchHistoryRepository;

    @Transactional
    public void deleteForGesuchDokument(UUID gesuchDokumentId) {
        gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokumentId);
    }

    @Transactional
    public void deleteForGesuchTrancheId(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuchDokuments = gesuchTranche.getGesuchDokuments();
        gesuchDokuments
            .forEach(dokument -> gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(dokument.getId()));
    }

    @Transactional
    public void copyKommentareFromTrancheToTranche(final GesuchTranche fromTranche, final GesuchTranche toTranche) {
        final var fromDokumentKommentars = getAllKommentareForGesuchTrancheId(fromTranche.getId());
        final var toGesuchDokuments = toTranche.getGesuchDokuments();

        for (final var fromKommentar : fromDokumentKommentars) {
            for (final var toGesuchDokument : toGesuchDokuments) {
                final var fromGesuchDokument = fromKommentar.getGesuchDokument();

                if (fromGesuchDokument.getDokumentTyp() != null) {
                    if (fromGesuchDokument.getDokumentTyp() == toGesuchDokument.getDokumentTyp()) {
                        final var newKommentar =
                            GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar, toGesuchDokument);
                        gesuchDokumentKommentarRepository.persist(newKommentar);
                    }
                } else if (
                    fromGesuchDokument.getCustomDokumentTyp() != null && toGesuchDokument.getCustomDokumentTyp() != null
                    && (Objects.equals(
                        fromGesuchDokument.getCustomDokumentTyp().getType(),
                        toGesuchDokument.getCustomDokumentTyp().getType()
                    ))
                ) {
                    final var newKommentar =
                        GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar, toGesuchDokument);
                    gesuchDokumentKommentarRepository.persist(newKommentar);
                }
            }
        }
    }

    @Transactional
    public List<GesuchDokumentKommentar> getAllKommentareForGesuchTrancheId(
        final UUID gesuchTrancheId
    ) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuchDokuments = gesuchTranche.getGesuchDokuments();
        ArrayList<GesuchDokumentKommentar> kommentars = new ArrayList<>();
        gesuchDokuments.stream()
            .map(dokument -> gesuchDokumentKommentarRepository.getByGesuchDokumentId(dokument.getId()))
            .forEach(kommentars::addAll);
        return kommentars;
    }

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchDokumentGS(
        final UUID gesuchDokumentId
    ) {
        var gesuchDokument = gesuchDokumentRepository.findById(gesuchDokumentId);
        if (Objects.isNull(gesuchDokument)) {
            gesuchDokument = gesuchDokumentHistoryRepository.findInHistoryById(gesuchDokumentId);
        }

        var gesuchTranche = gesuchTrancheRepository.requireById(gesuchDokument.getGesuchTranche().getId());
        Integer gesuchTrancheRevision = null;
        final var gesuch = gesuchTranche.getGesuch();
        if (gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE) {
            if (GesuchStatusUtil.gsReceivesGesuchdataOfStateEingereicht(gesuch)) {
                final var gesuchTrancheFehlendeDokumentRevisionOpt = gesuchHistoryRepository
                    .getRevisionWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.FEHLENDE_DOKUMENTE);
                gesuchTrancheRevision = gesuchTrancheFehlendeDokumentRevisionOpt.orElseGet(
                    () -> gesuchHistoryRepository
                        .getRevisionWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.EINGEREICHT)
                        .orElseThrow()
                );
            }
        } else if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && (gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            gesuchTrancheRevision =
                gesuchTrancheHistoryRepository
                    .getLatestRevisionWhereStatusChangedTo(gesuchTranche.getId(), GesuchTrancheStatus.UEBERPRUEFEN)
                    .orElseThrow();
            // TODO KSTIP-1910: add handling for tranche status Fehlende Dokumente
        }

        List<GesuchDokumentKommentar> gesuchDokumentKommentars =
            gesuchDokumentKommentarRepository.getByGesuchDokumentId(gesuchDokumentId);

        if (Objects.nonNull(gesuchTrancheRevision)) {
            gesuchDokumentKommentars = gesuchDokumentKommentarHistoryRepository
                .getGesuchDokumentKommentarOfGesuchDokumentAtRevision(gesuchDokumentId, gesuchTrancheRevision);
        }

        return gesuchDokumentKommentars.stream()
            .map(gesuchDokumentKommentarMapper::toDto)
            .toList();
    }

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchDokumentSB(
        final UUID gesuchDokumentId
    ) {
        final var gesuchDokumentKommentars =
            gesuchDokumentKommentarRepository
                .getByGesuchDokumentId(gesuchDokumentId);

        if (gesuchDokumentKommentars != null) {
            return gesuchDokumentKommentars.stream()
                .map(gesuchDokumentKommentarMapper::toDto)
                .toList();
        }
        return List.of();
    }

    @Transactional
    public void createKommentarForGesuchDokument(
        final GesuchDokument gesuchDokument,
        final GesuchDokumentKommentarDto gesuchDokumentKommentarDto
    ) {
        final var kommentar = gesuchDokumentKommentarMapper.toEntity(gesuchDokumentKommentarDto);
        if (gesuchDokumentKommentarDto == null) {
            createEmptyKommentarForGesuchDokument(gesuchDokument);
        } else {
            kommentar.setGesuchDokument(gesuchDokument);
            kommentar.setDokumentstatus(gesuchDokument.getStatus());
            gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
        }
    }

    @Transactional
    public void createEmptyKommentarForGesuchDokument(final GesuchDokument gesuchDokument) {
        final var kommentar = new GesuchDokumentKommentar()
            .setDokumentstatus(gesuchDokument.getStatus())
            .setGesuchDokument(gesuchDokument)
            .setKommentar(null);
        gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
    }
}
