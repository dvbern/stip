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

package ch.dvbern.stip.api.notiz.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class GesuchNotizService {
    private final GesuchNotizRepository gesuchNotizRepository;
    private final GesuchNotizMapper gesuchNotizMapper;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public List<GesuchNotizDto> getAllByGesuchId(final UUID gesuchId) {
        return gesuchNotizRepository.findAllByGesuchId(gesuchId).stream().map(gesuchNotizMapper::toDto).toList();
    }

    @Transactional
    public GesuchNotizDto getById(final UUID notizId) {
        final var notiz = gesuchNotizRepository.requireById(notizId);
        return gesuchNotizMapper.toDto(notiz);
    }

    @Transactional
    public void delete(final UUID gesuchNotizId) {
        final var notiz = gesuchNotizRepository.requireById(gesuchNotizId);
        gesuchNotizRepository.delete(notiz);
    }

    @Transactional
    public void deleteAllByGesuchId(final UUID gesuchId) {
        gesuchNotizRepository.findAllByGesuchId(gesuchId).forEach(gesuchNotizRepository::delete);
    }

    @Transactional
    public GesuchNotizDto create(final GesuchNotizCreateDto createDto) {
        final var gesuch = gesuchRepository.requireById(createDto.getGesuchId());
        final var notiz = gesuchNotizMapper.toEntity(createDto);
        notiz.setGesuch(gesuch);
        gesuchNotizRepository.persistAndFlush(notiz);
        return gesuchNotizMapper.toDto(notiz);
    }

    @Transactional
    public GesuchNotizDto update(final GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        var gesuchNotiz = gesuchNotizRepository.requireById(gesuchNotizUpdateDto.getId());
        gesuchNotizMapper.partialUpdate(gesuchNotizUpdateDto, gesuchNotiz);
        return gesuchNotizMapper.toDto(gesuchNotiz);
    }
}
