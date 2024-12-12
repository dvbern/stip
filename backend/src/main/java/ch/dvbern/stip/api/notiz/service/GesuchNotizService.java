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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class GesuchNotizService {
    private final GesuchRepository gesuchRepository;
    private final GesuchNotizRepository gesuchNotizRepository;
    private final GesuchNotizMapper gesuchNotizMapper;

    private List<GesuchNotizDto> getAllByFall(final Fall fall) {
        final var ausbildungs = fall.getAusbildungs();
        List<Gesuch> gesuchs = new ArrayList<>();
        ausbildungs.stream().map(Ausbildung::getGesuchs).forEach(gesuchs::addAll);
        final var notizes = new ArrayList<GesuchNotiz>();
        gesuchs.stream()
            .map(gesuch -> gesuchNotizRepository.findAllByGesuchId(gesuch.getId()))
            .forEach(notizes::addAll);
        return notizes.stream().map(gesuchNotizMapper::toDto).toList();
    }

    @Transactional
    public List<GesuchNotizDto> getAllByGesuchId(final UUID gesuchId) {
        try {
            final var gesuch = gesuchRepository.requireById(gesuchId);
            return getAllByFall(gesuch.getAusbildung().getFall());
        } catch (NotFoundException ex) {
            return List.of();
        }

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
    public GesuchNotizDto create(final Gesuch gesuch, final GesuchNotizCreateDto createDto) {
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

    @Transactional
    public GesuchNotizDto answerJuristischeNotiz(
        final JuristischeAbklaerungNotizAntwortDto dto,
        final UUID notizId
    ) {
        final var juristischeNotiz = gesuchNotizRepository.requireById(notizId);
        final var entity = gesuchNotizMapper.partialUpdate(dto, juristischeNotiz);
        return gesuchNotizMapper.toDto(entity);
    }
}
