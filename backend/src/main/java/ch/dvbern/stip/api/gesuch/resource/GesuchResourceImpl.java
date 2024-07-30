package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.json.CreatedResponseBuilder;
import ch.dvbern.stip.api.gesuch.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.AenderungsantragCreateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
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

    @Override
    public Response changeGesuchStatusToInBearbeitung(UUID gesuchId) {
        GesuchDto gesuchDto = gesuchService.gesuchStatusToInBearbeitung(gesuchId);
        return Response.ok(gesuchDto).build();
    }

    @Override
    public Response createAenderungsantrag(UUID gesuchId, AenderungsantragCreateDto aenderungsantragCreateDto) {
        // TODO KSTIP-1111: return Gesuch with the created Aenderungsformular attached
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_CREATE)
    @Override
    public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
        GesuchDto created = gesuchService.createGesuch(gesuchCreateDto);
        return CreatedResponseBuilder.of(created.getId(), GesuchResource.class).build();
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    public Response deleteGesuch(UUID gesuchId) {
        gesuchService.deleteGesuch(gesuchId);
        return Response.noContent().build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response gesuchEinreichen(UUID gesuchId) {
        gesuchService.gesuchEinreichen(gesuchId);
        return Response.accepted().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response gesuchEinreichenValidieren(UUID gesuchId) {
        return Response.ok(gesuchService.validateGesuchEinreichen(gesuchId)).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response gesuchFehlendeDokumenteUebermitteln(UUID gesuchId) {
        gesuchService.gesuchFehlendeDokumente(gesuchId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuch(UUID gesuchId) {
        var gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(NotFoundException::new);
        return Response.ok(gesuch).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuchDokumente(UUID gesuchId) {
        var gesuchDokumente = gesuchService.getAndCheckGesuchDokumentsForGesuch(gesuchId);
        return Response.ok(gesuchDokumente).build();
    }

    @RolesAllowed({ GESUCH_READ})
    @Override
    public Response getGesucheSb(GetGesucheSBQueryType getGesucheSBQueryType) {
        return Response.ok(gesuchService.findGesucheSB(getGesucheSBQueryType)).build();
    }

    @RolesAllowed({ GESUCH_READ, ROLE_GESUCHSTELLER })
    @Override
    public Response getGesucheGs() {
        return Response.ok(gesuchService.findGesucheGs()).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesucheForFall(UUID fallId) {
        return Response.ok(gesuchService.findAllForFall(fallId)).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getRequiredGesuchDokumentTyp(UUID gesuchId) {
        final var requiredTypes = gesuchService.getRequiredDokumentTypes(gesuchId);
        return Response.ok(requiredTypes).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getStatusProtokoll(UUID gesuchId) {
        final var statusprotokoll = gesuchHistoryService.getStatusprotokoll(gesuchId);
        return Response.ok(statusprotokoll).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, tenantService.getCurrentTenant().getIdentifier());
        return Response.accepted().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response validateGesuchPages(UUID gesuchId) {
        return Response.ok(gesuchService.validatePages(gesuchId)).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getBerechnungForGesuch(UUID gesuchId) {
        return Response.ok(gesuchService.getBerechnungsresultat(gesuchId)).build();
    }
}
