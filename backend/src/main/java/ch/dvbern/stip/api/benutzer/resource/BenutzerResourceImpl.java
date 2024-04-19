package ch.dvbern.stip.api.benutzer.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerResourceImpl implements BenutzerResource {

    private final BenutzerService benutzerService;

    @Override
    public Response createOrUpdateSachbearbeiterStammdaten(
        UUID benutzerId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(benutzerId, sachbearbeiterZuordnungStammdatenDto);
        return Response.accepted().build();
    }

    @Override
    public Response createOrUpdateSachbearbeiterStammdatenList(
        List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto
    ) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(sachbearbeiterZuordnungStammdatenListDto);
        return Response.accepted().build();
    }

    @Override
    public Response getCurrentBenutzer() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return Response.ok(benutzer).build();
    }

    @Override
    public Response getSachbearbeitende() {
        return Response.ok(benutzerService.getAllSachbearbeitendeMitZuordnungStammdaten()).build();
    }

    @Override
    public Response getSachbearbeiterStammdaten(UUID benutzerId) {
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto =
            benutzerService.findSachbearbeiterZuordnungStammdatenWithBenutzerId(benutzerId).orElseThrow(
                NotFoundException::new);
        return Response.ok(sachbearbeiterZuordnungStammdatenDto).build();
    }

    @Override
    public Response updateCurrentBenutzer(BenutzerUpdateDto benutzerUpdateDto) {
        benutzerService.updateCurrentBenutzer(benutzerUpdateDto);
        return Response.accepted().build();
    }
}
