package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentService {

    private final DokumentMapper dokumentMapper;

    private final DokumentRepository dokumentRepository;

    private final GesuchDokumentRepository gesuchDokumentRepository;

    private final GesuchRepository gesuchRepository;

    @Transactional
    public DokumentDto uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, FileUpload fileUpload) {
        Gesuch gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
        GesuchDokument gesuchDokument = gesuchDokumentRepository.findByGesuchAndDokumentType(gesuch.getId(),dokumentTyp).orElse(
                createGesuchDokument(gesuch,dokumentTyp)
        );
        Dokument dokument = new Dokument();
        dokument.setGesuchDokument(gesuchDokument);
        dokument.setFilename(fileUpload.fileName());
        dokument.setFilesize(String.valueOf(fileUpload.size()));
        dokument.setFilepfad("DUMMY");
        dokumentRepository.persist(dokument);

        return dokumentMapper.toDto(dokument);
    }

    private GesuchDokument createGesuchDokument(Gesuch gesuch, DokumentTyp dokumentTyp){
        GesuchDokument gesuchDokument = new GesuchDokument().setGesuch(gesuch).setDokumentTyp(dokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }

    public List<DokumentDto> findGesuchDokumenteForTyp(UUID gesuchId, DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument = dokumentRepository.findGesuchDokument(dokumentTyp, gesuchId);
        return gesuchDokument.getDokumente().stream().map(dokumentMapper::toDto).toList();
    }

    public Optional<DokumentDto> findDokument(UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = dokumentRepository.findById(dokumentId);
        return Optional.ofNullable(dokumentMapper.toDto(dokument));
    }

    public PutObjectRequest buildPutRequest(FileUpload fileUpload, String bucketName) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileUpload.fileName())
                .contentType(fileUpload.contentType())
                .build();
    }

    public GetObjectRequest buildGetRequest(String objectKey, String bucketName) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }
}
