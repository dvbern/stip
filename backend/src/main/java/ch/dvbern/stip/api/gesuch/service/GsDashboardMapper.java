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

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GsDashboardDto;
import ch.dvbern.stip.generated.dto.GsDashboardMissingDocumentsDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;

@ApplicationScoped
@RequiredArgsConstructor
public class GsDashboardMapper {
    private final GesuchsperiodeMapper gesuchsperiodeMapper;

    public GsDashboardDto toDto(
        final Gesuch gesuch,
        final GesuchTrancheSlimDto offeneAenderung,
        final Optional<ImmutablePair<UUID, Integer>> missingDocumentsTrancheIdAndCount
    ) {
        final var periodeDto = gesuchsperiodeMapper.toDto(gesuch.getGesuchsperiode());
        final var missingDocumentsDto = missingDocumentsTrancheIdAndCount
            .map(pair -> new GsDashboardMissingDocumentsDto(pair.getLeft(), pair.getRight()))
            .orElse(null);

        return new GsDashboardDto(
            periodeDto,
            gesuch.getGesuchStatus(),
            gesuch.getId(),
            offeneAenderung,
            missingDocumentsDto
        );
    }
}
