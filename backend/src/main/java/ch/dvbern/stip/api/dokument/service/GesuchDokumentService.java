package ch.dvbern.stip.api.dokument.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
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
    private final DokumentstatusService dokumentstatusService;

    @Transactional
    public List<GesuchDokumentKommentarDto> getGesuchDokumentsByGesuchDokumentId(UUID gesuchDokumentId) {
        return dokumentstatusService.getGesuchDokumentKommentareByGesuchDokumentId(gesuchDokumentId);
    }

    @Transactional
    public DokumentDto uploadDokument(
        final UUID gesuchId,
        final DokumentTyp dokumentTyp,
        final FileUpload fileUpload,
        final String objectId
    ) {
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

    @Transactional
    public List<DokumentDto> findGesuchDokumenteForTyp(final UUID gesuchId, final DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument =
            gesuchDokumentRepository.findByGesuchAndDokumentType(gesuchId, dokumentTyp).orElse(null);
        if (gesuchDokument == null) {
            return new ArrayList<>();
        }
        return gesuchDokument.getDokumente().stream().map(dokumentMapper::toDto).toList();
    }

    @Transactional
    public Optional<DokumentDto> findDokument(final UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = dokumentRepository.findById(dokumentId);
        return Optional.ofNullable(dokumentMapper.toDto(dokument));
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void deleteAllDokumentForGesuchInRepository(final UUID gesuchId) {
        gesuchDokumentRepository.findAllForGesuch(gesuchId)
            .forEach(gesuchDokumentRepository::delete);
    }

    @Transactional
    public List<String> getAllDokumentObjectIdsForGesuch(final UUID gesuchId) {
        return gesuchDokumentRepository.findAllForGesuch(gesuchId)
            .map(GesuchDokument::getDokumente)
            .flatMap(dokuments -> dokuments
                .stream()
                .map(Dokument::getObjectId)
            ).toList();
    }

    private static void gesuchstatusIsNotOrElseThrow(final Gesuch gesuch, final Gesuchstatus statusToVerify) {
        if (gesuch.getGesuchStatus() != statusToVerify) {
            throw new IllegalStateException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    @Transactional
    public void gesuchDokumentAblehnen(final UUID gesuchDokumentId, final GesuchDokumentAblehnenRequestDto dto) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        dokumentstatusService.triggerStatusChangeWithComment(
            gesuchDokument,
            DokumentstatusChangeEvent.ABGELEHNT,
            dto.getKommentar()
        );
    }

    @Transactional
    public void gesuchDokumentAkzeptieren(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        dokumentstatusService.triggerStatusChange(
            gesuchDokument,
            DokumentstatusChangeEvent.AKZEPTIERT
        );
    }

    public void deleteAllDokumentForGesuch(final UUID gesuchId) {
        executeDeleteDokumentsFromS3(getAllDokumentObjectIdsForGesuch(gesuchId));
        deleteAllDokumentForGesuchInRepository(gesuchId);
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
        final var objectIdentifiers = objectIds.stream().map(
            objectKey -> ObjectIdentifier.builder().key(GESUCH_DOKUMENT_PATH + objectKey).build()
        ).toList();
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

    @Transactional(TxType.REQUIRES_NEW)
    public String deleteDokument(final UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();
        dokumentRepository.delete(dokument);
        gesuchDokumentRepository.dropGesuchDokumentIfNoDokumente(dokument.getGesuchDokument().getId());
        return dokumentObjectId;
    }

    @Transactional
    public void deleteAbgelehnteDokumenteForGesuch(final Gesuch gesuch) {
        // Query for these instead of iterating "in memory" because gesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var gesuchDokumente = gesuchDokumentRepository
            .getAllForGesuchInStatus(gesuch, Dokumentstatus.ABGELEHNT)
            .toList();

        final var dokumenteToDeleteFromS3 = new ArrayList<String>();
        for (final var gesuchDokument : gesuchDokumente) {
            dokumenteToDeleteFromS3.addAll(
                gesuchDokument
                    .getDokumente()
                    .stream()
                    .map(Dokument::getObjectId).toList()
            );

            gesuchDokument.getDokumente().clear();
        }

        executeDeleteDokumentsFromS3(dokumenteToDeleteFromS3);
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

    private GesuchDokument createGesuchDokument(final Gesuch gesuch, final DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument = new GesuchDokument().setGesuch(gesuch).setDokumentTyp(dokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }
}
