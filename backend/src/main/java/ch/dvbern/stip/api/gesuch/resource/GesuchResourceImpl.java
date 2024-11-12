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

package ch.dvbern.stip.api.gesuch.resource;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.FallAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.json.CreatedResponseBuilder;
import ch.dvbern.stip.api.gesuch.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchResourceImpl implements GesuchResource {
    private final GesuchService gesuchService;
    private final TenantService tenantService;
    private final GesuchHistoryService gesuchHistoryService;
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;
    private final FallAuthorizer fallAuthorizer;

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response changeGesuchStatusToInBearbeitung(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        GesuchDto gesuchDto = gesuchService.gesuchStatusToInBearbeitung(gesuchId);
        return Response.ok(gesuchDto).build();
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public Response changeGesuchStatusToVerfuegt(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToVerfuegt(gesuchId);
        return Response.ok().build();
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public Response changeGesuchStatusToVersendet(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToVersendet(gesuchId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_CREATE)
    @Override
    public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
        gesuchAuthorizer.canCreate();
        GesuchDto created = gesuchService.createGesuch(gesuchCreateDto);
        return CreatedResponseBuilder.of(created.getId(), GesuchResource.class).build();
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    public Response deleteGesuch(UUID gesuchId) {
        gesuchAuthorizer.canDelete(gesuchId);
        gesuchService.deleteGesuch(gesuchId);
        return Response.noContent().build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response gesuchEinreichen(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchEinreichen(gesuchId);
        return Response.accepted().build();
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_UPDATE)
    @AllowAll
    @Override
    public Response gesuchFehlendeDokumenteUebermitteln(UUID gesuchId) {
        gesuchService.gesuchFehlendeDokumente(gesuchId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getCurrentGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        var gesuch = gesuchService.findGesuchWithOldestTranche(gesuchId).orElseThrow(NotFoundException::new);
        return Response.ok(gesuch).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuch(UUID gesuchId, UUID gesuchTrancheId) {
        gesuchAuthorizer.canRead(gesuchId);
        var gesuch = gesuchService.findGesuchWithTranche(gesuchId, gesuchTrancheId).orElseThrow(NotFoundException::new);
        return Response.ok(gesuch).build();
    }

    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public Response getGsDashboard() {
        return Response.ok(gesuchService.getFallDashboardItemDtos()).build();
    }

    @RolesAllowed({ GESUCH_READ, ROLE_GESUCHSTELLER })
    @AllowAll
    @Override
    public Response getGesucheGs() {
        return Response.ok(gesuchService.findGesucheGs()).build();
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesucheSb(
        GetGesucheSBQueryType getGesucheSBQueryType,
        GesuchTrancheTyp typ,
        Integer page,
        Integer pageSize,
        String fallNummer,
        String piaNachname,
        String piaVorname,
        LocalDate piaGeburtsdatum,
        Gesuchstatus status,
        String bearbeiter,
        LocalDate letzteAktivitaetFrom,
        LocalDate letzteAktivitaetTo,
        SbDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        gesuchAuthorizer.allowAllow();

        final var dtos = gesuchService.findGesucheSB(
            getGesucheSBQueryType,
            fallNummer,
            piaNachname,
            piaVorname,
            piaGeburtsdatum,
            status,
            bearbeiter,
            letzteAktivitaetFrom,
            letzteAktivitaetTo,
            typ,
            page,
            pageSize,
            sortColumn,
            sortOrder
        );
        return Response.ok(dtos).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getStatusProtokoll(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        final var statusprotokoll = gesuchHistoryService.getStatusprotokoll(gesuchId);
        return Response.ok(statusprotokoll).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchAuthorizer.canUpdate(gesuchId, gesuchUpdateDto);
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, tenantService.getCurrentTenant().getIdentifier());
        return Response.accepted().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getBerechnungForGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return Response.ok(gesuchService.getBerechnungsresultat(gesuchId)).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGsTrancheChanges(UUID aenderungId) {
        gesuchTrancheAuthorizer.canRead(aenderungId);
        final var changes = gesuchService.getGsTrancheChanges(aenderungId);
        return Response.ok(changes).build();
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public Response getSbTrancheChanges(UUID aenderungId) {
        final var changes = gesuchService.getSbTrancheChanges(aenderungId);
        return Response.ok(changes).build();
    }

    // TODO KSTIP-1247: Only SB can execute these next 3
    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response bearbeitungAbschliessen(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.bearbeitungAbschliessen(gesuchId);
        return Response.ok().build();
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public Response changeGesuchStatusToBereitFuerBearbeitung(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuchId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response gesuchZurueckweisen(UUID gesuchId, KommentarDto kommentarDto) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchZurueckweisen(gesuchId, kommentarDto);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response juristischAbklaeren(UUID gesuchId) {
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.juristischAbklaeren(gesuchId);
        return Response.ok().build();
    }
}
