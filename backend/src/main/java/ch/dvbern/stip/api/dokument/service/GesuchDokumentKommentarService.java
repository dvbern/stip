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
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentKommentarCopyUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
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
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;

    @Transactional
    public void deleteForGesuchDokument(UUID gesuchDokumentId) {
        gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokumentId);
    }

    @Transactional
    public void deleteForGesuchTrancheId(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuchDokuments = gesuchTranche.getGesuchDokuments();
        gesuchDokuments
            .forEach(dokument -> {
                gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(dokument.getId());
            });
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
                        final var newKommentar = GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar);
                        gesuchDokumentKommentarRepository.persistAndFlush(newKommentar);
                    }
                } else if (
                    fromGesuchDokument.getCustomDokumentTyp() != null && toGesuchDokument.getCustomDokumentTyp() != null
                    && (Objects.equals(
                        fromGesuchDokument.getCustomDokumentTyp().getType(),
                        toGesuchDokument.getCustomDokumentTyp().getType()
                    ))
                ) {
                    final var newKommentar = GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar);
                    gesuchDokumentKommentarRepository.persistAndFlush(newKommentar);
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
        var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);

        var gesuchTranche =
            gesuchTrancheHistoryService.getCurrentOrEingereichtTrancheForGS(gesuchDokument.getGesuchTranche().getId());

        var gesuchTrancheOpt = gesuchTrancheHistoryRepository.findCurrentGesuchTrancheOfGesuchInStatus(
            gesuchDokument.getGesuchTranche().getGesuch().getId(),
            Gesuchstatus.FEHLENDE_DOKUMENTE
        );

        if (gesuchTrancheOpt.isPresent()) {
            gesuchTranche = gesuchTrancheOpt.get();
        }

        final var gesuchDokumentOpt = gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(gesuchDokument1 -> gesuchDokument1.getId().equals(gesuchDokumentId))
            .findFirst();

        List<GesuchDokumentKommentar> gesuchDokumentKommentars = List.of();

        if (gesuchDokumentOpt.isPresent()) {
            gesuchDokument = gesuchDokumentOpt.get();
            gesuchDokumentKommentars =
                gesuchDokumentKommentarHistoryRepository.getGesuchDokumentKommentarOfGesuchDokumentBefore(
                    gesuchDokument.getId(),
                    gesuchDokument.getTimestampMutiert()
                );
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
