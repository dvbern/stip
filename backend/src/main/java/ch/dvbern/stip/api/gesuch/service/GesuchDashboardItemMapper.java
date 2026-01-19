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

package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemMissingDocumentsDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = GesuchsperiodeMapper.class)
public abstract class GesuchDashboardItemMapper {
    @Inject
    GesuchHistoryService gesuchHistoryService;

    @Named("toGesuchDashboardItemDto")
    @Mapping(source = "gesuch.gesuchsperiode", target = "gesuchsperiode")
    @Mapping(source = "gesuch", target = "gesuchStatus", qualifiedByName = "getGesuchStatus")
    @Mapping(source = "gesuch.id", target = "id")
    @Mapping(source = "offeneAenderung", target = "offeneAenderung")
    @Mapping(source = "missingDocumentsTrancheIdAndCount", target = "missingDocuments")
    @Mapping(source = "gesuch", target = "currentTrancheId", qualifiedByName = "getCurrentGesuchTrancheId")
    @Mapping(source = "gesuch", target = "startDate", qualifiedByName = "getStartDate")
    @Mapping(source = "gesuch", target = "endDate", qualifiedByName = "getEndDate")
    @Mapping(source = "gesuch.nachfristDokumente", target = "nachfristDokumente")
    public abstract GesuchDashboardItemDto toDto(
        final Gesuch gesuch,
        final GesuchTrancheSlimDto offeneAenderung,
        final Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    );

    GesuchDashboardItemMissingDocumentsDto map(
        Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    ) {
        return missingDocumentsTrancheIdAndCount
            .map(pair -> new GesuchDashboardItemMissingDocumentsDto(pair.getLeft(), pair.getRight()))
            .orElse(null);
    }

    @Named("getStartDate")
    static LocalDate getStartDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigAb();
    }

    @Named("getEndDate")
    static LocalDate getEndDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigBis();
    }

    @Named("getCurrentGesuchTrancheId")
    UUID getCurrentGesuchTrancheId(Gesuch gesuch) {
        final var gesuchToUse = gesuchHistoryService.getCurrentOrHistoricalGesuchForGS(gesuch.getId());
        if (Objects.nonNull(gesuchToUse.getLatestGesuchTranche())) {
            return gesuchToUse.getLatestGesuchTranche().getId();
        }
        return gesuchToUse.getGesuchTranchen().get(0).getId();
    }

    @Named("getGesuchStatus")
    Gesuchstatus getGesuchstatus(Gesuch gesuch) {
        if (Gesuchstatus.GESUCHSTELLER_RECEIVES_CURRENT_GESUCH.contains(gesuch.getGesuchStatus())) {
            return gesuch.getGesuchStatus();
        }
        return Gesuchstatus.EINGEREICHT;
    }
}
