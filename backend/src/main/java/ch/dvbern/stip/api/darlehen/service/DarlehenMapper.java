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
import java.util.Comparator;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDashboardDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateSbDto;
import jakarta.ws.rs.NotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = DarlehenDokumentMapper.class)
public abstract class DarlehenMapper {
    @Mapping(source = "fall.id", target = "fallId")
    public abstract FreiwilligDarlehenDto toDto(FreiwilligDarlehen freiwilligDarlehen);

    @Mapping(source = "fall.fallNummer", target = "fallNummer")
    @Mapping(source = "fall.id", target = "fallId")
    @Mapping(source = ".", target = "gesuchId", qualifiedByName = "getGesuchId")
    @Mapping(source = ".", target = "gesuchTrancheId", qualifiedByName = "getGesuchTrancheId")
    @Mapping(source = ".", target = "piaVorname", qualifiedByName = "getPiaVorname")
    @Mapping(source = ".", target = "piaNachname", qualifiedByName = "getPiaNachname")
    @Mapping(source = ".", target = "piaGeburtsdatum", qualifiedByName = "getPiaGeburtsdatum")
    @Mapping(source = ".", target = "bearbeiter", qualifiedByName = "getBearbeiter")
    @Mapping(source = "timestampMutiert", target = "letzteAktivitaet")
    public abstract FreiwilligDarlehenDashboardDto toDashboardDto(FreiwilligDarlehen freiwilligDarlehen);

    public abstract FreiwilligDarlehen toEntity(FreiwilligDarlehenDto darlehenDto);

    public abstract FreiwilligDarlehen partialUpdate(
        FreiwilligDarlehenUpdateGsDto darlehenDto,
        @MappingTarget FreiwilligDarlehen freiwilligDarlehen
    );

    public abstract FreiwilligDarlehen partialUpdate(
        FreiwilligDarlehenUpdateSbDto darlehenDto,
        @MappingTarget FreiwilligDarlehen freiwilligDarlehen
    );

    private Gesuch getGesuch(FreiwilligDarlehen freiwilligDarlehen) {
        return freiwilligDarlehen
            .getFall()
            .getAusbildungs()
            .stream()
            .sorted(Comparator.comparing(Ausbildung::getTimestampErstellt).reversed())
            .flatMap(a -> a.getGesuchs().stream().sorted(Comparator.comparing(Gesuch::getTimestampErstellt).reversed()))
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    @Named("getGesuchId")
    public UUID getGesuchId(FreiwilligDarlehen freiwilligDarlehen) {
        return getGesuch(freiwilligDarlehen).getId();
    }

    @Named("getGesuchTrancheId")
    public UUID getGesuchTrancheId(FreiwilligDarlehen freiwilligDarlehen) {
        return getGesuch(freiwilligDarlehen).getLatestGesuchTranche().getId();
    }

    @Named("getPiaNachname")
    public String getPiaNachname(FreiwilligDarlehen freiwilligDarlehen) {
        return getPia(freiwilligDarlehen)
            .getNachname();
    }

    @Named("getPiaVorname")
    public String getPiaVorname(FreiwilligDarlehen freiwilligDarlehen) {
        return getPia(freiwilligDarlehen)
            .getVorname();
    }

    @Named("getPiaGeburtsdatum")
    public LocalDate getPiaGeburtsdatum(FreiwilligDarlehen freiwilligDarlehen) {
        return getPia(freiwilligDarlehen)
            .getGeburtsdatum();
    }

    private static PersonInAusbildung getPia(FreiwilligDarlehen freiwilligDarlehen) {
        return freiwilligDarlehen.getFall()
            .getLatestGesuch()
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();
    }

    @Named("getBearbeiter")
    public String getBearbeiter(FreiwilligDarlehen freiwilligDarlehen) {
        return freiwilligDarlehen.getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter()
            .getFullName();
    }
}
