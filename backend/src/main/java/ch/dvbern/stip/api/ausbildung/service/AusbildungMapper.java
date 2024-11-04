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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.util.AusbildungDiffUtil;
import ch.dvbern.stip.api.bildungskategorie.service.BildungskategorieMapper;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.service.MonthYearToBeginOfMonth;
import ch.dvbern.stip.api.common.service.MonthYearToEndOfMonth;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    config = MappingConfig.class,
    uses = { FallMapper.class, AusbildungsgangMapper.class, BildungskategorieMapper.class }
)
public abstract class AusbildungMapper extends EntityUpdateMapper<AusbildungUpdateDto, Ausbildung> {
    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    public abstract AusbildungDto toDto(Ausbildung ausbildung);

    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
    )
    @Mapping(source = "fallId", target = "fall.id")
    public abstract Ausbildung toEntity(AusbildungDto ausbildungDto);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
    )
    @Mapping(source = "fallId", target = "fall.id")
    public abstract Ausbildung toNewEntity(AusbildungUpdateDto ausbildungDto);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
    )
    @Mapping(source = "fallId", target = "fall.id")
    public abstract Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final AusbildungUpdateDto newFormular,
        @MappingTarget final Ausbildung targetFormular
    ) {
        resetFieldIf(
            () -> AusbildungDiffUtil.hasIsAusbildungAuslandChanged(targetFormular, newFormular),
            "Clear Ausbildungsort because IsAusbildungAusland has changed",
            () -> {
                if (newFormular != null && Boolean.TRUE.equals(newFormular.getIsAusbildungAusland())) {
                    newFormular.setAusbildungsort(null);
                }
            }
        );
    }

    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    public abstract AusbildungUpdateDto toUpdateDto(Ausbildung ausbildung);
}
