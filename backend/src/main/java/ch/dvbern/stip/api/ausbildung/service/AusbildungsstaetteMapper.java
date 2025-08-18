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

import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteSlimDto;
import ch.dvbern.stip.generated.dto.RenameAusbildungsstaetteDto;
import jakarta.ws.rs.BadRequestException;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
    config = MappingConfig.class,
    uses = AusbildungsgangMapper.class
)
public abstract class AusbildungsstaetteMapper {
    abstract Ausbildungsstaette toEntity(AusbildungsstaetteCreateDto ausbildungsstaetteDto);

    @BeforeMapping
    protected void ensureOnlyOneNoIsSet(AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto) {
        final boolean ctIsNull = Objects.isNull(ausbildungsstaetteCreateDto.getCtNo());
        final boolean burIsNull = Objects.isNull(ausbildungsstaetteCreateDto.getBurNo());

        // !XOR
        if (ctIsNull == burIsNull) {
            throw new BadRequestException("(Only) One of CtNo and BurNo must be set");
        }
    }

    abstract AusbildungsstaetteDto toDto(Ausbildungsstaette ausbildungsstaette);

    abstract AusbildungsstaetteSlimDto toSlimDto(Ausbildungsstaette ausbildungsstaette);

    abstract void partialUpdate(
        RenameAusbildungsstaetteDto renameAbschlussDto,
        @MappingTarget Ausbildungsstaette abschluss
    );
}
