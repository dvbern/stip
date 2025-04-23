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

package ch.dvbern.stip.api.beschwerdeverlauf.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.beschwerdeverlauf.repo.BeschwerdeVerlaufRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BeschwerdeverlaufService {
    private final BeschwerdeVerlaufRepository beschwerdeverlaufRepository;
    private final GesuchRepository gesuchRepository;
    private final BeschwerdeverlaufMapper beschwerdeverlaufMapper;

    @Transactional
    public List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntriesByGesuchId(final UUID gesuchId) {
        return beschwerdeverlaufRepository.findByGesuchId(gesuchId)
            .stream()
            .map(beschwerdeverlaufMapper::toDto)
            .toList();
    }

    @Transactional
    public BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(
        final UUID gesuchId,
        final BeschwerdeVerlaufEntryCreateDto createDto
    ) {
        var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuch.isBeschwerdeHaengig() == createDto.getBeschwerdeSetTo()) {
            throw new BadRequestException("isBeschwerdeHaengig wurde bereits auf Wert gesetzt!");
        }
        gesuch.setBeschwerdeHaengig(createDto.getBeschwerdeSetTo());

        var entry = beschwerdeverlaufMapper.toEntity(createDto);
        entry.setGesuch(gesuch);
        beschwerdeverlaufRepository.persistAndFlush(entry);
        return beschwerdeverlaufMapper.toDto(entry);
    }

    @Transactional
    public BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntryIgnoreFlagValidation(
        final UUID gesuchId,
        final BeschwerdeVerlaufEntryCreateDto createDto
    ) {
        var gesuch = gesuchRepository.requireById(gesuchId);
        gesuch.setBeschwerdeHaengig(createDto.getBeschwerdeSetTo());

        var entry = beschwerdeverlaufMapper.toEntity(createDto);
        entry.setGesuch(gesuch);
        beschwerdeverlaufRepository.persistAndFlush(entry);
        return beschwerdeverlaufMapper.toDto(entry);
    }

}
