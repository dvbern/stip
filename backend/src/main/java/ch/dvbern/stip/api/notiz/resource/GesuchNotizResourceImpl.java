package ch.dvbern.stip.api.notiz.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
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

@RequestScoped
@RequiredArgsConstructor
public class GesuchNotizResourceImpl implements GesuchNotizResource {
    private final GesuchNotizService service;
    private final GesuchNotizAuthorizer authorizer;

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response createNotiz(GesuchNotizCreateDto gesuchNotizCreateDto) {
        final var notiz = service.create(gesuchNotizCreateDto);
        return Response.ok(notiz).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response deleteNotiz(UUID notizId) {
        service.delete(notizId);
        return Response.noContent().build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response getNotiz(UUID notizId) {
        authorizer.allowAllow();
        return Response.ok(service.getById(notizId)).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response getNotizen(UUID gesuchId) {
        final var notizen = service.getAllByGesuchId(gesuchId);
        return Response.ok(notizen).build();
    }

    @AllowAll
    @RolesAllowed(OidcConstants.ROLE_SACHBEARBEITER)
    @Override
    public Response updateNotiz(GesuchNotizUpdateDto gesuchNotizUpdateDto) {
        final var notiz = service.update(gesuchNotizUpdateDto);
        return Response.ok(notiz).build();
    }
}
