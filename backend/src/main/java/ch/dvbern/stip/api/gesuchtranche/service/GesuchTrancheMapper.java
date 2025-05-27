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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    config = MappingConfig.class,
    uses = GesuchFormularMapper.class
)
public interface GesuchTrancheMapper {

    @Mapping(source = "gueltigAb", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "gueltigBis", target = "gueltigkeit.gueltigBis")
    GesuchTranche toEntity(GesuchTrancheDto gesuchTrancheDto);

    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    GesuchTrancheDto toDto(GesuchTranche gesuchTranche);

    @Named("toSlimDto")
    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche);

    @Mapping(target = "tranchen", qualifiedByName = "toSlimDto")
    @Mapping(target = "initialTranchen", qualifiedByName = "toSlimDto")
    GesuchTrancheListDto toListDto(List<GesuchTranche> tranchen, List<GesuchTranche> initialTranchen);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GesuchTranche partialUpdate(GesuchTrancheUpdateDto gesuchUpdateDto, @MappingTarget GesuchTranche gesuch);
}
