package ch.dvbern.stip.api.ausbildung.service;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangService {
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final BildungskategorieRepository bildungskategorieRepository;
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangMapper ausbildungsgangMapper;

    public AusbildungsgangDto findById(UUID ausbildungsgangId) {
        return ausbildungsgangMapper.toDto(ausbildungsgangRepository.requireById(ausbildungsgangId));
    }

    @Transactional
    public AusbildungsgangDto createAusbildungsgang(AusbildungsgangCreateDto ausbildungsgangDto) {
        Ausbildungsgang ausbildungsgang = persistsAusbildungsgang(ausbildungsgangDto);
        return ausbildungsgangMapper.toDto(ausbildungsgang);
    }

    @Transactional
    public AusbildungsgangDto updateAusbildungsgang(UUID ausbildungsgangId, AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
        var ausbildungsgangToUpdate = ausbildungsgangRepository.requireById(ausbildungsgangId);
        persistsAusbildungsgang(ausbildungsgangUpdateDto, ausbildungsgangToUpdate);
        return ausbildungsgangMapper.toDto(ausbildungsgangToUpdate);
    }

    @Transactional
    public void deleteAusbildungsgang(UUID ausbildungsgangId) {
        var ausbildungsgang = ausbildungsgangRepository.requireById(ausbildungsgangId);
        ausbildungsgangRepository.delete(ausbildungsgang);
    }

    private void persistsAusbildungsgang(
        AusbildungsgangUpdateDto ausbildungsgangUpdate,
        Ausbildungsgang ausbildungsgangToUpdate) {
        ausbildungsgangMapper.partialUpdate(ausbildungsgangUpdate, ausbildungsgangToUpdate);
        ausbildungsgangToUpdate
            .setAusbildungsstaette(loadAusbildungsstaetteIfExists(ausbildungsgangUpdate.getAusbildungsstaetteId()));
        ausbildungsgangToUpdate.setBildungskategorie(loadBildungsart(ausbildungsgangUpdate.getBildungskategorieId()));
        ausbildungsgangRepository.persist(ausbildungsgangToUpdate);
    }

    private Ausbildungsgang persistsAusbildungsgang(
        AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        Ausbildungsgang ausbildungsgang = ausbildungsgangMapper.toEntity(ausbildungsgangCreateDto);
        ausbildungsgang
            .setAusbildungsstaette(loadAusbildungsstaetteIfExists(ausbildungsgangCreateDto.getAusbildungsstaetteId()));
        ausbildungsgang.setBildungskategorie(loadBildungsart(ausbildungsgangCreateDto.getBildungskategorieId()));
        ausbildungsgangRepository.persist(ausbildungsgang);
        return ausbildungsgang;
    }

    private Ausbildungsstaette loadAusbildungsstaetteIfExists(UUID ausbildungsstaetteId) {
        return ausbildungsstaetteId != null ?
            ausbildungsstaetteRepository.requireById(ausbildungsstaetteId) :
            new Ausbildungsstaette();
    }

    private Bildungskategorie loadBildungsart(UUID bildungsartId) {
        return bildungskategorieRepository.requireById(bildungsartId);
    }
}
