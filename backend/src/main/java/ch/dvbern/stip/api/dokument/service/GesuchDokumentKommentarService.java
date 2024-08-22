package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarService {
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;

    @Transactional
    public void createKommentarForGesuchDokument(final GesuchDokument gesuchDokument, final String comment) {
        final var kommentar = new GesuchDokumentKommentar()
            .setGesuchTranche(gesuchDokument.getGesuchTranche())
            .setDokumentTyp(gesuchDokument.getDokumentTyp())
            .setDokumentstatus(gesuchDokument.getStatus())
            .setKommentar(comment);

        gesuchDokumentKommentarRepository.persist(kommentar);
    }
}
