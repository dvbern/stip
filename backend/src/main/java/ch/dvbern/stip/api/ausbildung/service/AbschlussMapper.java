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

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AbschlussSlimDto;
import ch.dvbern.stip.generated.dto.BrueckenangebotCreateDto;
import ch.dvbern.stip.generated.dto.RenameAbschlussDto;
import jakarta.ws.rs.BadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class AbschlussMapper {
    abstract AbschlussDto toDto(Abschluss abschluss);

    @Mapping(target = "ausbildungskategorie", constant = "BRUECKENANGEBOT")
    @Mapping(target = "bildungskategorie", source = ".", qualifiedByName = "getBildungskategorieForBrueckenangebot")
    @Mapping(target = "bfsKategorie", constant = "3")
    @Mapping(target = "berufsbefaehigenderAbschluss", constant = "false")
    @Mapping(target = "ferien", constant = "SCHULE")
    abstract Abschluss createBrueckenangebot(BrueckenangebotCreateDto abschlussDto);

    @Named("getBildungskategorieForBrueckenangebot")
    Bildungskategorie getBildungskategorieForBrueckenangebot(BrueckenangebotCreateDto brueckenangebotCreateDto) {
        return switch (brueckenangebotCreateDto.getBildungsrichtung()) {
            case HOEHERE_BERUFSBILDUNG, HOCHSCHULE -> Bildungskategorie.TERTIAERSTUFE_B;
            case ALLGEMEINBILDENDE_SCHULE, BERUFLICHE_GRUNDBILDUNG -> Bildungskategorie.SEKUNDARSTUFE_II;
            case OBLIGATORISCHE_SCHULE -> throw new BadRequestException(
                "Cannot create Brueckenangebot with Bildungskategorie OBLIGATORISCHE_SCHULE"
            );
        };
    }

    abstract AbschlussSlimDto toSlimDto(Abschluss abschluss);

    abstract void partialUpdate(
        RenameAbschlussDto renameAbschlussDto,
        @MappingTarget Abschluss abschluss
    );
}
