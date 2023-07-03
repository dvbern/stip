package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.generated.api.AusbildungstaetteResource;
import ch.dvbern.stip.api.ausbildung.service.AusbildungstaetteService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungstaetteResourceImpl implements AusbildungstaetteResource {

    private final AusbildungstaetteService ausbildungstaetteService;

    @Override
    public Response getAusbildungstaetten() {
        return Response.ok(ausbildungstaetteService.getAusbildungsstaetten()).build();
    }
}
