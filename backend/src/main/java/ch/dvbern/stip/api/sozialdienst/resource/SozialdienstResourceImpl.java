package ch.dvbern.stip.api.sozialdienst.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.SozialdienstAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.generated.api.SozialdienstResource;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class SozialdienstResourceImpl implements SozialdienstResource {

    private final SozialdienstService sozialdienstService;

    private final SozialdienstAuthorizer sozialdienstAuthorizer;

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response createSozialdienst(SozialdienstCreateDto sozialdienstCreateDto) {
        sozialdienstAuthorizer.allowAllow();
        final var sozialdienst = sozialdienstService.createSozialdienst(sozialdienstCreateDto);
        return Response.ok(sozialdienst).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response deleteSozialdienst(UUID sozialdienstId) {
        sozialdienstAuthorizer.allowAllow();
        final var sozialdienst = sozialdienstService.deleteSozialdienst(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response getAllSozialdienste() {
        sozialdienstAuthorizer.allowAllow();
        final var sozialdienste = sozialdienstService.getAllSozialdienst();
        return Response.ok().entity(sozialdienste).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response getSozialdienst(UUID sozialdienstId) {
        sozialdienstAuthorizer.allowAllow();
        final var sozialdienst = sozialdienstService.getSozialdienstById(sozialdienstId);
        return Response.ok().entity(sozialdienst).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response replaceSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminCreateDto sozialdienstAdminCreateDto
    ) {
        sozialdienstAuthorizer.allowAllow();
        final var updated = sozialdienstService.replaceSozialdienstAdmin(sozialdienstId, sozialdienstAdminCreateDto);
        return Response.ok().entity(updated).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response updateSozialdienst(SozialdienstUpdateDto sozialdienstUpdateDto) {
        sozialdienstAuthorizer.allowAllow();
        final var updated = sozialdienstService.updateSozialdienst(sozialdienstUpdateDto);
        return Response.ok().entity(updated).build();
    }

    @RolesAllowed({ OidcConstants.ROLE_ADMIN })
    @Override
    public Response updateSozialdienstAdmin(
        UUID sozialdienstId,
        SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto) {
        sozialdienstAuthorizer.allowAllow();
        final var updated = sozialdienstService.updateSozialdienstAdmin(sozialdienstId, sozialdienstAdminUpdateDto);
        return Response.ok().entity(updated).build();
    }
}
