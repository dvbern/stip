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

package ch.dvbern.stip.api.ausbildung.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AbschlussService;
import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangService;
import ch.dvbern.stip.api.ausbildung.service.AusbildungsstaetteService;
import ch.dvbern.stip.api.ausbildung.type.AbschlussSortColumn;
import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsgangSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.common.authorization.AusbildungsstaetteAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteSlimDto;
import ch.dvbern.stip.generated.dto.BrueckenangebotCreateDto;
import ch.dvbern.stip.generated.dto.PaginatedAbschlussDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsstaetteDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNGSSTAETTE_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {
    private final AusbildungsstaetteAuthorizer ausbildungsstaetteAuthorizer;
    private final AusbildungsstaetteService ausbildungsstaetteService;
    private final AbschlussService abschlussService;
    private final AusbildungsgangService ausbildungsgangService;

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_CREATE)
    public AbschlussDto createAbschlussBrueckenangebot(BrueckenangebotCreateDto brueckenangebotCreateDto) {
        ausbildungsstaetteAuthorizer.canCreate();
        return abschlussService.createAbschlussBrueckenangebot(brueckenangebotCreateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_CREATE)
    public AusbildungsgangDto createAusbildungsgang(AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        ausbildungsstaetteAuthorizer.canCreate();
        return ausbildungsgangService.createAusbildungsgang(ausbildungsgangCreateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_CREATE)
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto) {
        ausbildungsstaetteAuthorizer.canCreate();
        return ausbildungsstaetteService.createAusbildungsstaette(ausbildungsstaetteCreateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public PaginatedAbschlussDto getAllAbschlussForUebersicht(
        Integer page,
        Integer pageSize,
        AbschlussSortColumn sortColumn,
        SortOrder sortOrder,
        Ausbildungskategorie ausbildungskategorie,
        Bildungskategorie bildungskategorie,
        Bildungsrichtung bildungsrichtung,
        Integer bfsKategorie,
        Boolean berufsbefaehigenderAbschluss,
        FerienTyp ferien,
        String bezeichnungDe,
        String bezeichnungFr,
        AbschlussZusatzfrage zusatzfrage,
        Boolean aktiv
    ) {
        ausbildungsstaetteAuthorizer.canRead();
        return abschlussService.getAllAbschlussForUebersicht(
            page,
            pageSize,
            sortColumn,
            sortOrder,
            ausbildungskategorie,
            bildungskategorie,
            bildungsrichtung,
            bfsKategorie,
            berufsbefaehigenderAbschluss,
            ferien,
            bezeichnungDe,
            bezeichnungFr,
            zusatzfrage,
            aktiv
        );
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public PaginatedAusbildungsgangDto getAllAusbildungsgangForUebersicht(
        Integer page,
        Integer pageSize,
        AusbildungsgangSortColumn sortColumn,
        SortOrder sortOrder,
        String abschlussBezeichnungDe,
        String abschlussBezeichnungFr,
        String ausbildungsstaetteNameDe,
        String ausbildungsstaetteNameFr,
        Boolean aktiv
    ) {
        ausbildungsstaetteAuthorizer.canRead();
        return ausbildungsgangService.getAllAusbildungsgangForUebersicht(
            page,
            pageSize,
            sortColumn,
            sortOrder,
            abschlussBezeichnungDe,
            abschlussBezeichnungFr,
            ausbildungsstaetteNameDe,
            ausbildungsstaetteNameFr,
            aktiv
        );
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public List<AusbildungsstaetteSlimDto> getAllAusbildungsstaetteForAuswahl() {
        ausbildungsstaetteAuthorizer.canRead();
        return ausbildungsstaetteService.getAllAusbildungsstaetteForAuswahl();
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_READ)
    public PaginatedAusbildungsstaetteDto getAllAusbildungsstaetteForUebersicht(
        Integer page,
        Integer pageSize,
        AusbildungsstaetteSortColumn sortColumn,
        SortOrder sortOrder,
        String nameDe,
        String nameFr,
        String chShis,
        String burNo,
        String ctNo,
        Boolean aktiv
    ) {
        ausbildungsstaetteAuthorizer.canRead();
        return ausbildungsstaetteService.getAllAusbildungsstaetteForUebersicht(
            page,
            pageSize,
            sortColumn,
            sortOrder,
            nameDe,
            nameFr,
            chShis,
            burNo,
            ctNo,
            aktiv
        );
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_UPDATE)
    public AbschlussDto setAbschlussInaktiv(UUID abschlussId) {
        ausbildungsstaetteAuthorizer.canUpdate();
        return abschlussService.setAbschlussInaktiv(abschlussId);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_UPDATE)
    public AusbildungsgangDto setAusbildungsgangInaktiv(UUID ausbildungsgangId) {
        ausbildungsstaetteAuthorizer.canUpdate();
        return ausbildungsgangService.setAusbildungsgangInaktiv(ausbildungsgangId);
    }

    @Override
    @RolesAllowed(AUSBILDUNGSSTAETTE_UPDATE)
    public AusbildungsstaetteDto setAusbildungsstaetteInaktiv(UUID ausbildungsstaetteId) {
        ausbildungsstaetteAuthorizer.canUpdate();
        return ausbildungsstaetteService.setAusbildungsstaetteInaktiv(ausbildungsstaetteId);
    }
}
