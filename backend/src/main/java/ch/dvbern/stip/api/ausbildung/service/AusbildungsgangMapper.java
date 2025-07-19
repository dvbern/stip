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

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangSlimDto;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class AusbildungsgangMapper {
    @Inject
    AbschlussService abschlussService;

    @Inject
    AusbildungsstaetteService ausbildungsstaetteService;

    @Mapping(target = "ausbildungsstaette", source = "ausbildungsstaetteId", qualifiedByName = "mapAusbildungsstaette")
    @Mapping(target = "abschluss", source = "abschlussId", qualifiedByName = "mapAbschluss")
    public abstract Ausbildungsgang toEntity(AusbildungsgangCreateDto ausbildungsgangDto);

    @Named("mapAbschluss")
    protected Abschluss mapAbschluss(final UUID abschlussId) {
        if (abschlussId == null) {
            return null;
        }
        return abschlussService.requireById(abschlussId);
    }

    @Named("mapAusbildungsstaette")
    protected Ausbildungsstaette mapAusbildungsstaette(final UUID ausbildungsstaetteId) {
        if (ausbildungsstaetteId == null) {
            return null;
        }
        return ausbildungsstaetteService.requireById(ausbildungsstaetteId);
    }

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
        return ausbildungsgang.getAbschluss().getBezeichnungDe();
    }

    @Named("getAusbildungsgangBezeichnungFr")
    public String getAusbildungsgangBezeichnungFr(Ausbildungsgang ausbildungsgang) {
        return ausbildungsgang.getAbschluss().getBezeichnungFr();
    }
}
