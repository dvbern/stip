package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
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

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getAllTranchenForGesuch(UUID gesuchId) {
        final var tranchenDtos = gesuchTrancheService.getAllTranchenForGesuch(gesuchId);
        return Response.ok(tranchenDtos).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response createGesuchTrancheCopy(
        UUID gesuchId,
        CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto
    ) {
        final var gesuchDto = gesuchTrancheService.createTrancheCopy(
            gesuchId,
            createGesuchTrancheRequestDto
        );
        return Response.ok(gesuchDto).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuchDokumente(UUID gesuchTrancheId) {
        var gesuchDokumente = gesuchTrancheService.getAndCheckGesuchDokumentsForGesuchTranche(gesuchTrancheId);
        return Response.ok(gesuchDokumente).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuchDokument(UUID gesuchTrancheId, DokumentTyp dokumentTyp) {
        final var gesuchDokument = gesuchTrancheService.getGesuchDokument(gesuchTrancheId, dokumentTyp);
        return Response.ok(gesuchDokument).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getRequiredGesuchDokumentTyp(UUID gesuchTrancheId) {
        final var requiredTypes = gesuchTrancheService.getRequiredDokumentTypes(gesuchTrancheId);
        return Response.ok(requiredTypes).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGsTrancheChanges(UUID aenderungId) {
        final var changes = gesuchTrancheService.getGsTrancheChanges(aenderungId);
        return Response.ok(changes).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getSbTrancheChanges(UUID aenderungId) {
        final var changes = gesuchTrancheService.getSbTrancheChanges(aenderungId);
        return Response.ok(changes).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response aenderungEinreichen(UUID aenderungId) {
        gesuchTrancheService.aenderungEinreichen(aenderungId);
        return Response.ok().build();
    }
}
