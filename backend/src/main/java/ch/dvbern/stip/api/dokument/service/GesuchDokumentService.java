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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.exception.AppValidationMessage;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkiverse.antivirus.runtime.AntivirusScanResult;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentService {
    private static final String GESUCH_DOKUMENT_PATH = "gesuch/";
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final DokumentMapper dokumentMapper;
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final DokumentstatusService dokumentstatusService;
    private final Antivirus antivirus;

    @Transactional
    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentarsByGesuchDokumentId(
        UUID gesuchDokumentId,
        DokumentTyp dokumentTyp
    ) {
        return dokumentstatusService.getGesuchDokumentKommentareByGesuchAndType(gesuchDokumentId, dokumentTyp);
    }

    @Transactional
    public DokumentDto uploadDokument(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp,
        final FileUpload fileUpload,
        final String objectId
    ) {
        GesuchTranche gesuchTranche =
            gesuchTrancheRepository.findByIdOptional(gesuchTrancheId).orElseThrow(NotFoundException::new);
        GesuchDokument gesuchDokument =
            gesuchDokumentRepository.findByGesuchTrancheAndDokumentType(gesuchTranche.getId(), dokumentTyp)
                .orElseGet(
                    () -> createGesuchDokument(gesuchTranche, dokumentTyp)
                );
        Dokument dokument = new Dokument();
        dokument.getGesuchDokumente().add(gesuchDokument);
        gesuchDokument.getDokumente().add(dokument);
        dokument.setFilename(fileUpload.fileName());
        dokument.setObjectId(objectId);
        dokument.setFilesize(String.valueOf(fileUpload.size()));
        dokument.setFilepath(GESUCH_DOKUMENT_PATH);
        dokumentRepository.persist(dokument);

        return dokumentMapper.toDto(dokument);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForTyp(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp
    ) {
        final var gesuchDokument =
            gesuchDokumentRepository.findByGesuchTrancheAndDokumentType(gesuchTrancheId, dokumentTyp);
        final var dto = gesuchDokument.map(gesuchDokumentMapper::toDto).orElse(null);
        return new NullableGesuchDokumentDto(dto);
    }

    @Transactional
    public Optional<DokumentDto> findDokument(final UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = dokumentRepository.findById(dokumentId);
        return Optional.ofNullable(dokumentMapper.toDto(dokument));
    }

    public void removeAllGesuchDokumentsForGesuch(final UUID gesuchId) {
        gesuchRepository.requireById(gesuchId)
            .getGesuchTranchen()
            .forEach(
                gesuchTranche -> removeAllDokumentsForGesuchTranche(gesuchTranche.getId())
            );
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void removeAllDokumentsForGesuchTranche(final UUID gesuchTrancheId) {
        gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId)
            .forEach(
                gesuchDokument -> removeGesuchDokument(gesuchDokument.getId())
            );
    }

    private static void gesuchstatusIsNotOrElseThrow(final Gesuch gesuch, final Gesuchstatus statusToVerify) {
        if (gesuch.getGesuchStatus() != statusToVerify) {
            throw new IllegalStateException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Transactional
    public void gesuchDokumentAblehnen(final UUID gesuchDokumentId, final GesuchDokumentAblehnenRequestDto dto) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);

        gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuchTranche().getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        dokumentstatusService.triggerStatusChangeWithComment(
            gesuchDokument,
            DokumentstatusChangeEvent.ABGELEHNT,
            dto.getKommentar()
        );
    }

    @Transactional
    public void gesuchDokumentAkzeptieren(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuchTranche().getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        dokumentstatusService.triggerStatusChange(
            gesuchDokument,
            DokumentstatusChangeEvent.AKZEPTIERT
        );
    }

    public CompletableFuture<DeleteObjectsResponse> deleteDokumentsFromS3Blocking(final List<String> objectIds) {
        return s3.deleteObjects(
            buildDeleteObjectsRequest(
                configService.getBucketName(),
                objectIds
            )
        );
    }

    private DeleteObjectsRequest buildDeleteObjectsRequest(final String bucketName, final List<String> objectIds) {
        final var objectIdentifiers = objectIds.stream()
            .map(
                objectKey -> ObjectIdentifier.builder().key(GESUCH_DOKUMENT_PATH + objectKey).build()
            )
            .toList();
        return DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(deleteObjectContainer -> deleteObjectContainer.objects(objectIdentifiers))
            .build();
    }

    public void executeDeleteDokumentsFromS3(final List<String> objectIds) {
        Uni.createFrom()
            .item(deleteDokumentsFromS3Blocking(objectIds))
            .await()
            .indefinitely();
    }

    @Transactional
    public String deleteDokument(final UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();
        for (final var gesuchDokument : dokument.getGesuchDokumente()) {
            gesuchDokument.getDokumente().remove(dokument);
            gesuchDokumentRepository.dropGesuchDokumentIfNoDokumente(gesuchDokument.getId());
        }
        if (dokument.getGesuchDokumente().isEmpty()) {
            dokumentRepository.delete(dokument);
        }

        return dokumentObjectId;
    }

    @Transactional
    public void removeDokument(final UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();
        for (final var gesuchDokument : dokument.getGesuchDokumente()) {
            gesuchDokument.getDokumente().remove(dokument);
            gesuchDokumentRepository.dropGesuchDokumentIfNoDokumente(gesuchDokument.getId());
        }
        if (dokument.getGesuchDokumente().isEmpty()) {
            dokumentRepository.delete(dokument);
            executeDeleteDokumentsFromS3(List.of(dokumentObjectId));
        }
    }

    @Transactional
    public void removeGesuchDokument(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        removeGesuchDokument(gesuchDokument);
    }

    @Transactional
    public void removeGesuchDokument(final GesuchDokument gesuchDokument) {
        final var dokuments = gesuchDokument.getDokumente();

        List<String> dokumentObjectIds = new ArrayList<>();
        // Using Iterator to be able to remove while looping
        for (Iterator<Dokument> iterator = dokuments.iterator(); iterator.hasNext();) {
            final var dokument = iterator.next();
            iterator.remove();
            if (dokument.getGesuchDokumente().isEmpty()) {
                dokumentRepository.delete(dokument);
                dokumentObjectIds.add(dokument.getObjectId());
            }
        }
        if (gesuchDokument.getDokumente().isEmpty()) {
            gesuchDokumentRepository.delete(gesuchDokument);
        }
        if (!dokumentObjectIds.isEmpty()) {
            executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    @Transactional
    public void deleteAbgelehnteDokumenteForGesuch(final Gesuch gesuch) {
        // Query for these instead of iterating "in memory" because gesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var gesuchDokumente = gesuchDokumentRepository
            .getAllForGesuchInStatus(gesuch, Dokumentstatus.ABGELEHNT)
            .toList();

        final var dokumenteToDeleteFromS3 = new ArrayList<String>();
        for (var gesuchdokument : gesuchDokumente) {
            final var dokumentList = gesuchdokument.getDokumente().stream().toList();
            for (var dokument : dokumentList) {
                dokument.getGesuchDokumente().remove(gesuchdokument);
                gesuchdokument.getDokumente().remove(dokument);

                // If no other references to this physical document exist, delete it as well
                if (dokument.getGesuchDokumente().isEmpty()) {
                    dokumenteToDeleteFromS3.add(dokument.getObjectId());
                    dokumentRepository.delete(dokument);
                }
            }
            gesuchDokumentRepository.delete(gesuchdokument);
        }

        executeDeleteDokumentsFromS3(dokumenteToDeleteFromS3);
    }

    public void scanDokument(final FileUpload fileUpload) {
        try (
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
            IOUtils.toBufferedInputStream(Files.newInputStream(fileUpload.uploadedFile())).readAllBytes()
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

    public CompletableFuture<PutObjectResponse> getCreateDokumentFuture(
        final String objectId,
        final FileUpload fileUpload
    ) {
        return s3.putObject(
            buildPutRequest(
                fileUpload,
                configService.getBucketName(),
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
            .key(GESUCH_DOKUMENT_PATH + objectId)
            .contentType(fileUpload.contentType())
            .build();
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> getGetDokumentFuture(final String objectId) {
        return s3.getObject(
            buildGetRequest(
                configService.getBucketName(),
                objectId
            ),
            AsyncResponseTransformer.toPublisher()
        );
    }

    private GetObjectRequest buildGetRequest(final String bucketName, final String objectKey) {
        return GetObjectRequest.builder()
            .bucket(bucketName)
            .key(GESUCH_DOKUMENT_PATH + objectKey)
            .build();
    }

    public CompletableFuture<DeleteObjectResponse> getDeleteDokumentFuture(final String objectId) {
        return s3.deleteObject(
            buildDeleteObjectRequest(
                configService.getBucketName(),
                objectId
            )
        );
    }

    private DeleteObjectRequest buildDeleteObjectRequest(final String bucketName, final String objectKey) {
        return DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(GESUCH_DOKUMENT_PATH + objectKey)
            .build();
    }

    private GesuchDokument createGesuchDokument(final GesuchTranche gesuchTranche, final DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument =
            new GesuchDokument().setGesuchTranche(gesuchTranche).setDokumentTyp(dokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }
}
