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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@ApplicationScoped
@UnlessBuildProfile("test")
public class DokumentDeleteService {
    public void executeDeleteDokumentFromS3(
        final S3AsyncClient s3,
        final String bucketName,
        final String objectId
    ) {
        executeDeleteDokumentsFromS3(s3, bucketName, List.of(objectId));
    }

    public void executeDeleteDokumentsFromS3(
        final S3AsyncClient s3,
        final String bucketName,
        final List<String> objectIds
    ) {
        Uni.createFrom()
            .item(deleteDokumentsFromS3Blocking(s3, bucketName, objectIds))
            .await()
            .indefinitely();
    }

    private CompletableFuture<DeleteObjectsResponse> deleteDokumentsFromS3Blocking(
        final S3AsyncClient s3,
        final String bucketName,
        final List<String> objectIds
    ) {
        return s3.deleteObjects(
            buildDeleteObjectsRequest(
                bucketName,
                objectIds
            )
        );
    }

    private DeleteObjectsRequest buildDeleteObjectsRequest(final String bucketName, final List<String> objectIds) {
        final var objectIdentifiers = objectIds.stream()
            .map(
                objectKey -> ObjectIdentifier.builder().key(objectKey).build()
            )
            .toList();

        return DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(deleteObjectContainer -> deleteObjectContainer.objects(objectIdentifiers))
            .build();
    }
}
