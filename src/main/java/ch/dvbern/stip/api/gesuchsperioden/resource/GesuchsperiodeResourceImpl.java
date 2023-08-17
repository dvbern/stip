package ch.dvbern.stip.api.gesuchsperioden.resource;

import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.api.GesuchsperiodeResource;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static ch.dvbern.stip.api.common.util.OidcConstants.*;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodeResourceImpl implements GesuchsperiodeResource {

    private final UriInfo uriInfo;
    private final GesuchsperiodenService gesuchsperiodenService;

    @RolesAllowed({ROLE_ADMIN})
    @Override
    public Response createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        var gesuchsperiode = gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(gesuchsperiode.getId().toString()).build()).build();
    }

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
    @Override
    public Response getAktiveGesuchsperioden() {
        var activeGesuchsperioden = gesuchsperiodenService.getAllActive();
        return Response.ok(activeGesuchsperioden).build();
    }

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
    @Override
    public Response getGesuchsperiode(UUID gesuchsperiodeId) {
        var gesuchsperiod = gesuchsperiodenService
                .getGesuchsperiode(gesuchsperiodeId)
                .orElseThrow(NotFoundException::new);

        return Response.ok(gesuchsperiod).build();
    }

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
    @Override
    public Response getGesuchsperioden() {
        return Response.ok(gesuchsperiodenService.getAllGesuchsperioden()).build();
    }
}
