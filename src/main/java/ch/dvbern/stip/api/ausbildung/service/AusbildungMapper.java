package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.service.*;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class)
public interface AusbildungMapper {
    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(source = "ausbildungsstaetteId", target = "ausbildungsstaette.id")
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {DateMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy = {DateMapper.class, MonthYearToEndOfMonth.class})
    Ausbildung toEntity(AusbildungDto ausbildungDto);

    @Mapping(source = "ausbildungsgang.id", target = "ausbildungsgangId")
    @Mapping(source = "ausbildungsstaette.id", target = "ausbildungsstaetteId")
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {DateMapper.class, DateToMonthYear.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy = {DateMapper.class, DateToMonthYear.class})
    AusbildungDto toDto(Ausbildung ausbildung);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang", qualifiedBy = {EntityReferenceMapper.class, EntityIdReference.class})
    @Mapping(source = "ausbildungsstaetteId", target = "ausbildungsstaette", qualifiedBy = {EntityReferenceMapper.class, EntityIdReference.class})
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {DateMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy = {DateMapper.class, MonthYearToEndOfMonth.class})
    Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);
}