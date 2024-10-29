package ch.dvbern.stip.api.gesuchsperioden.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.api.GesuchsperiodeResource;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.NullableGesuchsperiodeWithDatenDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodeResourceImpl implements GesuchsperiodeResource {
    private final GesuchsperiodenService gesuchsperiodenService;

    @RolesAllowed(STAMMDATEN_CREATE)
    @Override
    @AllowAll
    public Response createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        final var gesuchsperiode = gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
        return Response.ok(gesuchsperiode).build();
    }

    @RolesAllowed(STAMMDATEN_DELETE)
    @Override
    @AllowAll
    public Response deleteGesuchsperiode(UUID gesuchsperiodeId) {
        gesuchsperiodenService.deleteGesuchsperiode(gesuchsperiodeId);
        return Response.noContent().build();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getAktiveGesuchsperioden() {
        final var activeGesuchsperioden = gesuchsperiodenService.getAllActive();
        return Response.ok(activeGesuchsperioden).build();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getGesuchsperiode(UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodenService
            .getGesuchsperiode(gesuchsperiodeId)
            .orElseThrow(NotFoundException::new);

        return Response.ok(gesuchsperiode).build();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getGesuchsperioden() {
        return Response.ok(gesuchsperiodenService.getAllGesuchsperioden()).build();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getLatest() {
        final var gesuchsperiode = gesuchsperiodenService.getLatest();
        final var wrapped = new NullableGesuchsperiodeWithDatenDto(gesuchsperiode);
        return Response.ok(wrapped).build();
    }

    @Override
    @AllowAll
    @RolesAllowed(STAMMDATEN_UPDATE)
    public Response publishGesuchsperiode(UUID gesuchperiodeId) {
        final var gesuchsperiode = gesuchsperiodenService.publishGesuchsperiode(gesuchperiodeId);
        return Response.ok(gesuchsperiode).build();
    }

    @RolesAllowed(STAMMDATEN_UPDATE)
    @Override
    @AllowAll
    public Response updateGesuchsperiode(UUID gesuchsperiodeId, GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto) {
        final var gesuchsperiode = gesuchsperiodenService
            .updateGesuchsperiode(gesuchsperiodeId, gesuchsperiodeUpdateDto);
        return Response.ok(gesuchsperiode).build();
    }
}
