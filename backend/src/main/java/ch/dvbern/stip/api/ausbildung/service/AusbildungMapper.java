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
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.service.MonthYearToBeginOfMonth;
import ch.dvbern.stip.api.common.service.MonthYearToEndOfMonth;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static ch.dvbern.stip.api.ausbildung.entity.AusbildungBesuchtBMSValidationConstraintValidator.VALID_BFS_VALUES_FOR_BMS_FLAG;

@Mapper(
    config = MappingConfig.class,
    uses = { FallMapper.class, AusbildungsgangMapper.class, BildungskategorieMapper.class }
)
public abstract class AusbildungMapper extends EntityUpdateMapper<AusbildungUpdateDto, Ausbildung> {
    @Inject
    AusbildungAuthorizer ausbildungAuthorizer;

    @Inject
    Validator validator;

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
    @Mapping(source = "fall.id", target = "fallId")
    public abstract AusbildungDto toDto(Ausbildung ausbildung);

    @AfterMapping
    protected void setAusbildungEditable(
        @MappingTarget final AusbildungDto dto
    ) {
        dto.setEditable(ausbildungAuthorizer.canUpdateCheck(dto.getId()));
    }

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

    @AfterMapping
    public void resetAusbildungsgangIfNull(
        @MappingTarget Ausbildung ausbildung
    ) {
        if (ausbildung.getAusbildungsgang().getId() == null) {
            ausbildung.setAusbildungsgang(null);
        }
    }

    @BeforeMapping
    protected void resetDataIfNotValid(
        final AusbildungUpdateDto newAusbildung,
        @MappingTarget final Ausbildung ausbildung
    ) {
        // reset field if ausbildungsgang has changed and bfs of ausbildung is not in VALID_BFS_VALUES_FOR_BMS_FLAG
        resetFieldIf(
            () -> newAusbildung != null
            && ausbildung.getAusbildungsgang() != null
            && (AusbildungDiffUtil.hasAusbildungsgangChanged(ausbildung, newAusbildung))
            && (!VALID_BFS_VALUES_FOR_BMS_FLAG
                .contains(ausbildung.getAusbildungsgang().getBildungskategorie().getBfs())),
            "Reset BMS-Flag if it has changed and Ausbildung is not valid",
            () -> {
                // reset invalid data
                newAusbildung.setBesuchtBMS(false);
            }
        );
    }

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
