package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangService;
import ch.dvbern.stip.generated.api.AusbildungsgangResource;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangResourceImpl implements AusbildungsgangResource {

	private final AusbildungsgangService ausbildungsgangService;

	@Override
	public Response createAusbildungsgang(AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
		return null;
	}

	@Override
	public Response deleteAusbildungsgang(UUID ausbildungsgangId) {
		return null;
	}

	@Override
	public Response getAusbildungsgang(UUID ausbildungsgangId) {
		return Response.ok(ausbildungsgangService.findById(ausbildungsgangId)).build();
	}

	@Override
	public Response updateAusbildungsgang(UUID ausbildungsgangId, AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
		return null;
	}
}
