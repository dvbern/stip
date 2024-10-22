package ch.dvbern.stip.api.sozialdienst.resource;

import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
@RequestScoped
@RequiredArgsConstructor
public class SozialdienstResourceImpl implements SozialdienstResource {
    private final SozialdienstService sozialdienstService;

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        final var sozialdienst = sozialdienstService.createSozialdienst(sozialdienstCreateDto);
        return Response.ok().entity(sozialdienst).build();
    }

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response deleteSozialdienst(UUID sozialdienstId) {
        final var sozialdienst = sozialdienstService.deleteSozialdienst(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response getAllSozialdienste() {
        final var sozialdienste = sozialdienstService.getAllSozialdienst();
        return Response.ok().entity(sozialdienste).build();
    }

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response getSozialdienst(UUID sozialdienstId) {
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @RolesAllowed({OidcConstants.ROLE_SACHBEARBEITER, OidcConstants.ROLE_ADMIN})
    @Override
    public Response updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        final var updated = sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
        return Response.ok().entity(updated).build();
    }
}
