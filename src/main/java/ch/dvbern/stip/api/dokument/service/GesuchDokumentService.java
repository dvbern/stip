package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import io.smallrye.mutiny.Uni;
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

	private final String GESUCH_DOKUMENT_PATH = "gesuch/";

	@Transactional
	public DokumentDto uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, FileUpload fileUpload, String objectId) {
		Gesuch gesuch = gesuchRepository.findByIdOptional(gesuchId).orElseThrow(NotFoundException::new);
		GesuchDokument gesuchDokument =
				gesuchDokumentRepository.findByGesuchAndDokumentType(gesuch.getId(), dokumentTyp).orElse(
						createGesuchDokument(gesuch, dokumentTyp)
				);
		Dokument dokument = new Dokument();
		dokument.setGesuchDokument(gesuchDokument);
		dokument.setFilename(fileUpload.fileName());
		dokument.setObjectId(objectId);
		dokument.setFilesize(String.valueOf(fileUpload.size()));
		dokument.setFilepfad(GESUCH_DOKUMENT_PATH);
		dokumentRepository.persist(dokument);

		return dokumentMapper.toDto(dokument);
	}

	private GesuchDokument createGesuchDokument(Gesuch gesuch, DokumentTyp dokumentTyp) {
		GesuchDokument gesuchDokument = new GesuchDokument().setGesuch(gesuch).setDokumentTyp(dokumentTyp);
		gesuchDokumentRepository.persist(gesuchDokument);
		return gesuchDokument;
	}

	public List<DokumentDto> findGesuchDokumenteForTyp(UUID gesuchId, DokumentTyp dokumentTyp) {
		GesuchDokument gesuchDokument = gesuchDokumentRepository.findByGesuchAndDokumentType(gesuchId, dokumentTyp)
				.orElseThrow(NotFoundException::new);
		return gesuchDokument.getDokumente().stream().map(dokumentMapper::toDto).toList();
	}

	public Optional<DokumentDto> findDokument(UUID dokumentId) {
		Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
		Dokument dokument = dokumentRepository.findById(dokumentId);
		return Optional.ofNullable(dokumentMapper.toDto(dokument));
	}

	public PutObjectRequest buildPutRequest(FileUpload fileUpload, String bucketName, String objectId) {
		return PutObjectRequest.builder()
				.bucket(bucketName)
				.key(GESUCH_DOKUMENT_PATH + objectId)
				.contentType(fileUpload.contentType())
				.build();
	}

	public GetObjectRequest buildGetRequest(String objectKey, String bucketName) {
		return GetObjectRequest.builder()
				.bucket(bucketName)
				.key(GESUCH_DOKUMENT_PATH + objectKey)
				.build();
	}
}
