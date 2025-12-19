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

package ch.dvbern.stip.api.gesuch.util;

import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchMapperUtil {
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;

    public GesuchDto mapWithGesuchOfTranche(final GesuchTranche gesuchTranche, final boolean withVersteckteEltern) {
        return mapWithTranche(gesuchTranche.getGesuch(), gesuchTranche, withVersteckteEltern);
    }

    public GesuchDto mapWithNewestTranche(final Gesuch gesuch, final boolean withVersteckteEltern) {
        return mapWithTranche(
            gesuch,
            gesuch.getNewestGesuchTranche().orElseThrow(IllegalStateException::new),
            withVersteckteEltern
        );
    }

    public GesuchDto mapWithTranche(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final boolean withVersteckteEltern
    ) {
        final var gesuchDto = gesuchMapper.toDto(gesuch);
        final GesuchTrancheDto gesuchTrancheToWorkWith;
        if (withVersteckteEltern) {
            gesuchTrancheToWorkWith = gesuchTrancheMapper.toDtoWithVersteckteEltern(tranche);
        } else {
            gesuchTrancheToWorkWith = gesuchTrancheMapper.toDtoWithoutVersteckteEltern(tranche);
        }

        gesuchDto.setGesuchTrancheToWorkWith(gesuchTrancheToWorkWith);
        return gesuchDto;
    }

    public GesuchWithChangesDto toWithChangesDto(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final GesuchTranche changes,
        final boolean isInitial,
        final boolean withVersteckteEltern
    ) {
        var dto = gesuchMapper.toWithChangesDto(gesuch);
        dto.setIsInitial(isInitial);
        dto.setGesuchTrancheToWorkWith(mapWithOrWithoutEltern(tranche, withVersteckteEltern));
        if (Objects.isNull(changes)) {
            dto.setChanges(List.of());
        } else {
            dto.setChanges(List.of(mapWithOrWithoutEltern(changes, withVersteckteEltern)));
        }
        return dto;
    }

    public GesuchWithChangesDto toWithChangesDto(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final GesuchTranche changes,
        final boolean withVersteckteEltern
    ) {
        return toWithChangesDto(
            gesuch,
            tranche,
            changes,
            false,
            withVersteckteEltern
        );
    }

    public GesuchWithChangesDto toWithChangesDto(
        final Gesuch gesuch,
        final GesuchTranche tranche,
        final List<GesuchTranche> changes
    ) {
        final var dto = gesuchMapper.toWithChangesDto(gesuch);
        dto.setGesuchTrancheToWorkWith(gesuchTrancheMapper.toDtoWithVersteckteEltern(tranche));
        dto.setChanges(changes.stream().map(gesuchTrancheMapper::toDtoWithVersteckteEltern).toList());
        return dto;
    }

    private GesuchTrancheDto mapWithOrWithoutEltern(final GesuchTranche tranche, final boolean withVersteckteEltern) {
        if (withVersteckteEltern) {
            return gesuchTrancheMapper.toDtoWithVersteckteEltern(tranche);
        } else {
            return gesuchTrancheMapper.toDtoWithoutVersteckteEltern(tranche);
        }
    }
}
