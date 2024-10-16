package ch.dvbern.stip.api.notiz.resource;

import ch.dvbern.stip.api.common.authorization.GesuchNotizAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.notiz.service.GesuchNotizService;
import ch.dvbern.stip.generated.api.GesuchNotizResource;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
@RequestScoped
@RequiredArgsConstructor
public class GesuchNotizResourceImpl implements GesuchNotizResource {
    private final GesuchNotizService service;
    private final GesuchNotizAuthorizer authorizer;

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response createNotiz(GesuchNotizCreateDto gesuchNotizCreateDto) {
        authorizer.allowAllow();
        final var notiz = service.create(gesuchNotizCreateDto);
        return Response.ok().entity(notiz).build();
    }
    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response deleteNotiz(UUID notizId) {
        authorizer.allowAllow();
        service.delete(notizId);
        return Response.noContent().build();
    }
    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response getNotizen(UUID gesuchId) {
        authorizer.allowAllow();
        final var notizen = service.getAllByGesuchId(gesuchId);
        return Response.ok().entity(notizen).build();
    }
    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response updateNotiz(GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        authorizer.allowAllow();
        final var notiz = service.update(gesuchNotizUpdateDto);
        return Response.ok().entity(notiz).build();
    }
}
