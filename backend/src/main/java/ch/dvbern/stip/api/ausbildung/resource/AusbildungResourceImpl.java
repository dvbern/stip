package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.generated.api.AusbildungResource;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungResourceImpl implements AusbildungResource {
    //    private final AusbildungService ausbildungService;

    @Override
    public Response createAusbildung(AusbildungUpdateDto ausbildungUpdateDto) {
        return null;
    }

    @Override
    public Response getAusbildung(UUID ausbildungId) {
        return null;
    }
}
