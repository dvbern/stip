package ch.dvbern.stip.api.benutzer.resource;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerResourceImpl implements BenutzerResource {

    private final BenutzerService benutzerService;

    @Override
    public Response getCurrentBenutzer() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return Response.ok(benutzer).build();
    }

    @Override
    public Response updateCurrentBenutzer(BenutzerUpdateDto benutzerUpdateDto) {
        benutzerService.updateCurrentBenutzer(benutzerUpdateDto);
        return Response.accepted().build();
    }
}
