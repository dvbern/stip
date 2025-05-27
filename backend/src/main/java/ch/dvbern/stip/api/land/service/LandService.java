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

package ch.dvbern.stip.api.land.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.generated.dto.LandDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class LandService {
    private final LandRepository landRepository;
    private final LandMapper landMapper;

    public List<LandDto> getAllLaender() {
        return landRepository
            .findAll()
            .stream()
            .map(landMapper::toDto)
            .toList();
    }

    @Transactional
    public LandDto createLand(final LandDto landDto) {
        // TODO KSTIP-1968: Implement
        return null;
    }

    @Transactional
    public LandDto updateLand(final UUID landId, final LandDto landDto) {
        // TODO KSTIP-1968: Implement
        return null;
    }

    public boolean landInEuEfta(Land land) {
        // TODO KSTIP-1968: Refactor this to be a simple query
        return true;
    }

    public Optional<Land> getLandByBfsCode(final String bfsCode) {
        return landRepository.getByBfsCode(bfsCode);
    }
}
