package ch.dvbern.stip.api.gesuchsperioden.resource;

import java.util.UUID;

import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.api.GesuchsperiodeResource;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodeResourceImpl implements GesuchsperiodeResource {
    private final GesuchsperiodenService gesuchsperiodenService;

    @RolesAllowed(ROLE_ADMIN)
    @Override
    public Response createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        final var gesuchsperiode = gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
        return Response.ok(gesuchsperiode).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getAktiveGesuchsperioden() {
        final var activeGesuchsperioden = gesuchsperiodenService.getAllActive();
        return Response.ok(activeGesuchsperioden).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesuchsperiode(UUID gesuchsperiodeId) {
        final var gesuchsperiod = gesuchsperiodenService
            .getGesuchsperiode(gesuchsperiodeId)
            .orElseThrow(NotFoundException::new);

        return Response.ok(gesuchsperiod).build();
    }

    @RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
    @Override
    public Response getGesuchsperioden() {
        return Response.ok(gesuchsperiodenService.getAllGesuchsperioden()).build();
    }

    @RolesAllowed(ROLE_ADMIN)
    @Override
    public Response updateGesuchsperiode(UUID gesuchsperiodeId, GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto) {
        final var gesuchsperiode = gesuchsperiodenService
            .updateGesuchsperiode(gesuchsperiodeId, gesuchsperiodeUpdateDto);
        return Response.ok(gesuchsperiode).build();
    }
}
