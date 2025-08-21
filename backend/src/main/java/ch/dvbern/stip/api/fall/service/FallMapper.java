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

package ch.dvbern.stip.api.fall.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.delegieren.service.DelegierungMapper;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.generated.dto.FailedAuszahlungBuchhaltungDto;
import ch.dvbern.stip.generated.dto.FallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = { DelegierungMapper.class })
public interface FallMapper {

    Fall toEntity(FallDto fallDto);

    FallDto toDto(Fall fall);

    @Mapping(source = "id", target = "fallId")
    @Mapping(source = "fallNummer", target = "fallNummer")
    @Mapping(source = ".", target = "gesuchId", qualifiedByName = "getGesuchId")
    @Mapping(source = ".", target = "gesuchNummer", qualifiedByName = "getGesuchNummer")
    @Mapping(source = "auszahlung.zahlungsverbindung.vorname", target = "vorname")
    @Mapping(source = "auszahlung.zahlungsverbindung.nachname", target = "name")
    @Mapping(source = ".", target = "lastTryDate", qualifiedByName = "getLastTryDate")
    FailedAuszahlungBuchhaltungDto toFailedAuszahlungBuchhaltungDto(Fall fall);

    default Buchhaltung getLastFailedBuchhaltungAuszahlung(Fall fall) {
        return fall.getBuchhaltungs()
            .stream()
            .sorted(Comparator.comparing(Buchhaltung::getTimestampErstellt).reversed())
            .filter(buchhaltung -> buchhaltung.getSapStatus() == SapStatus.FAILURE)
            // .filter(buchhaltung -> BuchhaltungType.AUSZAHLUNGS.contains(buchhaltung.getBuchhaltungType()))
            .findFirst()
            .orElseThrow();
    }

    @Named("getGesuchId")
    default UUID getGesuchId(Fall fall) {
        return getLastFailedBuchhaltungAuszahlung(fall).getGesuch().getId();
    }

    @Named("getGesuchNummer")
    default String getGesuchNummer(Fall fall) {
        return getLastFailedBuchhaltungAuszahlung(fall).getGesuch().getGesuchNummer();
    }

    @Named("getLastTryDate")
    default LocalDateTime getLastTryDate(Fall fall) {
        return getLastFailedBuchhaltungAuszahlung(fall).getSapDeliverys()
            .stream()
            .sorted(Comparator.comparing(SapDelivery::getTimestampErstellt).reversed())
            .findFirst()
            .orElseThrow()
            .getTimestampErstellt();
    }

}
