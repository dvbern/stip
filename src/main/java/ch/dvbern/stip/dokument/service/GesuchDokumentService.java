package ch.dvbern.stip.dokument.service;

import ch.dvbern.stip.config.service.ConfigService;
import ch.dvbern.stip.dokument.dto.GesuchDokumentDTO;
import ch.dvbern.stip.dokument.model.Dokument;
import ch.dvbern.stip.dokument.model.DokumentTyp;
import ch.dvbern.stip.dokument.model.QGesuchDokument;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.service.GesuchService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GesuchDokumentService {

    @Inject
    private EntityManager entityManager;
    @Inject
    GesuchService gesuchService;

    @Inject
    ConfigService configService;

    public String uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, List<FileUpload> files) throws IOException {
        Gesuch gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(() -> new RuntimeException("Gesuch not found"));
        // TODO find DokumentTyp or create if not exist
        for(FileUpload fileUpload: files) {
            Dokument dokument = new Dokument();
            dokument.setFilename(fileUpload.fileName());
            dokument.setFilesize(String.valueOf(fileUpload.size()));
            // TODO find better path, or save dok with uuid as name
            File file = new File(configService.getFilePath() + "/" + dokumentTyp.toString());
            file.mkdirs();
            Files.move(fileUpload.filePath(), file.toPath());
            dokument.setFilepfad(file.getPath());
            // TODO set dokumentTyp to dokument for reference
        }
        return "Dokument hochgeladen";
    }

    public Optional<GesuchDokumentDTO> findGesuchDokumentForTyp(UUID gesuchId, DokumentTyp dokumentTyp) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QGesuchDokument qGesuchDokument = new QGesuchDokument("gesuchDokument");
        var query = queryFactory.select(qGesuchDokument).from(qGesuchDokument).where(
                qGesuchDokument.dokumentTyp.eq(dokumentTyp).and(qGesuchDokument.gesuch.id.eq(gesuchId))
        );
        GesuchDokumentDTO gesuchDokument =  GesuchDokumentDTO.from(query.fetchOne());
        return Optional.ofNullable(gesuchDokument);
    }

    public Optional<Dokument> findDokument(UUID dokumentId) {
        Objects.requireNonNull(dokumentId, "id muss gesetzt sein");
        Dokument dokument = entityManager.find(Dokument.class, dokumentId);
        return Optional.ofNullable(dokument);
    }
}
