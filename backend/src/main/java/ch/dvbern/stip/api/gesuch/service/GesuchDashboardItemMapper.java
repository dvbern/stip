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

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemMissingDocumentsDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = GesuchsperiodeMapper.class)
public interface GesuchDashboardItemMapper {
    @Named("toGesuchDashboardItemDto")
    @Mapping(source = "gesuch.gesuchsperiode", target = "gesuchsperiode")
    @Mapping(source = "gesuch.gesuchStatus", target = "gesuchStatus")
    @Mapping(source = "gesuch.id", target = "id")
    @Mapping(source = "offeneAenderung", target = "offeneAenderung")
    @Mapping(source = "missingDocumentsTrancheIdAndCount", target = "missingDocuments")
    GesuchDashboardItemDto toDto(
        final Gesuch gesuch,
        final GesuchTrancheSlimDto offeneAenderung,
        final Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    );

    default GesuchDashboardItemMissingDocumentsDto map(
        Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    ) {
        return missingDocumentsTrancheIdAndCount
            .map(pair -> new GesuchDashboardItemMissingDocumentsDto(pair.getLeft(), pair.getRight()))
            .orElse(null);
    }
}