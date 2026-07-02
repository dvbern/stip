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

import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.service.DelegierungMapper;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodeMapper;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    config = MappingConfig.class,
    uses = {
        DelegierungMapper.class,
        FallMapper.class,
        GesuchsperiodeMapper.class,
        GesuchTrancheMapper.class,
        GesuchStateInfoMapper.class,
    }
)
public abstract class GesuchMapper {
    @Inject
    FallService fallService;

    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(source = ".", target = "bearbeiter", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(source = ".", target = "hadDelegierungs", qualifiedByName = "getHadDelegierungs")
    @Mapping(source = ".", target = "delegierung", qualifiedByName = "getCurrentDelegierung")
    @Mapping(source = "ausbildung.fall.id", target = "fallId")
    @Mapping(source = "ausbildung.fall.fallNummer", target = "fallNummer")
    @Mapping(source = "ausbildung.id", target = "ausbildungId")
    @Mapping(
        target = "hasPendingAusbildungUnterbruchAntrag",
        source = ".",
        qualifiedByName = "hasPendingAusbildungUnterbruchAntrag"
    )
    public abstract GesuchDto toDto(Gesuch gesuch);

    @Mapping(source = "ausbildung.fall.fallNummer", target = "fallNummer")
    @Mapping(source = "ausbildung.fall.id", target = "fallId")
    @Mapping(source = ".", target = "startDate", qualifiedByName = "getStartDate")
    @Mapping(source = ".", target = "endDate", qualifiedByName = "getEndDate")
    @Mapping(source = ".", target = "state")
    @Mapping(source = ".", target = "piaVorname", qualifiedByName = "getPiaVorname")
    @Mapping(source = ".", target = "piaNachname", qualifiedByName = "getPiaNachname")
    public abstract GesuchInfoDto toInfoDtoGs(Gesuch gesuch);

    @Mapping(source = "ausbildung.fall.fallNummer", target = "fallNummer")
    @Mapping(source = "ausbildung.fall.id", target = "fallId")
    @Mapping(source = ".", target = "startDate", qualifiedByName = "getStartDate")
    @Mapping(source = ".", target = "endDate", qualifiedByName = "getEndDate")
    @Mapping(source = ".", target = "state")
    @Mapping(source = ".", target = "piaVorname", qualifiedByName = "getPiaVorname")
    @Mapping(source = ".", target = "piaNachname", qualifiedByName = "getPiaNachname")
    public abstract GesuchInfoDto toInfoDtoSb(Gesuch gesuch);

    @Nullable
    @Named("getPiaVorname")
    String getPiaVorname(final Gesuch gesuch) {
        return GesuchMapperUtil.getLatestPia(gesuch).map(PersonInAusbildung::getVorname).orElse(null);
    }

    @Nullable
    @Named("getPiaNachname")
    String getPiaNachname(final Gesuch gesuch) {
        return GesuchMapperUtil.getLatestPia(gesuch).map(PersonInAusbildung::getNachname).orElse(null);
    }

    @Mapping(source = "ausbildungId", target = "ausbildung.id")
    public abstract Gesuch toNewEntity(GesuchCreateDto gesuchCreateDto);

    @Mapping(source = "timestampMutiert", target = "aenderungsdatum")
    @Mapping(source = ".", target = "bearbeiter", qualifiedByName = "getFullNameOfSachbearbeiter")
    @Mapping(source = ".", target = "hadDelegierungs", qualifiedByName = "getHadDelegierungs")
    @Mapping(source = ".", target = "delegierung", qualifiedByName = "getCurrentDelegierung")
    @Mapping(source = "ausbildung.fall.id", target = "fallId")
    @Mapping(source = "ausbildung.fall.fallNummer", target = "fallNummer")
    @Mapping(source = "ausbildung.id", target = "ausbildungId")
    @Mapping(
        source = ".", target = "hasPendingAusbildungUnterbruchAntrag",
        qualifiedByName = "hasPendingAusbildungUnterbruchAntrag"
    )
    public abstract GesuchWithChangesDto toWithChangesDto(Gesuch gesuch);

    @Named("hasPendingAusbildungUnterbruchAntrag")
    public boolean hasPendingAusbildungUnterbruchAntrag(final Gesuch gesuch) {
        return gesuch.getAusbildung()
            .getAusbildungUnterbruchAntrags()
            .stream()
            .anyMatch(
                ausbildungUnterbruchAntrag -> ausbildungUnterbruchAntrag
                    .getStatus() == AusbildungUnterbruchAntragStatus.EINGEGEBEN
            );
    }

    @Named("getFullNameOfSachbearbeiter")
    String getFullNameOfSachbearbeiter(Gesuch gesuch) {
        final var zuordnung = gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung();
        if (Objects.isNull(zuordnung) || Objects.isNull(zuordnung.getSachbearbeiter())) {
            return "";
        }

        return zuordnung.getSachbearbeiter().getFullName();
    }

    private Fall getFall(Gesuch gesuch) {
        return fallService.getById(gesuch.getAusbildung().getFall().getId());
    }

    @Named("getCurrentDelegierung")
    Delegierung getCurrentDelegierung(Gesuch gesuch) {
        return getFall(gesuch).getCurrentDelegierung();
    }

    @Named("getHadDelegierungs")
    Boolean getHadDelegierungs(Gesuch gesuch) {
        return !getFall(gesuch).getHistoricalDelegierungs().isEmpty();
    }

    @Named("getStartDate")
    static LocalDate getStartDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigAb();
    }

    @Named("getEndDate")
    static LocalDate getEndDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigBis();
    }
}
