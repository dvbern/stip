package ch.dvbern.stip.api.benutzer.resource;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerResourceImpl implements BenutzerResource {

	private final BenutzerService benutzerService;

	@Override
	public Response getBenutzende() {
		return Response.ok(benutzerService.getAllBenutzer()).build();
	}

	@Override
	public Response getBenutzer(UUID benutzerId) {
		var benutzer = benutzerService
				.getBenutzer(benutzerId)
				.orElseThrow(NotFoundException::new);
		return Response.ok(benutzer).build();
	}
}
