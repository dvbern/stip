package ch.dvbern.stip.api.plz.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.generated.api.PlzResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class PlzResourceImpl implements PlzResource {
    private final PlzService plzService;

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getPlz() {
        return Response.ok(plzService.getAllPlz()).build();
    }
}
