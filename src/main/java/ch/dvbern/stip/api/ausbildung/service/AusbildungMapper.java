package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.common.service.*;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.*;

@Mapper(config = MappingConfig.class, uses = DateUtil.class)
public interface AusbildungMapper {
    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(source = "ausbildungsstaetteId", target = "ausbildungsstaette.id")
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {MonthYearMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy =  {MonthYearMapper.class, MonthYearToEndOfMonth.class})
    Ausbildung toEntity(AusbildungDto ausbildungDto);

    @Mapping(source = "ausbildungsgang.id", target = "ausbildungsgangId")
    @Mapping(source = "ausbildungsstaette.id", target = "ausbildungsstaetteId")
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {MonthYearMapper.class, DateToMonthYear.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy = {MonthYearMapper.class, DateToMonthYear.class})
    AusbildungDto toDto(Ausbildung ausbildung);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(source = "ausbildungsstaetteId", target = "ausbildungsstaette.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "ausbildungBegin", target = "ausbildungBegin", qualifiedBy = {MonthYearMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "ausbildungEnd", target = "ausbildungEnd", qualifiedBy = {MonthYearMapper.class, MonthYearToEndOfMonth.class})
    Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);
}