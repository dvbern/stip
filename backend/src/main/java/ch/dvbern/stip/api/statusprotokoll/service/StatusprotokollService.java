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

package ch.dvbern.stip.api.statusprotokoll.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.statusprotokoll.entity.Statusprotokoll;
import ch.dvbern.stip.api.statusprotokoll.repo.StatusprotokollRepository;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class StatusprotokollService {
    private final StatusprotokollRepository statusprotokollRepository;
    private final StatusprotokollMapper statusprotokollMapper;

    @Transactional
    public List<StatusprotokollEntryDto> getStatusprotokoll(final UUID gesuchId) {
        return statusprotokollRepository.getAllOfGesuch(gesuchId).stream().map(statusprotokollMapper::toDto).toList();
    }

    @Transactional
    public Statusprotokoll createStatusprotokoll(
        final String statusTo,
        final String statusFrom,
        final StatusprotokollEntryTyp typ,
        final String comment,
        final Gesuch gesuch
    ) {
        final var statusprotokoll = new Statusprotokoll().setStatusTo(statusTo)
            .setStatusFrom(statusFrom)
            .setTyp(typ)
            .setComment(comment)
            .setGesuch(gesuch);
        statusprotokollRepository.persist(statusprotokoll);
        return statusprotokoll;
    }
}
