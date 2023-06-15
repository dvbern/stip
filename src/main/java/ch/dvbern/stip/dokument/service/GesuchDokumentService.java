package ch.dvbern.stip.dokument.service;

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

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GesuchDokumentService {

    @Inject
    private EntityManager entityManager;
    @Inject
    GesuchService gesuchService;

    public String uploadDokument(UUID gesuchId, DokumentTyp dokumentTyp, FileUpload input) {

        Gesuch gesuch = gesuchService.findGesuch(gesuchId).orElseThrow(() -> new RuntimeException("Gesuch not found"));


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
