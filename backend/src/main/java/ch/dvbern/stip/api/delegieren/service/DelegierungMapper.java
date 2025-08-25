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

package ch.dvbern.stip.api.delegieren.service;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.DelegierungDto;
import ch.dvbern.stip.generated.dto.FallWithDelegierungDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = AdresseMapper.class)

public abstract class DelegierungMapper {
    public abstract Delegierung toEntity(final DelegierungDto delegierungDto);

    @Mapping(source = ".", target = "delegierungAngenommen", qualifiedByName = "delegierungAngenommen")
    public abstract DelegierungDto toDto(final Delegierung delegierung);

    public abstract FallWithDelegierungDto toFallWithDto(Fall fall);

    @Named("delegierungAngenommen")
    boolean delegierungAngenommen(Delegierung delegierung) {
        return delegierung.getDelegierterMitarbeiter() != null;
    }
}
