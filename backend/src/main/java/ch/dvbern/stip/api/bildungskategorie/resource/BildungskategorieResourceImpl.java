package ch.dvbern.stip.api.bildungskategorie.resource;

import ch.dvbern.stip.api.bildungskategorie.service.BildungskategorieService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.generated.api.BildungskategorieResource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;

@RequestScoped
@RequiredArgsConstructor
public class BildungskategorieResourceImpl implements BildungskategorieResource {
    private final BildungskategorieService bildungskategorieService;

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public Response getBildungskategorien() {
        final var bildungskategorien = bildungskategorieService.findAll();
        return Response.ok(bildungskategorien).build();
    }
}
