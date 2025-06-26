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

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LAND_ISO2CODE_NOT_UNIQUE;
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

        validateIso3CodeUniqueness(landDto.getIso3code(), null);
        validateIso2CodeUniqueness(landDto.getIso2code(), null);

        landRepository.persist(land);
        return landMapper.toDto(land);
    }

    @Transactional
    public LandDto updateLand(final UUID landId, final LandDto landDto) {
        final var land = landRepository.requireById(landId);
        landMapper.partialUpdate(landDto, land);

        validateIso2CodeUniqueness(landDto.getIso2code(), land.getId());
        validateIso3CodeUniqueness(landDto.getIso3code(), land.getId());

        return landMapper.toDto(land);
    }

    private void validateIso3CodeUniqueness(final String iso3code, final UUID excludeId) {
        if (iso3code != null) {
            final var duplicate = landRepository.getByIso3code(iso3code);
            if (duplicate.isPresent() && (excludeId == null || !duplicate.get().getId().equals(excludeId))) {
                throw new CustomValidationsException(
                    "iso3code must be unique or null",
                    new CustomConstraintViolation(
                        VALIDATION_LAND_ISO3CODE_NOT_UNIQUE,
                        "iso3code"
                    )
                );
            }
        }
    }

    private void validateIso2CodeUniqueness(final String iso2code, final UUID excludeId) {
        if (iso2code != null) {
            final var duplicate = landRepository.getByIso2code(iso2code);
            if (duplicate.isPresent() && (excludeId == null || !duplicate.get().getId().equals(excludeId))) {
                throw new CustomValidationsException(
                    "iso2code must be unique or null",
                    new CustomConstraintViolation(
                        VALIDATION_LAND_ISO2CODE_NOT_UNIQUE,
                        "iso2code"
                    )
                );
            }
        }
    }

    public Optional<Land> getLandByBfsCode(final String bfsCode) {
        return landRepository.getByBfsCode(bfsCode);
    }

    public Land requireLandById(final UUID id) {
        return landRepository.requireById(id);
    }
}
