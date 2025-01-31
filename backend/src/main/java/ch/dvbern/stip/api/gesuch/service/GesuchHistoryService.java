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

package ch.dvbern.stip.api.gesuch.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchHistoryService {
    private final GesuchHistoryRepository gesuchHistoryRepository;
    private final StatusprotokollMapper statusprotokollMapper;

    public List<StatusprotokollEntryDto> getStatusprotokoll(final UUID gesuchId) {
        final var revisions = gesuchHistoryRepository.getStatusHistory(gesuchId);
        return revisions.stream().map(statusprotokollMapper::toDto).toList();
    }

    public Optional<Gesuch> getLatestWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        return gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuchId, gesuchStatus);
    }
}
