package ch.dvbern.stip.api.stammdaten.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.stammdaten.service.LandService;
import ch.dvbern.stip.generated.api.StammdatenResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
public class StammdatenResourceImpl implements StammdatenResource {

    private final LandService landService;

    @Override
    @AllowAll
    @RolesAllowed(STAMMDATEN_READ)
    public Response getLaender() {
        var laender = landService.getAllLaender();
        return Response.ok(laender).build();
    }
}
