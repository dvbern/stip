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

import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.repo.LandRepository;
import ch.dvbern.stip.generated.dto.LandDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LAND_ISO3CODE_NOT_UNIQUE;

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
        if (landDto.getId() != null) {
            throw new BadRequestException("Cannot create Land that already has an ID");
        }

        final var land = landMapper.toEntity(landDto);

        if (landDto.getIso3code() != null) {
            var duplicate = landRepository.getByIso3code(land.getIso3code());
            if (duplicate.isPresent()) {
                throw new CustomValidationsException(
                    "iso3code must be unique or null",
                    new CustomConstraintViolation(
                        VALIDATION_LAND_ISO3CODE_NOT_UNIQUE,
                        "iso3code"
                    )
                );
            }
        }

        landRepository.persist(land);
        return landMapper.toDto(land);
    }

    @Transactional
    public LandDto updateLand(final UUID landId, final LandDto landDto) {
        final var land = landRepository.requireById(landId);
        landMapper.partialUpdate(landDto, land);

        if (landDto.getIso3code() != null) {
            var duplicate = landRepository.getByIso3code(land.getIso3code());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(land.getId())) {
                throw new CustomValidationsException(
                    "iso3code must be unique or null",
                    new CustomConstraintViolation(
                        VALIDATION_LAND_ISO3CODE_NOT_UNIQUE,
                        "iso3code"
                    )
                );
            }
        }

        return landMapper.toDto(land);
    }

    public Optional<Land> getLandByBfsCode(final String bfsCode) {
        return landRepository.getByBfsCode(bfsCode);
    }

    public Land requireLandById(final UUID id) {
        return landRepository.requireById(id);
    }
}
