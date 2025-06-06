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

import java.util.Objects;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BuchhaltungMapper {
    @Mapping(source = "stipendium", target = "stipendienBetrag")
    @Mapping(source = "betrag", target = "saldoAenderung")
    @Mapping(source = ".", target = "auszahlung", qualifiedByName = "getAuszahlung")
    @Mapping(source = ".", target = "rueckforderung", qualifiedByName = "getRueckforderung")
    @Mapping(source = ".", target = "sapId", qualifiedByName = "getSapDeliveryId")
    @Mapping(source = "sapDelivery.sapStatus", target = "sapStatus")
    @Mapping(source = "sapDelivery.sapBusinessPartnerId", target = "businessPartnerId")
    @Mapping(source = "gesuch.id", target = "gesuchId")
    public abstract BuchhaltungEntryDto toDto(Buchhaltung buchhaltung);

    @Named("getAuszahlung")
    Integer getAuszahlung(Buchhaltung buchhaltung) {
        if (BuchhaltungType.AUSZAHLUNGS.contains(buchhaltung.getBuchhaltungType()) && buchhaltung.getBetrag() > 0) {
            return buchhaltung.getBetrag();
        }
        return null;
    }

    @Named("getRueckforderung")
    Integer getRueckforderung(Buchhaltung buchhaltung) {
        if (BuchhaltungType.AUSZAHLUNGS.contains(buchhaltung.getBuchhaltungType()) && buchhaltung.getBetrag() < 0) {
            return buchhaltung.getBetrag();
        }
        return null;
    }

    @Named("getSapDeliveryId")
    String getSapDeliveryId(Buchhaltung buchhaltung) {
        if (Objects.nonNull(buchhaltung.getSapDelivery())) {
            return String.valueOf(buchhaltung.getSapDelivery().getSapDeliveryId().longValue());
        }
        return null;
    }
}
