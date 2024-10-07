package ch.dvbern.stip.api.gesuchsjahr.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.generated.api.GesuchsjahrResource;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.STAMMDATEN_UPDATE;

@RequestScoped
@AllArgsConstructor
public class GesuchsjahrResourceImpl implements GesuchsjahrResource {
    private final GesuchsjahrService gesuchsjahrService;

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getGesuchsjahr(UUID gesuchsjahrId) {
        final var gesuchsjahr = gesuchsjahrService.getGesuchsjahr(gesuchsjahrId);
        return Response.ok(gesuchsjahr).build();
    }

    @RolesAllowed(STAMMDATEN_READ)
    @Override
    @AllowAll
    public Response getGesuchsjahre() {
        final var gesuchsjahre = gesuchsjahrService.getGesuchsjahre();
        return Response.ok(gesuchsjahre).build();
    }

    @RolesAllowed(STAMMDATEN_CREATE)
    @Override
    @AllowAll
    public Response createGesuchsjahr(GesuchsjahrCreateDto gesuchsjahrCreateDto) {
        final var gesuchsjahr = gesuchsjahrService.createGesuchsjahr(gesuchsjahrCreateDto);
        return Response.ok(gesuchsjahr).build();
    }

    @RolesAllowed(STAMMDATEN_UPDATE)
    @Override
    @AllowAll
    public Response updateGesuchsjahr(UUID gesuchsjahrId, GesuchsjahrUpdateDto gesuchsjahrUpdateDto) {
        final var gesuchsjahr = gesuchsjahrService.updateGesuchsjahr(gesuchsjahrId, gesuchsjahrUpdateDto);
        return Response.ok(gesuchsjahr).build();
    }

    @RolesAllowed(STAMMDATEN_UPDATE)
    @Override
    @AllowAll
    public Response publishGesuchsjahr(UUID gesuchsjahrId) {
        final var gesuchsjahr = gesuchsjahrService.publishGesuchsjahr(gesuchsjahrId);
        return Response.ok(gesuchsjahr).build();
    }

    @RolesAllowed(STAMMDATEN_DELETE)
    @Override
    @AllowAll
    public Response deleteGesuchsjahr(UUID gesuchsjahrId) {
        gesuchsjahrService.deleteGesuchsjahr(gesuchsjahrId);
        return Response.noContent().build();
    }
}
