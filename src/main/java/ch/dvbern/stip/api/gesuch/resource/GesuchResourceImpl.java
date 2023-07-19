package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.*;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchResourceImpl implements GesuchResource {

    private final UriInfo uriInfo;
    private final GesuchService gesuchService;
    private final GesuchDokumentService gesuchDokumentService;

    @Override
    public Response createDokument(DokumentTyp dokumentTyp, UUID gesuchId, FileUpload file) {
        try {
            var dokument = gesuchDokumentService.uploadDokument(gesuchId, dokumentTyp, file);
            return Response.ok(dokument.getId()).build();
        } catch (IOException ioException) {
            return Response.serverError().build();
        }
    }

    @Override
    public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
        GesuchDto created = gesuchService.createGesuch(gesuchCreateDto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build()).build();
    }

    @Override
    public Response deleteDokument(UUID dokumentId, DokumentTyp dokumentTyp, UUID gesuchId) {
        return Response.noContent().build();
    }

    @Override
    public Response getDokument(UUID gesuchId, DokumentTyp dokumentTyp, UUID dokumentId) {
        DokumentDto dokument = gesuchDokumentService.findDokument(dokumentId).orElseThrow(NotFoundException::new);
        File nf = new File(dokument.getFilepfad() + dokument.getFilename());

        return ResponseBuilder
                .ok((Object) nf)
                .header("Content-Disposition", "attachment;filename=" + nf.getName())
                .build()
                .toResponse();
    }

    @Override
    public Response getDokumenteForTyp(DokumentTyp dokumentTyp, UUID gesuchId) {
        List<DokumentDto> dokumentDtoList = gesuchDokumentService.findGesuchDokumenteForTyp(gesuchId, dokumentTyp);
        return Response.ok(dokumentDtoList).build();
    }

    @Override
    public Response getGesuch(UUID gesuchId) {
        var gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(NotFoundException::new);
        return Response.ok(gesuch).build();
    }

    @Override
    public Response getGesuche() {
        return Response.ok(gesuchService.findAll()).build();
    }

    @Override
    public Response getGesucheForBenutzer(UUID benutzerId) {
        return Response.ok(gesuchService.findAllForBenutzer(benutzerId)).build();
    }

    @Override
    public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto);
        return Response.accepted().build();
    }
}
