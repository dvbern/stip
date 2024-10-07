package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsstaetteService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {

    private final UriInfo uriInfo;
    private final AusbildungsstaetteService ausbildungsstaetteService;

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public Response getAusbildungsstaette(UUID ausbildungsstaetteId) {
        return Response.ok(ausbildungsstaetteService.findById(ausbildungsstaetteId)).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public Response getAusbildungsstaetten() {
        return Response.ok(ausbildungsstaetteService.getAusbildungsstaetten()).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    @AllowAll
    public Response createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaette) {
        AusbildungsstaetteDto created = ausbildungsstaetteService.createAusbildungsstaette(ausbildungsstaette);
        return Response.ok(created).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    @AllowAll
    public Response updateAusbildungsstaette(UUID ausbildungsstaetteId, AusbildungsstaetteUpdateDto ausbildungsstaette) {
        AusbildungsstaetteDto updated = ausbildungsstaetteService.updateAusbildungsstaette(ausbildungsstaetteId, ausbildungsstaette);
        return Response.ok(updated).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    @AllowAll
    public Response deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        ausbildungsstaetteService.deleteAusbildungsstaette(ausbildungsstaetteId);
        return Response.noContent().build();
    }
}
