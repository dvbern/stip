package ch.dvbern.stip.ausbildung.resource;

import ch.dvbern.stip.generated.api.AusbildungstaetteResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungstaetteResourceImpl implements AusbildungstaetteResource {

    @Override
    public Response getAusbildungstaetten() {
        return null;
    }
}
