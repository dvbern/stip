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

package ch.dvbern.stip.gesuch.service;

import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.gesuch.entity.Gesuch;
import ch.dvbern.stip.gesuch.repo.GesuchRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
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
    public Gesuch saveGesuch(GesuchUpdateDto gesuchDTO) {
        Gesuch gesuch = gesuchMapper.toUpdateEntity(gesuchDTO);
        gesuchRepository.persist(gesuch);
        return gesuch;
    }

    /*private void handleAusbildungStammdaten(Gesuch gesuch, GesuchDto gesuchDTO) {
        if (gesuch.getAusbildungContainer() != null && gesuch.getAusbildungContainer().getAusbildungSB() != null) {
            if (gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungsgang() == null ||
            !gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungsgang().getId().equals(
                    gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungsgangId())) {
                gesuch.getAusbildungContainer().getAusbildungSB().setAusbildungsgang(
                        ausbildungService.findAusbildungsgangByID(
                                        gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungsgangId())
                                .orElseThrow(() -> new RuntimeException("Ausbildungsgang nicht gefunden")));
            }
            if (gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungstaette() == null ||
                    !gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungstaette().getId().equals(
                            gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungstaetteId())) {
                gesuch.getAusbildungContainer().getAusbildungSB().setAusbildungstaette(
                        ausbildungService.findAusbildungstaetteByID(
                                        gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungstaetteId())
                                .orElseThrow(() -> new RuntimeException("Ausbildungstaette nicht gefunden")));
            }
        }
    }*/


    public List<GesuchDto> findAll() {
       return gesuchRepository.findAll().stream().map(gesuchMapper::toDto).toList();
    }

    public Gesuch createGesuch(GesuchCreateDto gesuchCreateDto) {
        Gesuch gesuch = gesuchMapper.toNewEntity(gesuchCreateDto);
        gesuchRepository.persist(gesuch);
        return gesuch;
    }
}
