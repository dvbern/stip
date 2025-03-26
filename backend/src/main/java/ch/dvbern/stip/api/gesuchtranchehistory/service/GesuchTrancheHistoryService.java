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

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.util.GesuchStatusUtil;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheHistoryService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;

    public GesuchTranche getCurrentOrEingereichtTrancheForGS(final UUID gesuchTrancheId) {
        var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        if (gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE) {
            if (GesuchStatusUtil.gsReceivesGesuchdataOfStateEingereicht(gesuch)) {
                gesuchTranche =
                    gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToEingereicht(gesuch.getId())
                        .orElseThrow();
            }
        } else if (
            gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && (gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            gesuchTranche = gesuchTrancheHistoryRepository.getLatestWhereStatusChangedToUeberpruefen(gesuchTrancheId);
        }

        return gesuchTranche;
    }
}
