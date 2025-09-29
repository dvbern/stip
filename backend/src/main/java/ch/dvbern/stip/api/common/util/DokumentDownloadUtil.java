/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.util;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import mutiny.zero.flow.adapters.AdaptersToFlow;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@UtilityClass
public class DokumentDownloadUtil {
    public RestMulti<ByteArrayOutputStream> getWrapedDokument(
        final String fileName,
        final Supplier<CompletionStage<ByteArrayOutputStream>> generateDocumentTask
    ) {

        return RestMulti.fromUniResponse(
            Uni.createFrom().completionStage(generateDocumentTask.get()),
            s -> Multi.createFrom().completionStage(generateDocumentTask.get()),
            s -> getRequiredHeaders(fileName),
            s -> Response.Status.OK.getStatusCode()
        );
    }

    public RestMulti<Buffer> getDokument(
        final S3AsyncClient s3,
        final String bucketName,
        final String objectId,
        final String dokumentPathPrefix,
        final String fileName
    ) {

        final Supplier<CompletionStage<ResponsePublisher<GetObjectResponse>>> stageSupplier =
            () -> getDownloadDokumentFuture(
                s3,
                bucketName,
                dokumentPathPrefix + objectId
            );

        return RestMulti.fromUniResponse(
            Uni.createFrom()
                .completionStage(stageSupplier),
            response -> Multi.createFrom()
                .safePublisher(AdaptersToFlow.publisher(response))
                .map(byteBuffer -> {
                    byte[] result = new byte[byteBuffer.remaining()];
                    byteBuffer.get(result);
                    return Buffer.buffer(result);
                }),
            response -> getRequiredHeaders(fileName)
        );
    }

    private Map getRequiredHeaders(final String fileName) {
        return Map.of(
            "Content-Disposition",
            List.of("attachment;filename=" + fileName),
            "Content-Type",
            List.of("application/octet-stream")
        );
    }

    public FileDownloadTokenDto getFileDownloadToken(
        final UUID id,
        final String idClaim,
        final BenutzerService benutzerService,
        final ConfigService configService
    ) {
        return new FileDownloadTokenDto()
            .token(
                Jwt.claims()
                    .upn(benutzerService.getCurrentBenutzername())
                    .claim(idClaim, id.toString())
                    .expiresIn(Duration.ofMinutes(configService.getExpiresInMinutes()))
                    .issuer(configService.getIssuer())
                    .jws()
                    .signWithSecret(configService.getSecret())
            );
    }

    public UUID getClaimId(
        final JWTParser jwtParser,
        final String jwtString,
        final String secret,
        final String idClaim
    ) {
        JsonWebToken jwt;
        try {
            jwt = jwtParser.verify(jwtString, secret);
        } catch (ParseException e) {
            throw new UnauthorizedException();
        }

        final var idString = (String) jwt.claim(idClaim).orElseThrow(BadRequestException::new);

        return UUID.fromString(idString);
    }

    private CompletableFuture<ResponsePublisher<GetObjectResponse>> getDownloadDokumentFuture(
        final S3AsyncClient s3,
        final String bucketName,
        final String objectId
    ) {
        // TODO objectKey needs to prefixed with "PATH/..."
        return s3.getObject(buildGetRequest(bucketName, objectId), AsyncResponseTransformer.toPublisher());
    }

    private GetObjectRequest buildGetRequest(final String bucketName, final String objectId) {
        return GetObjectRequest.builder().bucket(bucketName).key(objectId).build();
    }
}
