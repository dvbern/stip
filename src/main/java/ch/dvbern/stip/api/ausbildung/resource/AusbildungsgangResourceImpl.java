package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.generated.api.AusbildungsgangResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangResourceImpl implements AusbildungsgangResource {

	@Override
	public Response getAusbildungsgang(UUID ausbildungsgangId) {
		return null;
	}
}
