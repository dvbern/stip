package ch.dvbern.stip.api.lebenslauf.service;

import ch.dvbern.stip.api.common.service.*;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.generated.dto.LebenslaufItemDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(config = MappingConfig.class, uses=
        DateUtil.class
)
public interface LebenslaufItemMapper {

    @Mapping(source = "von", target = "von", qualifiedBy = {MonthYearMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "bis", target = "bis", qualifiedBy =  {MonthYearMapper.class,MonthYearToEndOfMonth.class})
    LebenslaufItem toEntity(LebenslaufItemDto lebenslaufItemDto);

    @Mapping(source = "von", target = "von", qualifiedBy = {MonthYearMapper.class, DateToMonthYear.class})
    @Mapping(source = "bis", target = "bis", qualifiedBy = {MonthYearMapper.class, DateToMonthYear.class})
    LebenslaufItemDto toDto(LebenslaufItem lebenslaufItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "von", target = "von", qualifiedBy = {MonthYearMapper.class, MonthYearToBeginOfMonth.class})
    @Mapping(source = "bis", target = "bis", qualifiedBy = {MonthYearMapper.class, MonthYearToEndOfMonth.class})
    LebenslaufItem partialUpdate(LebenslaufItemUpdateDto lebenslaufItemUpdateDto, @MappingTarget LebenslaufItem lebenslaufItem);

    Set<LebenslaufItem> map(List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDto);
}