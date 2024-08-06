package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.service.GesuchTrancheService;
import ch.dvbern.stip.generated.api.GesuchTrancheResource;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheResourceImpl implements GesuchTrancheResource {
    private final GesuchTrancheService gesuchTrancheService;

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response createAenderungsantrag(UUID gesuchId, CreateAenderungsantragRequestDto aenderungsantragCreateDto) {
        final var gesuchDto = gesuchTrancheService.createAenderungsantrag(gesuchId, aenderungsantragCreateDto);
        return Response.ok(gesuchDto).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getAenderungsantrag(UUID gesuchId) {
        final var gesuchDto = gesuchTrancheService.getAenderungsantrag(gesuchId);
        return Response.ok(gesuchDto).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response createGesuchTrancheCopy(
        UUID gesuchId,
        UUID trancheId,
        CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto
    ) {
        final var gesuchDto = gesuchTrancheService.createTrancheCopy(
            gesuchId,
            trancheId,
            createGesuchTrancheRequestDto
        );
        return Response.ok(gesuchDto).build();
    }
}
