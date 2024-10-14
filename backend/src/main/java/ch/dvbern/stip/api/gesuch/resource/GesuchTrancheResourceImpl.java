package ch.dvbern.stip.api.gesuch.resource;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheService;
import ch.dvbern.stip.generated.api.GesuchTrancheResource;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
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
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response createAenderungsantrag(UUID gesuchId, CreateAenderungsantragRequestDto createAenderungsantragRequestDto) {
        gesuchAuthorizer.canUpdate(gesuchId, true);
        final var trancheDto = gesuchTrancheService.createAenderungsantrag(gesuchId, createAenderungsantragRequestDto);
        return Response.ok(trancheDto).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getAllTranchenForGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        final var tranchenDtos = gesuchTrancheService.getAllTranchenForGesuch(gesuchId);
        return Response.ok(tranchenDtos).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response createGesuchTrancheCopy(
        UUID gesuchId,
        CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto
    ) {
        gesuchAuthorizer.canUpdate(gesuchId);
        final var trancheDto = gesuchTrancheService.createTrancheCopy(
            gesuchId,
            createGesuchTrancheRequestDto
        );
        return Response.ok(trancheDto).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response deleteAenderung(UUID aenderungId) {
        gesuchTrancheAuthorizer.canDeleteAenderung(aenderungId);
        gesuchTrancheService.deleteAenderung(aenderungId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuchDokumente(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        var gesuchDokumente = gesuchTrancheService.getAndCheckGesuchDokumentsForGesuchTranche(gesuchTrancheId);
        return Response.ok(gesuchDokumente).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getGesuchDokument(UUID gesuchTrancheId, DokumentTyp dokumentTyp) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        final var gesuchDokument = gesuchTrancheService.getGesuchDokument(gesuchTrancheId, dokumentTyp);
        return Response.ok(gesuchDokument).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getRequiredGesuchDokumentTyp(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        final var requiredTypes = gesuchTrancheService.getRequiredDokumentTypes(gesuchTrancheId);
        return Response.ok(requiredTypes).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response validateGesuchTranchePages(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        return Response.ok(
            gesuchTrancheService.validatePages(gesuchTrancheId)
        ).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response aenderungEinreichen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        gesuchTrancheService.aenderungEinreichen(aenderungId);
        return Response.ok().build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response gesuchTrancheEinreichenValidieren(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdate(gesuchTrancheId);
        final var validationReport = gesuchTrancheService.einreichenValidieren(gesuchTrancheId);
        return Response.ok(validationReport).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response aenderungAkzeptieren(UUID aenderungId) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        final var newTranche = gesuchTrancheService.aenderungAkzeptieren(aenderungId);
        return Response.ok(newTranche).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response aenderungAblehnen(UUID aenderungId, KommentarDto kommentarDto) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        final var tranche = gesuchTrancheService.aenderungAblehnen(aenderungId, kommentarDto);
        return Response.ok(tranche).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Response aenderungManuellAnpassen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        final var tranche = gesuchTrancheService.aenderungManuellAnpassen(aenderungId);
        return Response.ok(tranche).build();
    }
}
