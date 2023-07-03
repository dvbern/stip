package ch.dvbern.stip.api.gesuchsperioden.resource;

import ch.dvbern.stip.generated.api.GesuchsperiodeResource;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodeResourceImpl implements GesuchsperiodeResource {

    private final UriInfo uriInfo;
    private final GesuchsperiodenService gesuchsperiodenService;

    @Override
    public Response createGesuchsperiode(GesuchsperiodeCreateDto createGesuchsperiodeDto) {
        var gesuchsperiode = gesuchsperiodenService.createGesuchsperiode(createGesuchsperiodeDto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(gesuchsperiode.getId().toString()).build()).build();
    }

    @Override
    public Response getAktiveGesuchsperioden() {
        var activeGesuchsperioden = gesuchsperiodenService.getAllActive();
        return Response.ok(activeGesuchsperioden).build();
    }

    @Override
    public Response getGesuchsperiode(UUID gesuchsperiodeId) {
        var gesuchsperiod = gesuchsperiodenService
                .getGesuchsperiode(gesuchsperiodeId)
                .orElseThrow(NotFoundException::new);

        return Response.ok(gesuchsperiod).build();
    }

    @Override
    public Response getGesuchsperioden() {
        return Response.ok(gesuchsperiodenService.getAllGesuchsperioden()).build();
    }
}
