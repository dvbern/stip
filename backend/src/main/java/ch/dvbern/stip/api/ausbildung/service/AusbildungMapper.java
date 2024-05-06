package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.service.MonthYearToBeginOfMonth;
import ch.dvbern.stip.api.common.service.MonthYearToEndOfMonth;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = AusbildungsgangMapper.class)
public interface AusbildungMapper {
    @Mapping(source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class })
    @Mapping(source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class })
    Ausbildung toEntity(AusbildungDto ausbildungDto);

    @Mapping(source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    @Mapping(source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    AusbildungDto toDto(Ausbildung ausbildung);

    @Mapping(source = "ausbildungsgangId",
        target = "ausbildungsgang",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class })
    @Mapping(source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class })
    @Mapping(source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class })
    Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);
}
