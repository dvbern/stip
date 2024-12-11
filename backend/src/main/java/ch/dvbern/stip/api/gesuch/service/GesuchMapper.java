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

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    config = MappingConfig.class,
    uses = {
        FallMapper.class,
        GesuchsperiodeMapper.class
    }
)
public abstract class GesuchMapper {
    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", source = ".", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(target = "fallId", source = "ausbildung.fall.id")
    @Mapping(target = "fallNummer", source = "ausbildung.fall.fallNummer")
    @Mapping(target = "ausbildungId", source = "ausbildung.id")
    public abstract GesuchDto toDto(Gesuch gesuch);

    @Mapping(source = "ausbildungId", target = "ausbildung.id")
    // @Mapping(source = "gesuchsperiodeId", target = "gesuchsperiode.id")
    public abstract Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);

    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", source = ".", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(source = "ausbildung.fall.id", target = "fallId")
    @Mapping(source = "ausbildung.fall.fallNummer", target = "fallNummer")
    @Mapping(source = "ausbildung.id", target = "ausbildungId")
    public abstract GesuchWithChangesDto toWithChangesDto(Gesuch gesuch);

    @Named("getFullNameOfSachbearbeiter")
    String getFullNameOfSachbearbeiter(Gesuch gesuch) {
        final var zuordnung = gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung();
        if (zuordnung == null) {
            return "";
        }

        return zuordnung.getSachbearbeiter().getFullName();
    }
}
