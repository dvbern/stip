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

package ch.dvbern.stip.api.lebenslauf.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.service.MonthYearToBeginOfMonth;
import ch.dvbern.stip.api.common.service.MonthYearToEndOfMonth;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.generated.dto.LebenslaufItemDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface LebenslaufItemMapper {

    @Mapping(source = "von", target = "von", qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class })
    @Mapping(source = "bis", target = "bis", qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class })
    LebenslaufItem toEntity(LebenslaufItemDto lebenslaufItemDto);

    @Mapping(source = "von", target = "von", qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    @Mapping(source = "bis", target = "bis", qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    LebenslaufItemDto toDto(LebenslaufItem lebenslaufItem);

    @Mapping(source = "von", target = "von", qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class })
    @Mapping(source = "bis", target = "bis", qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class })
    LebenslaufItem partialUpdate(
        LebenslaufItemUpdateDto lebenslaufItemUpdateDto,
        @MappingTarget LebenslaufItem lebenslaufItem
    );

    default Set<LebenslaufItem> map(
        List<LebenslaufItemUpdateDto> lebenslaufItemUpdateDtos,
        @MappingTarget Set<LebenslaufItem> lebenslaufItemSet
    ) {
        if (lebenslaufItemUpdateDtos.isEmpty()) {
            lebenslaufItemSet.clear();
        }
        Iterator<LebenslaufItem> iterator = lebenslaufItemSet.iterator();
        while (iterator.hasNext()) {
            LebenslaufItem lebenslaufItem = iterator.next();
            if (
                lebenslaufItemUpdateDtos.stream()
                    .noneMatch(
                        lebenslaufItemUpdateDto -> lebenslaufItem.getId().equals(lebenslaufItemUpdateDto.getId())
                    )
            ) {
                iterator.remove();
            }
        }
        for (LebenslaufItemUpdateDto lebenslaufItemUpdateDto : lebenslaufItemUpdateDtos) {
            if (lebenslaufItemUpdateDto.getId() != null) {
                LebenslaufItem found = lebenslaufItemSet.stream()
                    .filter(lebenslaufItem -> lebenslaufItem.getId().equals(lebenslaufItemUpdateDto.getId()))
                    .findFirst()
                    .orElseThrow(
                        () -> new NotFoundException("LebenslaufItem Not FOUND")
                    );
                lebenslaufItemSet.remove(found);
                lebenslaufItemSet.add(partialUpdate(lebenslaufItemUpdateDto, found));
            } else {
                lebenslaufItemSet.add(partialUpdate(lebenslaufItemUpdateDto, new LebenslaufItem()));
            }
        }
        return lebenslaufItemSet;
    }

    @Mapping(source = "von", target = "von", qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    @Mapping(source = "bis", target = "bis", qualifiedBy = { DateMapper.class, DateToMonthYear.class })
    LebenslaufItemUpdateDto toUpdateDto(LebenslaufItem lebenslaufItem);
}
