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

package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.generated.dto.EinnahmenKostenDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class EinnahmenKostenMapper {
    public abstract EinnahmenKosten toEntity(EinnahmenKostenDto einnahmenKostenDto);

    public abstract EinnahmenKostenDto toDto(EinnahmenKosten einnahmenKosten);

    public abstract EinnahmenKosten partialUpdate(
        EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        @MappingTarget EinnahmenKosten einnahmenKosten
    );

    public abstract EinnahmenKostenUpdateDto toUpdateDto(EinnahmenKosten einnahmenKosten);

    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        @MappingTarget EinnahmenKosten einnahmenKosten
    ) {
        if (einnahmenKostenUpdateDto.getWgWohnend()) {
            einnahmenKostenUpdateDto.setAlternativeWohnformWohnend(null);
        } else {
            einnahmenKostenUpdateDto.setWgAnzahlPersonen(null);
        }
    }
}
