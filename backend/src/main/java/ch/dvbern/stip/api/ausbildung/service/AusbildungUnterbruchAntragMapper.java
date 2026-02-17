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

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.util.AusbildungUnterbruchAntragUtil;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
    config = MappingConfig.class
)
public abstract class AusbildungUnterbruchAntragMapper {
    abstract AusbildungUnterbruchAntragGSDto toGsDto(AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag);

    @Mapping(source = "gueltigkeit.gueltigAb", target = "startDate")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "endDate")
    @Mapping(source = ".", target = "canAntragAkzeptieren", qualifiedByName = "getCanAntragAkzeptieren")
    abstract AusbildungUnterbruchAntragSBDto toSbDto(AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag);

    @Named("getCanAntragAkzeptieren")
    protected boolean getCanAntragAkzeptieren(final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag) {
        return AusbildungUnterbruchAntragUtil.canAusbildungUnterbruchAntragAkzeptieren(ausbildungUnterbruchAntrag);
    }

    @Mapping(source = "startDate", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "endDate", target = "gueltigkeit.gueltigBis")
    public abstract AusbildungUnterbruchAntrag partialUpdate(
        UpdateAusbildungUnterbruchAntragGSDto updateAusbildungUnterbruchAntragGSDto,
        @MappingTarget AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag
    );

    @Mapping(source = "startDate", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "endDate", target = "gueltigkeit.gueltigBis")
    public abstract AusbildungUnterbruchAntrag partialUpdate(
        UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragGSDto,
        @MappingTarget AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag
    );
}
