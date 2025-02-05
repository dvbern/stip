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
import java.util.Comparator;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    config = MappingConfig.class,
    uses = {
        FallMapper.class,
        GesuchsperiodeMapper.class,
        GesuchTrancheMapper.class,
    }
)
public abstract class GesuchMapper {
    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(target = "bearbeiter", source = ".", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(target = "fallId", source = "ausbildung.fall.id")
    @Mapping(target = "fallNummer", source = "ausbildung.fall.fallNummer")
    @Mapping(target = "ausbildungId", source = "ausbildung.id")
    @Mapping(target = "gesuchTrancheToWorkWith", source = "currentGesuchTranche")
    public abstract GesuchDto toDto(Gesuch gesuch);

    @Mapping(source = ".", target = "startDate", qualifiedByName = "getStartDate")
    @Mapping(source = ".", target = "endDate", qualifiedByName = "getEndDate")
    public abstract GesuchInfoDto toInfoDto(Gesuch gesuch);

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
        if (zuordnung == null || zuordnung.getSachbearbeiter() == null) {
            return "";
        }

        return zuordnung.getSachbearbeiter().getFullName();
    }

    @Named("getStartDate")
    static LocalDate getStartDate(Gesuch gesuch) {
        return getGesuchDateRange(gesuch).getGueltigAb();
    }

    @Named("getEndDate")
    static LocalDate getEndDate(Gesuch gesuch) {
        return getGesuchDateRange(gesuch).getGueltigBis();
    }

    static DateRange getGesuchDateRange(Gesuch gesuch) {
        final var tranchenStartEnd = gesuch.getTranchenTranchen().map(GesuchTranche::getGueltigkeit).toList();
        final var startDatum = tranchenStartEnd.stream()
            .min(Comparator.comparing(DateRange::getGueltigAb))
            .map(DateRange::getGueltigAb)
            .orElseThrow(IllegalStateException::new);
        final var endDatum = tranchenStartEnd.stream()
            .max(Comparator.comparing(DateRange::getGueltigBis))
            .map(DateRange::getGueltigBis)
            .orElseThrow(IllegalStateException::new);

        return new DateRange(startDatum, endDatum);
    }
}
