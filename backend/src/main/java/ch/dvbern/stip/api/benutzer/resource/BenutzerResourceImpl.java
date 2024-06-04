package ch.dvbern.stip.api.benutzer.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenWorker;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import ch.dvbern.stip.generated.api.BenutzerResource;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerResourceImpl implements BenutzerResource {

    private final BenutzerService benutzerService;
    private final ZuordnungService zuordnungService;
    private final SachbearbeiterZuordnungStammdatenWorker worker;
    private final TenantService tenantService;

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    public Response createOrUpdateSachbearbeiterStammdaten(
        UUID benutzerId,
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(benutzerId, sachbearbeiterZuordnungStammdatenDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
        return Response.accepted().build();
    }

    @Override
    @RolesAllowed(STAMMDATEN_CREATE)
    public Response createOrUpdateSachbearbeiterStammdatenList(
        List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto
    ) {
        benutzerService.createOrUpdateSachbearbeiterStammdaten(sachbearbeiterZuordnungStammdatenListDto);
        worker.updateZuordnung(tenantService.getCurrentTenant().getIdentifier());
        return Response.accepted().build();
    }

    @Override
    public Response prepareCurrentBenutzer() {
        final var benutzer = benutzerService.getOrCreateAndUpdateCurrentBenutzer();
        return Response.ok(benutzer).build();
    }

    @Override
    public Response getSachbearbeitende() {
        return Response.ok(benutzerService.getAllSachbearbeitendeMitZuordnungStammdaten()).build();
    }

    @Override
    @RolesAllowed(STAMMDATEN_READ)
    public Response getSachbearbeiterStammdaten(UUID benutzerId) {
        SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto =
            benutzerService.findSachbearbeiterZuordnungStammdatenWithBenutzerId(benutzerId).orElseThrow(
                NotFoundException::new);
        return Response.ok(sachbearbeiterZuordnungStammdatenDto).build();
    }
}
