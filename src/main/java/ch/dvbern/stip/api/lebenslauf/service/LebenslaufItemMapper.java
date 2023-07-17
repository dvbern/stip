package ch.dvbern.stip.api.lebenslauf.service;

import ch.dvbern.stip.api.common.service.*;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.generated.dto.LebenslaufItemDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import jakarta.ws.rs.NotFoundException;
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default Set<LebenslaufItem> map(List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDtos, @MappingTarget Set<LebenslaufItem> lebenslaufItemSet) {
        for (LebenslaufItemUpdateDto lebenslaufItemUpdateDto : lebenslaufItemUpdateDtos) {
            if (lebenslaufItemUpdateDto.getId() != null) {
                LebenslaufItem found = lebenslaufItemSet.stream().filter(lebenslaufItem -> lebenslaufItem.getId().equals(lebenslaufItemUpdateDto.getId())).findFirst().orElseThrow(
                        () -> new NotFoundException("LebenslaufItem Not FOUND")
                );
                lebenslaufItemSet.remove(found);
                lebenslaufItemSet.add(partialUpdate(lebenslaufItemUpdateDto, found));
            }
            else {
                lebenslaufItemSet.add(partialUpdate(lebenslaufItemUpdateDto, new LebenslaufItem()));
            }
        }
        return lebenslaufItemSet;
    }
}