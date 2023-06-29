package ch.dvbern.stip.ausbildung.resource;

import ch.dvbern.stip.ausbildung.service.AusbildungstaetteService;
import ch.dvbern.stip.generated.api.AusbildungstaetteResource;
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
