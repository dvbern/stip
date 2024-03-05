package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsstaetteService;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {

    private final UriInfo uriInfo;
    private final AusbildungsstaetteService ausbildungsstaetteService;

    @Override
    public Response getAusbildungsstaette(UUID ausbildungsstaetteId) {
        return Response.ok(ausbildungsstaetteService.findById(ausbildungsstaetteId)).build();
    }

    @Override
    public Response getAusbildungsstaetten() {
        return Response.ok(ausbildungsstaetteService.getAusbildungsstaetten()).build();
    }

    @Override
    public Response createAusbildungsstaette(AusbildungsstaetteUpdateDto ausbildungsstaette) {
        AusbildungsstaetteDto created = ausbildungsstaetteService.createAusbildungsstaette(ausbildungsstaette);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build()).build();
    }

    @Override
    public Response updateAusbildungsstaette(UUID ausbildungsstaetteId, AusbildungsstaetteUpdateDto ausbildungsstaette) {
        ausbildungsstaetteService.updateAusbildungsstaette(ausbildungsstaetteId, ausbildungsstaette);
        return Response.accepted().build();
    }

    @Override
    public Response deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        ausbildungsstaetteService.deleteAusbildungsstaette(ausbildungsstaetteId);
        return Response.noContent().build();
    }
}
