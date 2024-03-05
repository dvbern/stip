package ch.dvbern.stip.api.ausbildung.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteService {

    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsstaetteMapper ausbildungsstaetteMapper;

    public AusbildungsstaetteDto findById(UUID ausbildungsstetteId) {
        var ausbildungsstaette = ausbildungsstaetteRepository.findById(ausbildungsstetteId);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public Collection<AusbildungsstaetteDto> getAusbildungsstaetten() {
        return ausbildungsstaetteRepository.findAll()
            .stream()
            .filter(staetten -> !staetten.getAusbildungsgaenge().isEmpty())
            .map(ausbildungsstaetteMapper::toDto)
            .toList();
    }

    @Transactional
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteUpdateDto ausbildungsstaetteDto) {
        Ausbildungsstaette ausbildungsstaette = new Ausbildungsstaette();
        persistAusbildungsstaette(ausbildungsstaetteDto, ausbildungsstaette);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public void updateAusbildungsstaette(UUID ausbildungsstaetteId, AusbildungsstaetteUpdateDto ausbildungsstaetteUpdateDto) {
        Ausbildungsstaette ausbildungsstaetteToUpdate = ausbildungsstaetteRepository.findById(ausbildungsstaetteId);
        persistAusbildungsstaette(ausbildungsstaetteUpdateDto, ausbildungsstaetteToUpdate);
    }

    @Transactional
    public void deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        Ausbildungsstaette ausbildungsstaette = ausbildungsstaetteRepository.findById(ausbildungsstaetteId);
        ausbildungsstaetteRepository.delete(ausbildungsstaette);
    }

    private void persistAusbildungsstaette(
        AusbildungsstaetteUpdateDto ausbildungsstaetteUpdate,
        Ausbildungsstaette ausbildungsstaetteToUpdate) {
        ausbildungsstaetteMapper.partialUpdate(ausbildungsstaetteUpdate, ausbildungsstaetteToUpdate);
        ausbildungsstaetteRepository.persist(ausbildungsstaetteToUpdate);
    }

}
