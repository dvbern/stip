package ch.dvbern.stip.api.bildungsart.resource;

import ch.dvbern.stip.api.bildungsart.service.BildungsartService;
import ch.dvbern.stip.generated.api.BildungsartResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;

@RequestScoped
@RequiredArgsConstructor
public class BildungsartResourceImpl implements BildungsartResource {
    private final BildungsartService bildungsartService;

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public Response getBildungsarten() {
        final var bildungsarten = bildungsartService.findAll();
        return Response.ok(bildungsarten).build();
    }
}
