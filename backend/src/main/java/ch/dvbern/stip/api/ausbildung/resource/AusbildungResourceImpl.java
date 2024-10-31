package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.generated.api.AusbildungResource;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungResourceImpl implements AusbildungResource {
    private final AusbildungService ausbildungService;
    private final AusbildungAuthorizer ausbildungAuthorizer;

    @Override
    @RolesAllowed(GESUCH_UPDATE)
    @AllowAll
    public Response createAusbildung(AusbildungUpdateDto ausbildungUpdateDto) {
        return Response.ok(ausbildungService.createAusbildung(ausbildungUpdateDto)).build();
    }

    @Override
    @RolesAllowed(GESUCH_READ)
    public Response getAusbildung(UUID ausbildungId) {
        ausbildungAuthorizer.canRead(ausbildungId);
        return Response.ok(ausbildungService.getAusbildungById(ausbildungId)).build();
    }

    @Override
    @RolesAllowed(GESUCH_UPDATE)
    public Response updateAusbildung(UUID ausbildungId, AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canUpdate(ausbildungId);
        return Response.ok(ausbildungService.patchAusbildung(ausbildungId, ausbildungUpdateDto)).build();
    }
}
