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

package ch.dvbern.stip.api.darlehen.resource;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.DarlehenAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.darlehen.service.DarlehenService;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import ch.dvbern.stip.api.darlehen.type.GetDarlehenSBQueryType;
import ch.dvbern.stip.api.darlehen.type.SbDarlehenDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.api.DarlehenResource;
import ch.dvbern.stip.generated.dto.DarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDarlehenDashboardDto;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.FREIGABESTELLE_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class DarlehenResourceImpl implements DarlehenResource {

    private final DarlehenService darlehenService;
    private final DarlehenAuthorizer darlehenAuthorizer;

    @Override
    public DarlehenDto getDarlehenGs(UUID fallId) {
        darlehenAuthorizer.canGetDarlehenGs(fallId);
        return darlehenService.getDarlehenGs(fallId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public DarlehenDto createDarlehen(UUID fallId) {
        darlehenAuthorizer.canCreateDarlehen(fallId);
        return darlehenService.createDarlehen(fallId);
    }

    @Override
    @RolesAllowed({SB_GESUCH_UPDATE, FREIGABESTELLE_GESUCH_UPDATE})
    public DarlehenDto darlehenAblehen(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAblehenen(darlehenId);
        return darlehenService.darlehenAblehen(darlehenId);
    }

    @Override
    @RolesAllowed({SB_GESUCH_UPDATE, FREIGABESTELLE_GESUCH_UPDATE})
    public DarlehenDto darlehenAkzeptieren(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAkzeptieren(darlehenId);
        return darlehenService.darlehenAkzeptieren(darlehenId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public DarlehenDto darlehenEingeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenEingeben(darlehenId);
        return darlehenService.darlehenEingeben(darlehenId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenFreigeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenFreigeben(darlehenId);
        return darlehenService.darlehenFreigeben(darlehenId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenZurueckweisen(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenZurueckweisen(darlehenId);
        return darlehenService.darlehenZurueckweisen(darlehenId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public DarlehenDto darlehenUpdateGs(UUID darlehenId, DarlehenUpdateGsDto darlehenUpdateGsDto) {
        darlehenAuthorizer.canDarlehenUpdateGs(darlehenId);
        return darlehenService.darlehenUpdateGs(darlehenId, darlehenUpdateGsDto);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenUpdateSb(UUID darlehenId, DarlehenUpdateSbDto darlehenUpdateSbDto) {
        darlehenAuthorizer.canDarlehenUpdateSb(darlehenId);
        return darlehenService.darlehenUpdateSb(darlehenId, darlehenUpdateSbDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_CREATE)
    public Uni<Response> createDarlehenDokument(
        UUID darlehenId,
        DarlehenDokumentType dokumentTyp,
        FileUpload fileUpload
    ) {
        darlehenAuthorizer.canCreateDarlehenDokument(darlehenId);
        return darlehenService.uploadDarlehenDokument(darlehenId, dokumentTyp, fileUpload);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public DarlehenDokumentDto getDarlehenDokument(UUID darlehenId, DarlehenDokumentType dokumentTyp) {
        darlehenAuthorizer.canGetDarlehenDokument();
        return darlehenService.getDarlehenDokument(darlehenId, dokumentTyp);
    }

    @Override
    public PaginatedSbDarlehenDashboardDto getDarlehenSb(
        GetDarlehenSBQueryType SbDarlehenDashboardColumn,
        Integer page,
        Integer pageSize,
        String fallNummer,
        String piaNachname,
        String piaVorname,
        LocalDate piaGeburtsdatum,
        String status,
        String bearbeiter,
        LocalDate letzteAktivitaetFrom,
        LocalDate letzteAktivitaetTo,
        SbDarlehenDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        darlehenAuthorizer.canGetDarlehenSb();
        return null;
    }
}
