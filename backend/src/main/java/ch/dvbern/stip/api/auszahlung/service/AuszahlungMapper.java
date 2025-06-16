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

package ch.dvbern.stip.api.auszahlung.service;

import java.util.Objects;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { ZahlungsverbindungMapper.class })
public abstract class AuszahlungMapper {
    public abstract Auszahlung toEntity(AuszahlungUpdateDto auszahlungUpdateDto);

    @Mapping(
        source = "auszahlung.auszahlungAnSozialdienst", target = "value.auszahlungAnSozialdienst",
        defaultValue = "false"
    )
    @Mapping(source = "auszahlung.zahlungsverbindung", target = "value.zahlungsverbindung")
    public abstract AuszahlungDto toDto(Fall fall);

    public abstract Auszahlung partialUpdate(
        AuszahlungUpdateDto auszahlungUpdateDto,
        @MappingTarget Auszahlung auszahlung
    );

    public abstract AuszahlungUpdateDto toUpdateDto(Auszahlung auszahlung);

    @AfterMapping
    protected void setIsDelegatedFlag(final Fall fall, @MappingTarget AuszahlungDto auszahlungDto) {
        auszahlungDto.setIsDelegated(Objects.nonNull(fall.getDelegierung()));
    }
}
