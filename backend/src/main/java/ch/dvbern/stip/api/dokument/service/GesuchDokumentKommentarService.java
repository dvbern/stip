package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarService {
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final GesuchDokumentKommentarMapper gesuchDokumentKommentarMapper;
    private final BenutzerService  benutzerService;

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchIdAndDokumentTyp(final UUID gesuchId, final DokumentTyp dokumentTyp) {
        final var gesuchDokumentKommentars = gesuchDokumentKommentarRepository.getByTypAndGesuchId(dokumentTyp,gesuchId);
        if(gesuchDokumentKommentars != null){
            return gesuchDokumentKommentars.stream()
                .map(gesuchDokumentKommentarMapper::toDto).toList();
        }else{
            return List.of();
        }
    }

    @Transactional
    public void createKommentarForGesuchDokument(final GesuchDokument gesuchDokument,final GesuchDokumentKommentarDto gesuchDokumentKommentarDto) {
        final var kommentar = gesuchDokumentKommentarMapper.toEntity(gesuchDokumentKommentarDto);
        if(gesuchDokumentKommentarDto == null){
            createEmptyKommentarForGesuchDokument(gesuchDokument);
        }else{
            kommentar.setGesuch(gesuchDokument.getGesuch());
            kommentar.setDokumentstatus(gesuchDokument.getStatus());
            gesuchDokumentKommentarRepository.persist(kommentar);
        }
    }

    @Transactional
    public void createEmptyKommentarForGesuchDokument(final GesuchDokument gesuchDokument) {
        final var kommentar = new GesuchDokumentKommentar()
            .setGesuch(gesuchDokument.getGesuch())
            .setDokumentstatus(gesuchDokument.getStatus())
            .setKommentar(null);
        gesuchDokumentKommentarRepository.persist(kommentar);
    }
}
