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

import java.time.LocalDate;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.util.AusbildungUnterbruchAntragUtil;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
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
    @Mapping(source = "gesuch", target = "unterbruchLatestEndDate", qualifiedByName = "getUnterbruchLatestEndDate")
    @Mapping(
        source = "gesuch", target = "unterbruchEarliestStartDate", qualifiedByName = "getUnterbruchEarliestStartDate"
    )
    abstract AusbildungUnterbruchAntragSBDto toSbDto(final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag);

    @Named("getUnterbruchLatestEndDate")
    protected LocalDate getUnterbruchLatestEndDate(final Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigBis();
    }

    @Named("getUnterbruchEarliestStartDate")
    protected LocalDate getUnterbruchEarliestStartDate(final Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigAb();
    }

    @Named("getCanAntragAkzeptieren")
    protected boolean getCanAntragAkzeptieren(final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag) {
        return AusbildungUnterbruchAntragUtil.canAusbildungUnterbruchAntragAkzeptieren(ausbildungUnterbruchAntrag);
    }

    @Mapping(source = "startDate", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "endDate", target = "gueltigkeit.gueltigBis")
    public abstract AusbildungUnterbruchAntrag partialUpdate(
        final UpdateAusbildungUnterbruchAntragGSDto updateAusbildungUnterbruchAntragGSDto,
        @MappingTarget final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag
    );

    @Mapping(source = "startDate", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "endDate", target = "gueltigkeit.gueltigBis")
    public abstract AusbildungUnterbruchAntrag partialUpdate(
        final UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragGSDto,
        @MappingTarget final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag
    );
}
