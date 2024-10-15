package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.FallAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.json.CreatedResponseBuilder;
import ch.dvbern.stip.api.gesuch.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.*;
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
    @Override
    public Response gesuchFehlendeDokumenteUebermitteln(UUID gesuchId) {
        gesuchAuthorizer.allowAllow();
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

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesucheSb(GetGesucheSBQueryType getGesucheSBQueryType) {
        gesuchAuthorizer.allowAllow();
        return Response.ok(gesuchService.findGesucheSB(getGesucheSBQueryType)).build();
    }

    @RolesAllowed({ GESUCH_READ, ROLE_GESUCHSTELLER })
    @Override
    public Response getGesucheGs() {
        gesuchAuthorizer.allowAllow();
        return Response.ok(gesuchService.findGesucheGs()).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesucheForFall(UUID fallId) {
        fallAuthorizer.canRead(fallId);
        return Response.ok(gesuchService.findAllForFall(fallId)).build();
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
        gesuchAuthorizer.canUpdate(gesuchId);
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
        gesuchAuthorizer.canRead(aenderungId);
        final var changes = gesuchService.getGsTrancheChanges(aenderungId);
        return Response.ok(changes).build();
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getSbTrancheChanges(UUID aenderungId) {
        gesuchAuthorizer.allowAllow();
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
