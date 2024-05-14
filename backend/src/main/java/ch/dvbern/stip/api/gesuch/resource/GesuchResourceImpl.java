package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.json.CreatedResponseBuilder;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchResourceImpl implements GesuchResource {

    private final GesuchService gesuchService;
    private final GesuchDokumentService gesuchDokumentService;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final TenantService tenantService;

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
        GesuchDto created = gesuchService.createGesuch(gesuchCreateDto);
        return CreatedResponseBuilder.of(created.getId(), GesuchResource.class).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response deleteGesuch(UUID gesuchId) {
        gesuchDokumentService.deleteAllDokumentForGesuch(gesuchId);
        gesuchService.deleteGesuch(gesuchId);
        return Response.noContent().build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response gesuchEinreichen(UUID gesuchId) {
        gesuchService.gesuchEinreichen(gesuchId);
        return Response.accepted().build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response gesuchEinreichenValidieren(UUID gesuchId) {
        return Response.ok(gesuchService.validateGesuchEinreichen(gesuchId)).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesuch(UUID gesuchId) {
        var gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(NotFoundException::new);
        return Response.ok(gesuch).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesuchDokumente(UUID gesuchId) {
        var gesuchDokumente = gesuchService.getGesuchDokumenteForGesuch(gesuchId);
        return Response.ok(gesuchDokumente).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesuche() {
        return Response.ok(gesuchService.findAllWithPersonInAusbildung()).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesucheForBenutzer(UUID benutzerId) {
        return Response.ok(gesuchService.findAllForBenutzer(benutzerId)).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesucheForFall(UUID fallId) {
        return Response.ok(gesuchService.findAllForFall(fallId)).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesucheForMe() {
        return Response.ok(gesuchService.findAllForCurrentBenutzer()).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getRequiredGesuchDokumentTyp(UUID gesuchId) {
        final var requiredTypes = gesuchService.getRequiredDokumentTypes(gesuchId);
        return Response.ok(requiredTypes).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, tenantService.getCurrentTenant().getIdentifier());
        return Response.accepted().build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response validateGesuchPages(UUID gesuchId) {
        return Response.ok(gesuchService.validatePages(gesuchId)).build();
    }
}
