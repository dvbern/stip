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

import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.notiz.entity.JuristischeAbklaerungNotiz;
import ch.dvbern.stip.api.notiz.repo.GesuchNotizRepository;
import ch.dvbern.stip.api.notiz.repo.JuristischeNotizRepository;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizTypDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestScoped
public class GesuchNotizService {
    private final GesuchNotizRepository gesuchNotizRepository;
    private final GesuchNotizMapper gesuchNotizMapper;
    private final JuristischeNotizMapper juristischeNotizMapper;
    private final GesuchRepository gesuchRepository;
    private final JuristischeNotizRepository juristischeNotizRepository;

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

    // todo: distinguish?
    @Transactional
    public GesuchNotizDto create(final GesuchNotizCreateDto createDto) {
        final var gesuch = gesuchRepository.requireById(createDto.getGesuchId());
        if (createDto.getNotizTyp().equals(GesuchNotizTypDto.JURISTISCHE_NOTIZ)) {
            final var juristischeNotiz = juristischeNotizMapper.toEntity(createDto);
            juristischeNotiz.setGesuch(gesuch);
            gesuchNotizRepository.persistAndFlush(juristischeNotiz);

            // convert
            final var notiz = (GesuchNotiz) juristischeNotiz;
            return gesuchNotizMapper.toDto(notiz);

        } else {
            final var notiz = gesuchNotizMapper.toEntity(createDto);
            notiz.setGesuch(gesuch);
            gesuchNotizRepository.persistAndFlush(notiz);
            return gesuchNotizMapper.toDto(notiz);
        }

    }

    @Transactional
    public GesuchNotizDto update(final GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        var gesuchNotiz = gesuchNotizRepository.requireById(gesuchNotizUpdateDto.getId());
        gesuchNotizMapper.partialUpdate(gesuchNotizUpdateDto, gesuchNotiz);
        return gesuchNotizMapper.toDto(gesuchNotiz);
    }

    @Transactional
    public JuristischeAbklaerungNotizDto getJuristischeNotizById(final UUID notizId) {
        final var juristischeNotiz = juristischeNotizRepository.requireById(notizId);
        return juristischeNotizMapper.toDto(juristischeNotiz);
    }

    @Transactional
    public List<JuristischeAbklaerungNotizDto> getAllJuristischeNotizen(final UUID gesuchId) {
        // todo: refactor - cause: mapstruct illegall access exception
        ArrayList<JuristischeAbklaerungNotizDto> list = new ArrayList<>();
        final var results = juristischeNotizRepository.findAllByGesuchId(gesuchId);
        for (JuristischeAbklaerungNotiz result : results) {
            list.add(juristischeNotizMapper.toDto(result));
        }
        return list;
        /*
         * return juristischeNotizRepository.findAllByGesuchId(gesuchId)
         * .stream()
         * .map(juristischeNotizMapper::toDto)
         * .toList();
         *
         */
    }

}
