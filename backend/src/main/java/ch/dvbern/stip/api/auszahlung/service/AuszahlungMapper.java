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

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.zahlungsverbindung.service.ZahlungsverbindungMapper;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.FallAuszahlungDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { ZahlungsverbindungMapper.class })
public abstract class AuszahlungMapper {
    public abstract Auszahlung toEntity(AuszahlungUpdateDto auszahlungUpdateDto);

    @Mapping(
        source = "auszahlung.auszahlungAnSozialdienst", target = "auszahlung.auszahlungAnSozialdienst",
        defaultValue = "false"
    )
    @Mapping(source = "auszahlung.zahlungsverbindung", target = "auszahlung.zahlungsverbindung")
    public abstract FallAuszahlungDto toDto(Fall fall);

    public abstract Auszahlung partialUpdate(
        AuszahlungUpdateDto auszahlungUpdateDto,
        @MappingTarget Auszahlung auszahlung
    );

    public abstract FallAuszahlungDto toUpdateDto(Auszahlung auszahlung);

    @AfterMapping
    protected void setIsDelegatedFlag(final Fall fall, @MappingTarget FallAuszahlungDto auszahlungDto) {
        auszahlungDto.setIsDelegated(fall.isDelegiert());
    }
}
