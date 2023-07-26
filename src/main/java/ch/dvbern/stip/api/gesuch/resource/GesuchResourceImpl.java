package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.*;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import io.vertx.core.buffer.Buffer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchResourceImpl implements GesuchResource {

	private final UriInfo uriInfo;
	private final GesuchService gesuchService;
	private final GesuchDokumentService gesuchDokumentService;
	private final ConfigService configService;
	private final S3Client s3;

	@Override
	public Response createDokument(DokumentTyp dokumentTyp, UUID gesuchId, FileUpload fileUpload) {

		if (fileUpload.fileName() == null || fileUpload.fileName().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (fileUpload.contentType() == null || fileUpload.contentType().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		PutObjectResponse putResponse = s3.putObject(
				gesuchDokumentService.buildPutRequest(fileUpload, configService.getBucketName()),
				RequestBody.fromFile(fileUpload.uploadedFile()));
		if (putResponse != null) {
			return Response.ok(gesuchDokumentService.uploadDokument(gesuchId, dokumentTyp, fileUpload)).build();
		} else {
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
	public Response deleteGesuch(UUID gesuchId) {
		gesuchService.deleteGesuch(gesuchId);
		return Response.noContent().build();
	}

	@Override
	public Response getDokument(UUID gesuchId, DokumentTyp dokumentTyp, UUID dokumentId) {
		DokumentDto dokument = gesuchDokumentService.findDokument(dokumentId).orElseThrow(NotFoundException::new);

		ResponseBytes<GetObjectResponse> objectBytes =
				s3.getObjectAsBytes(gesuchDokumentService.buildGetRequest(
						dokument.getFilename(),
						configService.getBucketName()));
		ResponseBuilder response = Response.ok(objectBytes.asByteArray());
		response.header("Content-Disposition", "attachment;filename=" + dokument.getFilename());
		response.header("Content-Type", objectBytes.response().contentType());
		return response.build();
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
	public Response getGesucheForFall(UUID fallId) {
		return Response.ok(gesuchService.findAllForFall(fallId)).build();
	}

	@Override
	public Response updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
		gesuchService.updateGesuch(gesuchId, gesuchUpdateDto);
		return Response.accepted().build();
	}

	private static Buffer toBuffer(ByteBuffer bytebuffer) {
		byte[] result = new byte[bytebuffer.remaining()];
		bytebuffer.get(result);
		return Buffer.buffer(result);
	}
}
