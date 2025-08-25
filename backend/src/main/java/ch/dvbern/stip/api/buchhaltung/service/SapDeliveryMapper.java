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

package ch.dvbern.stip.api.buchhaltung.service;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.generated.dto.SapDeliveryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class SapDeliveryMapper {
    @Mapping(source = "sapDeliveryId", target = "sapId", qualifiedByName = "getSapDeliveryId")
    public abstract SapDeliveryDto toDto(SapDelivery sapDelivery);

    @Named("getSapDeliveryId")
    String getSapDeliveryId(BigDecimal sapDeliveryId) {
        return String.valueOf(sapDeliveryId.longValue());
    }
}
