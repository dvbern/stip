package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.exception.mapper.ValidationsExceptionMapper;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutiny.zero.flow.adapters.AdaptersToFlow;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.reactivestreams.Publisher;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchResourceImpl implements GesuchResource {

	private final UriInfo uriInfo;
	private final GesuchService gesuchService;
	private final GesuchDokumentService gesuchDokumentService;
	private final ConfigService configService;
	private final S3AsyncClient s3;

	private final ValidationsExceptionMapper validationsExceptionMapper;

	@Override
	public Uni<Response> createDokument(DokumentTyp dokumentTyp, UUID gesuchId, FileUpload fileUpload) {

		if (fileUpload.fileName() == null || fileUpload.fileName().isEmpty()) {
			return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
		}

		if (fileUpload.contentType() == null || fileUpload.contentType().isEmpty()) {
			return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
		}

		if (!FileUtil.checkFileExtensionAllowed(fileUpload.uploadedFile(), configService.getAllowedMimeTypes())) {
			return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
		}

		String objectId = FileUtil.generateUUIDWithFileExtension(fileUpload.fileName());
		return Uni.createFrom()
				.completionStage(() ->
						s3.putObject(
								gesuchDokumentService.buildPutRequest(
										fileUpload,
										configService.getBucketName(),
										objectId),
								AsyncRequestBody.fromFile(fileUpload.uploadedFile())))
				.onItem()
				.invoke(() -> gesuchDokumentService.uploadDokument(
						gesuchId,
						dokumentTyp,
						fileUpload,
						objectId))
				.onItem()
				.ignore()
				.andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
				.onFailure()
				.invoke(throwable -> LOG.error(throwable.getMessage()))
				.onFailure()
				.recoverWithItem(Response.serverError().build());
	}

	@Override
	public Response createGesuch(GesuchCreateDto gesuchCreateDto) {
		GesuchDto created = gesuchService.createGesuch(gesuchCreateDto);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build()).build();
	}

	@Override
	public Response deleteDokument(UUID dokumentId, DokumentTyp dokumentTyp, UUID gesuchId) {
		gesuchDokumentService.deleteDokument(dokumentId);
		return Response.noContent().build();
	}

	@Override
	public Response deleteGesuch(UUID gesuchId) {
		gesuchDokumentService.deleteAllDokumentForGesuch(gesuchId);
		gesuchService.deleteGesuch(gesuchId);
		return Response.noContent().build();
	}

	@Override
	public Response gesuchEinreichen(UUID gesuchId) {
		try {
			gesuchService.gesuchEinreichen(gesuchId);
			return Response.accepted().build();
		}
		catch (ValidationsException validationsException) {
			return Response.status(Status.BAD_REQUEST).entity(validationsExceptionMapper.toDto(validationsException)).build();
		}
	}

	@Override
	@Blocking
	public RestMulti<Buffer> getDokument(UUID gesuchId, DokumentTyp dokumentTyp, UUID dokumentId) {
		DokumentDto dokumentDto = gesuchDokumentService.findDokument(dokumentId).orElseThrow(NotFoundException::new);
		return RestMulti.fromUniResponse(
				Uni.createFrom().completionStage(() -> s3.getObject(
						gesuchDokumentService.buildGetRequest(
								configService.getBucketName(),
								dokumentDto.getObjectId()),
						AsyncResponseTransformer.toPublisher())),
				response -> Multi.createFrom()
						.safePublisher(AdaptersToFlow.publisher((Publisher<ByteBuffer>) response))
						.map(GesuchResourceImpl::toBuffer),
				response -> Map.of(
						"Content-Disposition",
						List.of("attachment;filename=" + dokumentDto.getFilename()),
						"Content-Type",
						List.of("application/octet-stream")));
	}

	private static Buffer toBuffer(ByteBuffer bytebuffer) {
		byte[] result = new byte[bytebuffer.remaining()];
		bytebuffer.get(result);
		return Buffer.buffer(result);
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
		try {
			gesuchService.updateGesuch(gesuchId, gesuchUpdateDto);
			return Response.accepted().build();
		}
		catch (ValidationsException validationsException) {
			return Response.serverError().entity(validationsExceptionMapper.toDto(validationsException)).build();
		}
	}
}
