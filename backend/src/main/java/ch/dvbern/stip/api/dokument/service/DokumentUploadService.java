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

package ch.dvbern.stip.api.dokument.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.exception.AppValidationMessage;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.common.util.StringUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkiverse.antivirus.runtime.AntivirusScanResult;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@ApplicationScoped
@UnlessBuildProfile("test")
@AllArgsConstructor
public class DokumentUploadService {
    protected static final String DOCUMENT_CONTENT_TYPE = "application/pdf";

    public Uni<Response> validateScanUploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final Antivirus antivirus,
        final Set<String> allowedMimetypes,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback,
        final @Nullable Consumer<Throwable> onFailure
    ) {
        if (!checkFileUpload(fileUpload, allowedMimetypes)) {
            throw new BadRequestException(
                "Incompatible mimetype, allowed types are: %s".formatted(
                    String.join(
                        ",",
                        allowedMimetypes
                    )
                )
            );
        }

        scanDokument(antivirus, fileUpload);

        return uploadDokument(
            fileUpload,
            s3,
            configService,
            dokumentPathPrefix,
            serviceCallback,
            onFailure
        );
    }

    public Uni<Response> validateScanUploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final Antivirus antivirus,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback,
        final @Nullable Consumer<Throwable> onFailure
    ) {
        return validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            configService.getAllowedMimeTypes(),
            dokumentPathPrefix,
            serviceCallback,
            onFailure
        );
    }

    public Uni<Response> validateScanUploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final Antivirus antivirus,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback
    ) {
        if (!checkFileUpload(fileUpload, configService.getAllowedMimeTypes())) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        scanDokument(antivirus, fileUpload);

        return uploadDokument(
            fileUpload,
            s3,
            configService,
            dokumentPathPrefix,
            serviceCallback
        );
    }

    public Uni<Response> uploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback,
        final @Nullable Consumer<Throwable> onFailure
    ) {
        final var objectId = FileUtil.generateUUIDWithFileExtension(fileUpload.fileName());
        final var key = dokumentPathPrefix + objectId;

        final Supplier<CompletionStage<PutObjectResponse>> stageSupplier = () -> getUploadDokumentFuture(
            s3,
            fileUpload,
            configService.getBucketName(),
            key
        );

        return Uni.createFrom()
            .completionStage(stageSupplier)
            .onItem()
            .invoke(() -> serviceCallback.accept(objectId))
            .onItem()
            .ignore()
            .andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
            .onFailure()
            .invoke(throwable -> {
                if (onFailure != null) {
                    onFailure.accept(throwable);
                }
            })
            .onFailure()
            .recoverWithItem(Response.serverError().build());
    }

    public Uni<Response> uploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback
    ) {
        final var objectId = FileUtil.generateUUIDWithFileExtension(fileUpload.fileName());
        final var key = dokumentPathPrefix + objectId;

        final Supplier<CompletionStage<PutObjectResponse>> stageSupplier = () -> getUploadDokumentFuture(
            s3,
            fileUpload,
            configService.getBucketName(),
            key
        );

        return Uni.createFrom()
            .completionStage(stageSupplier)
            .onItem()
            .invoke(() -> serviceCallback.accept(objectId))
            .onItem()
            .ignore()
            .andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
            .onFailure()
            .transform(InternalServerErrorException::new);
    }

    public String executeUploadDocument(
        final byte[] byteArray,
        final String fileName,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String documentPathPrefix
    ) {
        final var objectId = FileUtil.generateUUIDWithFileExtension(fileName);
        final var key = documentPathPrefix + objectId;

        Uni.createFrom()
            .completionStage(getUploadDokumentFuture(s3, byteArray, configService.getBucketName(), key))
            .await()
            .indefinitely();

        return objectId;
    }

    protected boolean checkFileUpload(final FileUpload fileUpload, final Set<String> allowedMimeTypes) {
        if (StringUtil.isNullOrEmpty(fileUpload.fileName()) || StringUtil.isNullOrEmpty(fileUpload.contentType())) {
            return false;
        }

        return FileUtil.checkFileExtensionAllowed(fileUpload.uploadedFile(), allowedMimeTypes);
    }

    protected void scanDokument(final Antivirus antivirus, final FileUpload fileUpload) {
        try (
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                IOUtils.toBufferedInputStream(Files.newInputStream(fileUpload.uploadedFile()))
                    .readAllBytes()
            )
        ) {
            // scan the file and check the results
            List<AntivirusScanResult> results = antivirus.scan(fileUpload.fileName(), inputStream);
            for (AntivirusScanResult result : results) {
                if (result.getStatus() != Response.Status.OK.getStatusCode()) {
                    LOG.warn(
                        "bad signature detected in file={} message={}",
                        fileUpload.fileName(),
                        result.getMessage()
                    );
                    throw AppValidationMessage.badSignatureDetectedInUpload().create();
                }
            }

            inputStream.reset();
        } catch (IOException e) {
            LOG.error("could not scan document", e);
            throw AppFailureMessage.internalError("could not scan file upload").create();
        }
    }

    private CompletableFuture<PutObjectResponse> getUploadDokumentFuture(
        final S3AsyncClient s3,
        final byte[] byteArray,
        final String bucketName,
        final String objectId
    ) {
        return s3.putObject(
            buildPutRequest(DOCUMENT_CONTENT_TYPE, bucketName, objectId),
            AsyncRequestBody.fromBytes(byteArray)
        );
    }

    private CompletableFuture<PutObjectResponse> getUploadDokumentFuture(
        final S3AsyncClient s3,
        final FileUpload fileUpload,
        final String bucketName,
        final String objectId
    ) {
        return s3.putObject(
            buildPutRequest(fileUpload.contentType(), bucketName, objectId),
            AsyncRequestBody.fromFile(fileUpload.uploadedFile())
        );
    }

    private PutObjectRequest buildPutRequest(final String contentType, final String bucketName, final String objectId) {
        return PutObjectRequest.builder().bucket(bucketName).key(objectId).contentType(contentType).build();
    }
}
