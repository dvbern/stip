package ch.dvbern.stip.gesuch.resource;

import ch.dvbern.stip.dokument.entity.Dokument;
import ch.dvbern.stip.dokument.entity.DokumentTyp;
import ch.dvbern.stip.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.gesuch.entity.Gesuch;
import ch.dvbern.stip.gesuch.service.GesuchService;
import ch.dvbern.stip.shared.dto.ResponseId;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
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

    private final GesuchService gesuchService;

    private final GesuchDokumentService gesuchDokumentService;

    @Override
    public Response createDokument(DokumentTyp dokumentTyp, UUID gesuchId, FileUpload file) {
        Dokument dokument;
        Response response;
        try {
            dokument = gesuchDokumentService.uploadDokument(gesuchId, dokumentTyp, file);
            response = Response.ok(dokument.getId()).build();
        }
        catch (IOException ioException) {
            response = Response.serverError().build();
        }
        return response;
    }

    @Override
    public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
        Gesuch created = gesuchService.createGesuch(gesuchCreateDto);
        return Response.ok(new ResponseId(created.getId())).build();
    }

    @Override
    public Response deleteDokument(UUID dokumentId, DokumentTyp dokumentTyp, UUID gesuchId) {
        return Response.noContent().build();
    }

    @Override
    public Response getDokument(UUID gesuchId, DokumentTyp dokumentTyp, UUID dokumentId) {
        Dokument dokument = gesuchDokumentService.findDokument(dokumentId).orElseThrow(NotFoundException::new);
        File nf = new File(dokument.getFilepfad() + dokument.getFilename());
        ResponseBuilder response = ResponseBuilder.ok((Object) nf);
        response.header("Content-Disposition", "attachment;filename=" + nf.getName());
        return response.build().toResponse();
    }


    @Override
    public Response getDokumenteForTyp(DokumentTyp dokumentTyp, UUID gesuchId) {
        List<DokumentDto> dokumentDtoList = gesuchDokumentService.findGesuchDokumenteForTyp(gesuchId, dokumentTyp);
        return Response.ok(dokumentDtoList).build();
    }

    @Override
    public Response getGesuch(UUID gesuchId) {
        return gesuchService.findGesuch(gesuchId).map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @Override
    public Response getGesuche() {
        return Response.ok(gesuchService.findAll()).build();
    }

    @Override
    public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        Gesuch changed = gesuchService.saveGesuch(gesuchUpdateDto);
        return Response.ok(new ResponseId(changed.getId())).build();
    }


}
