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

package ch.dvbern.stip.api.adresse.service;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.generated.dto.AdresseDto;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class AdresseMapper {
    @Inject
    LandService landService;

    @Mapping(source = "landId", target = "land", qualifiedByName = "mapLand")
    public abstract Adresse toEntity(AdresseDto adresseDto);

    @Mapping(source = "land.id", target = "landId")
    public abstract AdresseDto toDto(Adresse adresse);

    @Mapping(source = "landId", target = "land", qualifiedByName = "mapLand")
    public abstract Adresse partialUpdate(AdresseDto adresseDto, @MappingTarget Adresse adresse);

    @Named("mapLand")
    protected Land mapLand(UUID landId) {
        if (landId == null) {
            return null;
        }

        return landService.requireLandById(landId);
    }
}
