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

package ch.dvbern.stip.api.ausbildung.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteService {

    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsstaetteMapper ausbildungsstaetteMapper;

    public AusbildungsstaetteDto findById(UUID ausbildungsstetteId) {
        var ausbildungsstaette = ausbildungsstaetteRepository.requireById(ausbildungsstetteId);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public List<AusbildungsstaetteDto> getAusbildungsstaetten() {
        return ausbildungsstaetteRepository.findAll()
            .stream()
            .map(ausbildungsstaetteMapper::toDto)
            .toList();
    }

    @Transactional
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaetteDto) {
        Ausbildungsstaette ausbildungsstaette = persistAusbildungsstaette(ausbildungsstaetteDto);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public AusbildungsstaetteDto updateAusbildungsstaette(
        UUID ausbildungsstaetteId,
        AusbildungsstaetteUpdateDto ausbildungsstaetteUpdateDto
    ) {
        Ausbildungsstaette ausbildungsstaetteToUpdate = ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
        persistAusbildungsstaette(ausbildungsstaetteUpdateDto, ausbildungsstaetteToUpdate);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaetteToUpdate);
    }

    @Transactional
    public void deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        Ausbildungsstaette ausbildungsstaette = ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
        ausbildungsstaetteRepository.delete(ausbildungsstaette);
    }

    private void persistAusbildungsstaette(
        AusbildungsstaetteUpdateDto ausbildungsstaetteUpdate,
        Ausbildungsstaette ausbildungsstaetteToUpdate
    ) {
        ausbildungsstaetteMapper.partialUpdate(ausbildungsstaetteUpdate, ausbildungsstaetteToUpdate);
        ausbildungsstaetteRepository.persist(ausbildungsstaetteToUpdate);
    }

    private Ausbildungsstaette persistAusbildungsstaette(
        AusbildungsstaetteCreateDto ausbildungsstaetteCreate
    ) {
        Ausbildungsstaette ausbildungsstaette = ausbildungsstaetteMapper.toEntity(ausbildungsstaetteCreate);
        ausbildungsstaetteRepository.persist(ausbildungsstaette);
        return ausbildungsstaette;
    }

}
