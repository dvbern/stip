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
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final GesuchDokumentKommentarMapper gesuchDokumentKommentarMapper;

    @Transactional
    public void deleteForGesuchTrancheId(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuchDokuments = gesuchTranche.getGesuchDokuments();
        gesuchDokuments.forEach(dokument -> {
            gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(dokument.getId());
        });
    }

    @Transactional
    public void copyKommentareFromTrancheToTranche(final GesuchTranche fromTranche, final GesuchTranche toTranche) {
        final var dokumentKommentars = getAllKommentareForGesuchTrancheId(fromTranche.getId());
        for (final var kommentar : dokumentKommentars) {
            createKommentarFromTrancheAndExisting(toTranche, kommentar);
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
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchTrancheIdAndDokumentTyp(
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

    public GesuchDokumentKommentar createKommentarFromTrancheAndExisting(
        final GesuchTranche gesuchTranche,
        final GesuchDokumentKommentar gesuchDokumentKommentar
    ) {
        final var kommentar = new GesuchDokumentKommentar()
            .setGesuchDokument(gesuchDokumentKommentar.getGesuchDokument())
            .setKommentar(gesuchDokumentKommentar.getKommentar())
            .setDokumentstatus(gesuchDokumentKommentar.getDokumentstatus());
        gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
        return kommentar;
    }
}
