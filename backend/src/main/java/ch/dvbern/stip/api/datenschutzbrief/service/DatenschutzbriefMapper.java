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

package ch.dvbern.stip.api.datenschutzbrief.service;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.generated.dto.DatenschutzbriefOverviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public interface DatenschutzbriefMapper {
    @Mapping(source = ".", target = "massendruckJobId", qualifiedByName = "getRelatedMassendruckJobId")
    @Mapping(source = "datenschutzbriefEmpfaenger", target = "elternTyp")
    DatenschutzbriefOverviewDto toDto(Datenschutzbrief datenschutzbrief);

    @Named("getRelatedMassendruckJobId")
    default UUID getRelatedMassendruckJobId(Datenschutzbrief datenschutzbrief) {
        final var datenschutzbriefMassendruck = datenschutzbrief.getDatenschutzbriefMassendruck();
        if (Objects.isNull(datenschutzbriefMassendruck)) {
            return null;
        }

        return datenschutzbriefMassendruck.getMassendruckJob().getId();
    }
}
