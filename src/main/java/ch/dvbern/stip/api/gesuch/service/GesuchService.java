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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchService {

	private final GesuchRepository gesuchRepository;

	private final GesuchMapper gesuchMapper;

	public Optional<GesuchDto> findGesuch(UUID id) {
		return gesuchRepository.findByIdOptional(id).map(gesuchMapper::toDto);
	}

	@Transactional
	public void updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
		var gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
		gesuchMapper.partialUpdate(gesuchUpdateDto, gesuch);
		gesuchRepository.getEntityManager().merge(gesuch);
	}

	public List<GesuchDto> findAll() {
		return gesuchRepository.findAll().stream().map(gesuchMapper::toDto).toList();
	}

	@Transactional
	public GesuchDto createGesuch(GesuchCreateDto gesuchCreateDto) {
		Gesuch gesuch = gesuchMapper.toNewEntity(gesuchCreateDto);
		gesuchRepository.persist(gesuch);
		return gesuchMapper.toDto(gesuch);
	}

	public List<GesuchDto> findAllForBenutzer(UUID benutzerId) {
		return gesuchRepository.findAllForBenutzer(benutzerId).map(gesuchMapper::toDto).toList();
	}

	public List<GesuchDto> findAllForFall(UUID fallId) {
		return gesuchRepository.findAllForFall(fallId).map(gesuchMapper::toDto).toList();
	}

	@Transactional
	public void deleteGesuch(UUID gesuchId) {
		Gesuch gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
		gesuchRepository.delete(gesuch);
	}
}
