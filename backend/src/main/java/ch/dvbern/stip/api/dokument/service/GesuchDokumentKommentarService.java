package ch.dvbern.stip.api.dokument.service;

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

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchTrancheIdAndDokumentTyp(final UUID gesuchTrancheId, final DokumentTyp dokumentTyp) {
        final var gesuchDokumentKommentars = gesuchDokumentKommentarRepository.getByTypAndGesuchTrancheId(dokumentTyp, gesuchTrancheId);
        if(gesuchDokumentKommentars != null){
            return gesuchDokumentKommentars.stream()
                .map(gesuchDokumentKommentarMapper::toDto).toList();
        }
        return List.of();
    }

    @Transactional
    public void createKommentarForGesuchDokument(final GesuchDokument gesuchDokument,final GesuchDokumentKommentarDto gesuchDokumentKommentarDto) {
        final var kommentar = gesuchDokumentKommentarMapper.toEntity(gesuchDokumentKommentarDto);
        if(gesuchDokumentKommentarDto == null){
            createEmptyKommentarForGesuchDokument(gesuchDokument);
        }else{
            kommentar.setGesuchTranche(gesuchDokument.getGesuchTranche());
            kommentar.setDokumentstatus(gesuchDokument.getStatus());
            gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
        }
    }

    @Transactional
    public void createEmptyKommentarForGesuchDokument(final GesuchDokument gesuchDokument) {
        final var kommentar = new GesuchDokumentKommentar()
            .setGesuchTranche(gesuchDokument.getGesuchTranche())
            .setDokumentstatus(gesuchDokument.getStatus())
            .setDokumentTyp(gesuchDokument.getDokumentTyp())
            .setKommentar(null);
        gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
    }
}
