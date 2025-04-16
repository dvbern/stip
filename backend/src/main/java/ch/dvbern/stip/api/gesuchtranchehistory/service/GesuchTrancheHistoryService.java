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

package ch.dvbern.stip.api.gesuchtranchehistory.service;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheHistoryService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchHistoryService gesuchHistoryService;

    public GesuchTranche getCurrentOrHistoricalTrancheForGS(final UUID gesuchTrancheId) {
        var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && (gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            return gesuchTrancheHistoryRepository.getLatestWhereStatusChangedToUeberpruefen(gesuchTrancheId);
        }
        final var gesuchToReturnFrom =
            gesuchHistoryService.getCurrentOrHistoricalGesuchForGS(gesuchTranche.getGesuch().getId());

        return gesuchToReturnFrom
            .getGesuchTranchen()
            .stream()
            .filter(gesuchTranche1 -> gesuchTranche1.getId().equals(gesuchTrancheId))
            .findFirst()
            .orElse(
                gesuchToReturnFrom.getGesuchTranchen().get(0)
            );
    }

    public Optional<Integer> getHistoricalTrancheRevisionForGS(final UUID gesuchTrancheId) {
        var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && (gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            return gesuchTrancheHistoryRepository
                .getLatestRevisionWhereStatusChangedTo(gesuchTranche.getId(), GesuchTrancheStatus.UEBERPRUEFEN);
        }

        return gesuchHistoryService.getHistoricalGesuchRevisionForGS(gesuchTranche.getGesuch().getId());
    }

    @Transactional
    public Optional<GesuchTranche> getGesuchTrancheAtRevision(final UUID gesuchTrancheId, final Integer revision) {
        return gesuchTrancheHistoryRepository.getGesuchTrancheAtRevision(gesuchTrancheId, revision);
    }
}
