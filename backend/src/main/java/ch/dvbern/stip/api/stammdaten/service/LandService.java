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

package ch.dvbern.stip.api.stammdaten.service;

import java.util.Arrays;
import java.util.List;

import ch.dvbern.stip.api.stammdaten.repo.LandEuEftaRepository;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.LandEuEftaDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class LandService {
    private final LandEuEftaRepository landEuEftaRepository;
    private final LandEuEftaMapper landEuEftaMapper;

    public List<Land> getAllLaender() {
        return Arrays.stream(Land.values()).toList();
    }

    public boolean landInEuEfta(Land land) {
        // TODO KSTIP-1968: Refactor this to be a simple query
        return getAllLandEuEfta()
            .stream()
            .filter(LandEuEftaDto::getIsEuEfta)
            .map(LandEuEftaDto::getLand)
            .toList()
            .contains(land);
    }

    @Transactional
    public List<LandEuEftaDto> getAllLandEuEfta() {
        return landEuEftaRepository
            .findAll()
            .stream()
            .map(landEuEftaMapper::toDto)
            .toList();
    }

    @Transactional
    public List<LandEuEftaDto> setLaenderEuEfta(List<LandEuEftaDto> landEuEftaDto) {
        for (LandEuEftaDto dto : landEuEftaDto) {
            landEuEftaRepository.setLandEuEfta(dto.getLand(), dto.getIsEuEfta());
        }

        return getAllLandEuEfta();
    }
}
