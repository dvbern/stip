package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.DokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentService {

    private DokumentMapper dokumentMapper;

    private ConfigService configService;

    private DokumentRepository dokumentRepository;

    public Dokument uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, FileUpload fileUpload) throws IOException {
        //Gesuch gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(() -> new RuntimeException("Gesuch not found"));
        // TODO find DokumentTyp or create if not exist

        Dokument dokument = new Dokument();
        dokument.setFilename(fileUpload.fileName());
        dokument.setFilesize(String.valueOf(fileUpload.size()));
        // TODO find better path, or save dok with uuid as name
        File file = new File(configService.getFilePath() + "/" + dokumentTyp.toString());
        file.mkdirs();
        Files.move(fileUpload.filePath(), file.toPath());
        dokument.setFilepfad(file.getPath());
        // TODO set dokumentTyp to dokument for reference
        dokumentRepository.persist(dokument);

        return dokument;
    }

    public List<DokumentDto> findGesuchDokumenteForTyp(UUID gesuchId, DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument = dokumentRepository.findGesuchDokument(dokumentTyp, gesuchId);
        return gesuchDokument.getDokumente().stream().map(dokumentMapper::toDto).toList();
    }

    public Optional<Dokument> findDokument(UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = dokumentRepository.findById(dokumentId);
        return Optional.ofNullable(dokument);
    }
}
