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

package ch.dvbern.stip.api.eltern.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.generated.dto.ElternDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface ElternMapper {
    Eltern toEntity(ElternDto elternDto);

    ElternDto toDto(Eltern eltern);

    Eltern partialUpdate(ElternUpdateDto elternUpdateDto, @MappingTarget Eltern eltern);

    default Set<Eltern> map(List<ElternUpdateDto> elternUpdateDtos, @MappingTarget Set<Eltern> elternSet) {
        if (elternUpdateDtos.isEmpty()) {
            elternSet.clear();
        }
        Iterator<Eltern> iterator = elternSet.iterator();
        while (iterator.hasNext()) {
            Eltern eltern = iterator.next();
            if (
                elternUpdateDtos.stream()
                    .noneMatch(elternUpdateDto -> eltern.getId().equals(elternUpdateDto.getId()))
            ) {
                iterator.remove();
            }
        }
        for (ElternUpdateDto elternUpdateDto : elternUpdateDtos) {
            if (elternUpdateDto.getId() != null) {
                Eltern found = elternSet.stream()
                    .filter(eltern -> eltern.getId().equals(elternUpdateDto.getId()))
                    .findFirst()
                    .orElseThrow(
                        () -> new NotFoundException("Eltern Not FOUND")
                    );
                elternSet.remove(found);
                elternSet.add(partialUpdate(elternUpdateDto, found));
            } else {
                elternSet.add(partialUpdate(elternUpdateDto, new Eltern()));
            }
        }
        return elternSet;
    }

    ElternUpdateDto toUpdateDto(Eltern eltern);
}
