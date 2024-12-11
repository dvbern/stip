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

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import ch.dvbern.stip.api.config.service.ConfigService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@UtilityClass
public class DokumentUploadUtil {
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
        return Uni.createFrom()
            .completionStage(
                () -> getUploadDokumentFuture(
                    s3,
                    fileUpload,
                    configService.getBucketName(),
                    key
                )
            )
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

    public boolean checkFileUpload(final FileUpload fileUpload, final ConfigService configService) {
        if (StringUtil.isNullOrEmpty(fileUpload.fileName()) || StringUtil.isNullOrEmpty(fileUpload.contentType())) {
            return false;
        }

        if (!FileUtil.checkFileExtensionAllowed(fileUpload.uploadedFile(), configService.getAllowedMimeTypes())) {
            return false;
        }

        return true;
    }

    private CompletableFuture<PutObjectResponse> getUploadDokumentFuture(
        final S3AsyncClient s3,
        final FileUpload fileUpload,
        final String bucketName,
        final String objectId
    ) {
        return s3.putObject(
            buildPutRequest(
                fileUpload,
                bucketName,
                objectId
            ),
            AsyncRequestBody.fromFile(fileUpload.uploadedFile())
        );
    }

    private PutObjectRequest buildPutRequest(
        final FileUpload fileUpload,
        final String bucketName,
        final String objectId
    ) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectId)
            .contentType(fileUpload.contentType())
            .build();
    }
}
