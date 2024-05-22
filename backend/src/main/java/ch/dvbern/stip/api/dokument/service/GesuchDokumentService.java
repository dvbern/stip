package ch.dvbern.stip.api.dokument.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.DokumentDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentService {

    private static final String GESUCH_DOKUMENT_PATH = "gesuch/";
    private final DokumentMapper dokumentMapper;
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchRepository gesuchRepository;
    private final S3AsyncClient s3;
    private final ConfigService configService;

    @Transactional
    public DokumentDto uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, FileUpload fileUpload, String objectId) {
        Gesuch gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
        GesuchDokument gesuchDokument =
            gesuchDokumentRepository.findByGesuchAndDokumentType(gesuch.getId(), dokumentTyp).orElseGet(
                () -> createGesuchDokument(gesuch, dokumentTyp)
            );
        Dokument dokument = new Dokument();
        dokument.setGesuchDokument(gesuchDokument);
        dokument.setFilename(fileUpload.fileName());
        dokument.setObjectId(objectId);
        dokument.setFilesize(String.valueOf(fileUpload.size()));
        dokument.setFilepath(GESUCH_DOKUMENT_PATH);
        dokumentRepository.persist(dokument);

        return dokumentMapper.toDto(dokument);
    }

    private GesuchDokument createGesuchDokument(Gesuch gesuch, DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument = new GesuchDokument().setGesuch(gesuch).setDokumentTyp(dokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }

    @Transactional
    public List<DokumentDto> findGesuchDokumenteForTyp(UUID gesuchId, DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument =
            gesuchDokumentRepository.findByGesuchAndDokumentType(gesuchId, dokumentTyp).orElse(null);
        if (gesuchDokument == null) {
            return new ArrayList<>();
        }
        return gesuchDokument.getDokumente().stream().map(dokumentMapper::toDto).toList();
    }

    @Transactional
    public Optional<DokumentDto> findDokument(UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = dokumentRepository.findById(dokumentId);
        return Optional.ofNullable(dokumentMapper.toDto(dokument));
    }

    private PutObjectRequest buildPutRequest(FileUpload fileUpload, String bucketName, String objectId) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(GESUCH_DOKUMENT_PATH + objectId)
            .contentType(fileUpload.contentType())
            .build();
    }

    private GetObjectRequest buildGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
            .bucket(bucketName)
            .key(GESUCH_DOKUMENT_PATH + objectKey)
            .build();
    }

    private DeleteObjectRequest buildDeleteObjectRequest(String bucketName, String objectKey) {
        return DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(GESUCH_DOKUMENT_PATH + objectKey)
            .build();
    }

    private DeleteObjectsRequest buildDeleteObjectsRequest(String bucketName, List<String> objectIds) {
        final var objectIdentifiers = objectIds.stream().map(
            objectKey -> ObjectIdentifier.builder().key(GESUCH_DOKUMENT_PATH + objectKey).build()
        ).toList();
        return DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(deleteObjectContainer -> deleteObjectContainer.objects(objectIdentifiers))
            .build();
    }

    @Transactional
    public void deleteAllDokumentForGesuchInRepository(UUID gesuchId) {
        gesuchDokumentRepository.findAllForGesuch(gesuchId)
            .forEach(gesuchDokumentRepository::delete);
    }

    @Transactional
    public List<String> getAllDokumentObjectIdsForGesuch(UUID gesuchId) {
        return gesuchDokumentRepository.findAllForGesuch(gesuchId)
            .map(GesuchDokument::getDokumente)
            .flatMap(dokuments -> dokuments
                .stream()
                .map(Dokument::getObjectId)
            ).toList();
    }

    public void deleteAllDokumentForGesuch(UUID gesuchId) {
        executeDeleteDokumentsFromS3(getAllDokumentObjectIdsForGesuch(gesuchId));
        deleteAllDokumentForGesuchInRepository(gesuchId);
    }

    public Uni<Void> deleteDokumentsFromS3Blocking(List<String> objectIds) {
        var future = s3.deleteObjects(
            buildDeleteObjectsRequest(
                configService.getBucketName(),
                objectIds
            )
        );
        future.join();

        return Uni.createFrom().voidItem();
    }

    public void executeDeleteDokumentsFromS3(List<String> objectIds) {
        Uni.createFrom()
        .item(objectIds)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(this::deleteDokumentsFromS3Blocking, Throwable::printStackTrace);
    }

    @Transactional
    public String deleteDokument(UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();
        dokumentRepository.delete(dokument);
        gesuchDokumentRepository.dropGesuchDokumentIfNoDokumente(dokument.getGesuchDokument().getId());
        return dokumentObjectId;
    }

    public CompletableFuture<PutObjectResponse> getCreateDokumentFuture(String objectId, FileUpload fileUpload) {
        return s3.putObject(
            buildPutRequest(
                fileUpload,
                configService.getBucketName(),
                objectId
            ),
            AsyncRequestBody.fromFile(fileUpload.uploadedFile())
        );
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> getGetDokumentFuture(String objectId) {
        return s3.getObject(
            buildGetRequest(
                configService.getBucketName(),
                objectId
            ),
            AsyncResponseTransformer.toPublisher()
        );
    }

    public CompletableFuture<DeleteObjectResponse> getDeleteDokumentFuture(String objectId) {
        return s3.deleteObject(
            buildDeleteObjectRequest(
                configService.getBucketName(),
                objectId
            )
        );
    }
}
