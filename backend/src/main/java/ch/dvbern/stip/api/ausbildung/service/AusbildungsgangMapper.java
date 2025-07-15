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

package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangSlimDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class AusbildungsgangMapper {
    @Mapping(target = "ausbildungsstaette.id", source = "ausbildungsstaetteId")
    @Mapping(target = "abschluss.id", source = "abschlussId")
    public abstract Ausbildungsgang toEntity(AusbildungsgangCreateDto ausbildungsgangDto);

    @Mapping(target = "bezeichnungDe", source = ".", qualifiedByName = "getAusbildungsgangBezeichnungDe")
    @Mapping(target = "bezeichnungFr", source = ".", qualifiedByName = "getAusbildungsgangBezeichnungFr")
    @Mapping(target = "abschlussId", source = "abschluss.id")
    @Mapping(target = "abschlussBezeichnungDe", source = "abschluss.bezeichnungDe")
    @Mapping(target = "abschlussBezeichnungFr", source = "abschluss.bezeichnungFr")
    @Mapping(target = "ausbildungsstaetteId", source = "ausbildungsstaette.id")
    @Mapping(target = "ausbildungsstaetteNameDe", source = "ausbildungsstaette.nameDe")
    @Mapping(target = "ausbildungsstaetteNameFr", source = "ausbildungsstaette.nameFr")
    @Mapping(target = "ausbildungskategorie", source = "abschluss.ausbildungskategorie")
    @Mapping(target = "bildungsrichtung", source = "abschluss.bildungsrichtung")
    public abstract AusbildungsgangDto toDto(Ausbildungsgang ausbildungsgang);

    @Mapping(target = "bezeichnungDe", source = ".", qualifiedByName = "getAusbildungsgangBezeichnungDe")
    @Mapping(target = "bezeichnungFr", source = ".", qualifiedByName = "getAusbildungsgangBezeichnungFr")
    public abstract AusbildungsgangSlimDto toSlimDto(Ausbildungsgang ausbildungsgang);

    @Named("getAusbildungsgangBezeichnungDe")
    public String getAusbildungsgangBezeichnungDe(Ausbildungsgang ausbildungsgang) {
        return String.format(
            "%s - %s",
            ausbildungsgang.getAusbildungsstaette().getNameDe(),
            ausbildungsgang.getAbschluss().getBezeichnungDe()
        );
    }

    @Named("getAusbildungsgangBezeichnungFr")
    public String getAusbildungsgangBezeichnungFr(Ausbildungsgang ausbildungsgang) {
        return String.format(
            "%s - %s",
            ausbildungsgang.getAusbildungsstaette().getNameFr(),
            ausbildungsgang.getAbschluss().getBezeichnungFr()
        );
    }
}
