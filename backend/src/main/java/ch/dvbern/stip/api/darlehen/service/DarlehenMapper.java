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

package ch.dvbern.stip.api.darlehen.service;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.DarlehenDashboardDto;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = DarlehenDokumentMapper.class)
public abstract class DarlehenMapper {
    @Mapping(source = "fall.id", target = "fallId")
    @Mapping(source = "relatedGesuch.id", target = "relatedGesuchId")
    public abstract DarlehenDto toDto(Darlehen darlehen);

    @Mapping(source = "fall.fallNummer", target = "fallNummer")
    @Mapping(source = "fall.id", target = "fallId")
    @Mapping(source = "relatedGesuch.id", target = "relatedGesuchId")
    @Mapping(source = ".", target = "piaVorname", qualifiedByName = "getPiaVorname")
    @Mapping(source = ".", target = "piaNachname", qualifiedByName = "getPiaNachname")
    @Mapping(source = ".", target = "piaGeburtsdatum", qualifiedByName = "getPiaGeburtsdatum")
    @Mapping(source = ".", target = "bearbeiter", qualifiedByName = "getBearbeiter")
    @Mapping(source = "timestampMutiert", target = "letzteAktivitaet")
    public abstract DarlehenDashboardDto toDashboardDto(Darlehen darlehen);

    public abstract Darlehen toEntity(DarlehenDto darlehenDto);

    public abstract Darlehen partialUpdate(
        DarlehenUpdateGsDto darlehenDto,
        @MappingTarget Darlehen darlehen
    );

    public abstract Darlehen partialUpdate(
        DarlehenUpdateSbDto darlehenDto,
        @MappingTarget Darlehen darlehen
    );

    @Named("getPiaNachname")
    public String getPiaNachname(Darlehen darlehen) {
        return getPia(darlehen)
            .getNachname();
    }

    @Named("getPiaVorname")
    public String getPiaVorname(Darlehen darlehen) {
        return getPia(darlehen)
            .getVorname();
    }

    @Named("getPiaGeburtsdatum")
    public LocalDate getPiaGeburtsdatum(Darlehen darlehen) {
        return getPia(darlehen)
            .getGeburtsdatum();
    }

    private static PersonInAusbildung getPia(Darlehen darlehen) {
        return darlehen.getFall()
            .getLatestGesuch()
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();
    }

    @Named("getBearbeiter")
    public String getBearbeiter(Darlehen darlehen) {
        return darlehen.getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter()
            .getFullName();
    }
}
