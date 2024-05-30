package ch.dvbern.stip.api.dokument.resource;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.common.util.StringUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import ch.dvbern.stip.generated.dto.DokumentDto;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutiny.zero.flow.adapters.AdaptersToFlow;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class DokumentResourceImpl implements DokumentResource {
    private final GesuchDokumentService gesuchDokumentService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final BenutzerService benutzerService;

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getDokumenteForTyp(DokumentTyp dokumentTyp, UUID gesuchId) {
        List<DokumentDto> dokumentDtoList = gesuchDokumentService.findGesuchDokumenteForTyp(gesuchId, dokumentTyp);
        return Response.ok(dokumentDtoList).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public Uni<Response> createDokument(DokumentTyp dokumentTyp, UUID gesuchId, FileUpload fileUpload) {
        if (StringUtil.isNullOrEmpty(fileUpload.fileName()) || StringUtil.isNullOrEmpty(fileUpload.contentType())) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        if (!FileUtil.checkFileExtensionAllowed(fileUpload.uploadedFile(), configService.getAllowedMimeTypes())) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        String objectId = FileUtil.generateUUIDWithFileExtension(fileUpload.fileName());
        return Uni.createFrom()
            .completionStage(() -> gesuchDokumentService.getCreateDokumentFuture(objectId, fileUpload))
            .onItem()
            .invoke(() -> gesuchDokumentService.uploadDokument(
                gesuchId,
                dokumentTyp,
                fileUpload,
                objectId
            ))
            .onItem()
            .ignore()
            .andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
            .onFailure()
            .invoke(throwable -> LOG.error(throwable.getMessage()))
            .onFailure()
            .recoverWithItem(Response.serverError().build());
    }

    @Override
    @Blocking
    public RestMulti<Buffer> getDokument(String token) {
        JsonWebToken jwt;
        try {
            jwt = jwtParser.verify(token, configService.getSecret());
        } catch (ParseException e) {
            throw new UnauthorizedException();
        }

        final var dokumentId = UUID.fromString((String)
            jwt.claim(DokumentDownloadConstants.DOKUMENT_ID_CLAIM)
                .orElseThrow(BadRequestException::new)
        );

        final var dokumentDto = gesuchDokumentService.findDokument(dokumentId).orElseThrow(NotFoundException::new);
        return RestMulti.fromUniResponse(
            Uni.createFrom().completionStage(() -> gesuchDokumentService.getGetDokumentFuture(dokumentDto.getObjectId())),
            response -> Multi.createFrom()
                .safePublisher(AdaptersToFlow.publisher(response))
                .map(DokumentResourceImpl::toBuffer),
            response -> Map.of(
                "Content-Disposition",
                List.of("attachment;filename=" + dokumentDto.getFilename()),
                "Content-Type",
                List.of("application/octet-stream")
            )
        );
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public Response getDokumentDownloadToken(UUID gesuchId, DokumentTyp dokumentTyp, UUID dokumentId) {
        if (gesuchDokumentService.findDokument(dokumentId).isEmpty()) {
            throw new NotFoundException();
        }

        final var token = Jwt
            .claims()
            .upn(benutzerService.getCurrentBenutzername())
            .claim(DokumentDownloadConstants.DOKUMENT_ID_CLAIM, dokumentId.toString())
            .claim(DokumentDownloadConstants.GESUCH_ID_CLAIM, gesuchId.toString())
            .expiresIn(Duration.ofMinutes(configService.getExpiresInMinutes()))
            .issuer(configService.getIssuer())
            .jws()
            .signWithSecret(configService.getSecret());

        return Response.ok(token).build();
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    @Blocking
    public Uni<Response> deleteDokument(UUID dokumentId, DokumentTyp dokumentTyp, UUID gesuchId) {
        final var dokumentObjectId = gesuchDokumentService.deleteDokument(dokumentId);

        return Uni.createFrom().completionStage(() -> gesuchDokumentService.getDeleteDokumentFuture(dokumentObjectId))
            .onItem()
            .ignore()
            .andSwitchTo(Uni.createFrom().item(Response.noContent().build()))
            .onFailure()
            .recoverWithItem(Response.serverError().build());
    }

    private static Buffer toBuffer(ByteBuffer bytebuffer) {
        byte[] result = new byte[bytebuffer.remaining()];
        bytebuffer.get(result);
        return Buffer.buffer(result);
    }
}
