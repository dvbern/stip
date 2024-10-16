package ch.dvbern.stip.api.dokument.resource;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.common.util.StringUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
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

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;
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
    @AllowAll
    public Response getGesuchDokumenteForTyp(DokumentTyp dokumentTyp, UUID gesuchTrancheId) {
        final var gesuchDokument = gesuchDokumentService.findGesuchDokumentForTyp(gesuchTrancheId, dokumentTyp);
        return Response.ok(gesuchDokument).build();
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    @AllowAll
    public Response getGesuchDokumentKommentare(DokumentTyp dokumentTyp, UUID gesuchId) {
        return Response.ok()
            .entity(gesuchDokumentService.getGesuchDokumentKommentarsByGesuchDokumentId(gesuchId, dokumentTyp)).build();
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    @AllowAll
    public Uni<Response> createDokument(DokumentTyp dokumentTyp, UUID gesuchTrancheId, FileUpload fileUpload) {
        if (StringUtil.isNullOrEmpty(fileUpload.fileName()) || StringUtil.isNullOrEmpty(fileUpload.contentType())) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        if (!FileUtil.checkFileExtensionAllowed(fileUpload.uploadedFile(), configService.getAllowedMimeTypes())) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        gesuchDokumentService.scanDokument(fileUpload);

        String objectId = FileUtil.generateUUIDWithFileExtension(fileUpload.fileName());
        return Uni.createFrom()
            .completionStage(() -> gesuchDokumentService.getCreateDokumentFuture(objectId, fileUpload))
            .onItem()
            .invoke(() -> gesuchDokumentService.uploadDokument(
                gesuchTrancheId,
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
    @AllowAll
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
            Uni.createFrom()
                .completionStage(() -> gesuchDokumentService.getGetDokumentFuture(dokumentDto.getObjectId())),
            response -> Multi.createFrom()
                .safePublisher(AdaptersToFlow.publisher(response))
                .map(byteBuffer -> {
                    byte[] result = new byte[byteBuffer.remaining()];
                    byteBuffer.get(result);
                    return Buffer.buffer(result);
                }),
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
    @AllowAll
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
    @AllowAll
    @Blocking
    public Response deleteDokument(UUID dokumentId, DokumentTyp dokumentTyp, UUID gesuchId) {
        gesuchDokumentService.removeDokument(dokumentId);
        return Response.ok().build();
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Override
    @AllowAll
    public Response gesuchDokumentAblehnen(
        UUID gesuchDokumentId,
        GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto
    ) {
        gesuchDokumentService.gesuchDokumentAblehnen(gesuchDokumentId, gesuchDokumentAblehnenRequestDto);
        return Response.ok().build();
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Override
    @AllowAll
    public Response gesuchDokumentAkzeptieren(UUID gesuchDokumentId) {
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokumentId);
        return Response.ok().build();
    }
}
