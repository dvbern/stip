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

package ch.dvbern.stip.api.kind.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.KindDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface KindMapper {
    Kind toEntity(KindDto kindDto);

    KindDto toDto(Kind kind);

    Kind partialUpdate(KindUpdateDto kindUpdateDto, @MappingTarget Kind kind);

    default Set<Kind> map(List<KindUpdateDto> kindUpdateDtos, @MappingTarget Set<Kind> kinder) {
        if (kindUpdateDtos.isEmpty()) {
            kinder.clear();
        }
        Iterator<Kind> iterator = kinder.iterator();
        while (iterator.hasNext()) {
            Kind kind = iterator.next();
            if (kindUpdateDtos.stream().noneMatch(kindUpdateDto -> kind.getId().equals(kindUpdateDto.getId()))) {
                iterator.remove();
            }
        }
        for (KindUpdateDto kindUpdateDto : kindUpdateDtos) {
            if (kindUpdateDto.getId() != null) {
                Kind found =
                    kinder.stream()
                        .filter(kind -> kind.getId().equals(kindUpdateDto.getId()))
                        .findFirst()
                        .orElseThrow(
                            () -> new NotFoundException("Kind Not FOUND")
                        );
                kinder.remove(found);
                kinder.add(partialUpdate(kindUpdateDto, found));
            } else {
                kinder.add(partialUpdate(kindUpdateDto, new Kind()));
            }
        }
        return kinder;
    }

    KindUpdateDto toUpdateDto(Kind kind);
}
