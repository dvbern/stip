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

package ch.dvbern.stip.api.gesuchhistory.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.util.GesuchStatusUtil;
import ch.dvbern.stip.api.gesuchhistory.repository.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchHistoryService {
    private final GesuchHistoryRepository gesuchHistoryRepository;
    private final GesuchRepository gesuchRepository;
    private final StatusprotokollMapper statusprotokollMapper;

    @Transactional
    public List<StatusprotokollEntryDto> getStatusprotokoll(final UUID gesuchId) {
        final var revisions = gesuchHistoryRepository.getStatusHistory(gesuchId);
        return revisions.stream().map(statusprotokollMapper::toDto).toList();
    }

    public Optional<Gesuch> getFirstWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        return gesuchHistoryRepository.getFirstWhereStatusChangedTo(gesuchId, gesuchStatus);
    }

    public Optional<Gesuch> getLatestWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        return gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuchId, gesuchStatus);
    }

    public Gesuch getCurrentOrHistoricalGesuchForGS(final UUID gesuchId) {
        var gesuch = gesuchRepository.requireById(gesuchId);

        if (GesuchStatusUtil.gsReceivesCurrentGesuch(gesuch.getGesuchStatus())) {
            return gesuch;
        }

        if (gesuch.isVerfuegt()) {
            return gesuchHistoryRepository
                .getLatestWhereStatusChangedToOneOf(gesuchId, Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN)
                .orElseThrow(NotFoundException::new);
        }

        return gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuchId, Gesuchstatus.EINGEREICHT)
            .orElseThrow(NotFoundException::new);
    }

    public Optional<Integer> getHistoricalGesuchRevisionForGS(final UUID gesuchId) {
        var gesuch = gesuchRepository.requireById(gesuchId);
        if (GesuchStatusUtil.gsReceivesCurrentGesuch(gesuch.getGesuchStatus())) {
            return Optional.empty();
        }

        if (gesuch.isVerfuegt()) {
            return gesuchHistoryRepository
                .getRevisionWhereStatusChangedToOneOf(gesuchId, Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN);
        }

        final var gesuchTrancheFehlendeDokumentRevisionOpt = gesuchHistoryRepository
            .getRevisionWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.FEHLENDE_DOKUMENTE);

        if (gesuchTrancheFehlendeDokumentRevisionOpt.isPresent()) {
            return gesuchTrancheFehlendeDokumentRevisionOpt;
        }
        return gesuchHistoryRepository.getRevisionWhereStatusChangedTo(gesuchId, Gesuchstatus.EINGEREICHT);
    }

}
