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

package ch.dvbern.stip.api.steuerdaten.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenDiffUtil;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class SteuerdatenMapper extends EntityUpdateMapper<SteuerdatenDto, Steuerdaten> {
    public abstract Steuerdaten toEntity(SteuerdatenDto steuerdatenDto);

    public abstract SteuerdatenDto toDto(Steuerdaten steuerdaten);

    public abstract Steuerdaten partialUpdate(
        SteuerdatenDto steuerdatenDto,
        @MappingTarget Steuerdaten steuerdaten
    );

    public Set<Steuerdaten> map(
        final List<SteuerdatenDto> steuerdatenDtos,
        final @MappingTarget Set<Steuerdaten> steuerdatenSet
    ) {
        if (steuerdatenDtos.isEmpty()) {
            steuerdatenSet.clear();
        }
        Iterator<Steuerdaten> iterator = steuerdatenSet.iterator();
        while (iterator.hasNext()) {
            Steuerdaten steuerdaten = iterator.next();
            if (
                steuerdatenDtos.stream()
                    .noneMatch(elternUpdateDto -> steuerdaten.getId().equals(elternUpdateDto.getId()))
            ) {
                iterator.remove();
            }
        }
        for (SteuerdatenDto steuerdatenDto : steuerdatenDtos) {
            if (steuerdatenDto.getId() != null) {
                Steuerdaten found = steuerdatenSet.stream()
                    .filter(steuerdaten -> steuerdaten.getId().equals(steuerdatenDto.getId()))
                    .findFirst()
                    .orElseThrow(
                        () -> new NotFoundException("Steuerdaten Not FOUND")
                    );
                steuerdatenSet.remove(found);
                steuerdatenSet.add(partialUpdate(steuerdatenDto, found));
            } else {
                steuerdatenSet.add(partialUpdate(steuerdatenDto, new Steuerdaten()));
            }
        }
        return steuerdatenSet;
    }

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(SteuerdatenDto source, @MappingTarget Steuerdaten target) {
        resetFieldIf(
            () -> SteuerdatenDiffUtil.hasArbeitsverhaeltnissChangedToUnselbstaendig(source, target),
            "Clear Saeulen because Arbeitsverhaeltniss is unselbstaendig",
            () -> {
                source.setSaeule2(null);
                source.setSaeule3a(null);
            }
        );
    }

    @AfterMapping
    protected void resetDependentDataAfterUpdate(
        final Steuerdaten steuerdaten
    ) {
        if (!steuerdaten.getIsArbeitsverhaeltnisSelbstaendig()) {
            steuerdaten.setSaeule2(null);
            steuerdaten.setSaeule3a(null);
        }
    }
}
