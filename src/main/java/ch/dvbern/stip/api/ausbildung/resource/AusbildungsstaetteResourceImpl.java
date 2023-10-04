package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsstaetteService;
import ch.dvbern.stip.generated.api.AusbildungsstaetteResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteResourceImpl implements AusbildungsstaetteResource {

    private final AusbildungsstaetteService ausbildungsstaetteService;

    @Override
    public Response getAusbildungsstaette(UUID ausbildungsstaetteId) {
        return Response.ok(ausbildungsstaetteService.findById(ausbildungsstaetteId)).build();
    }

    @Override
    public Response getAusbildungsstaetten() {
        return Response.ok(ausbildungsstaetteService.getAusbildungsstaetten()).build();
    }
}
