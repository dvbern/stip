package ch.dvbern.stip.api.ausbildung.service;

import java.util.Collection;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
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
    public AusbildungsstaetteDto createAusbildungsstaette(AusbildungsstaetteCreateDto ausbildungsstaetteDto) {
        Ausbildungsstaette ausbildungsstaette = persistAusbildungsstaette(ausbildungsstaetteDto);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

    @Transactional
    public void updateAusbildungsstaette(UUID ausbildungsstaetteId, AusbildungsstaetteUpdateDto ausbildungsstaetteUpdateDto) {
        Ausbildungsstaette ausbildungsstaetteToUpdate = ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
        persistAusbildungsstaette(ausbildungsstaetteUpdateDto, ausbildungsstaetteToUpdate);
    }

    @Transactional
    public void deleteAusbildungsstaette(UUID ausbildungsstaetteId) {
        Ausbildungsstaette ausbildungsstaette = ausbildungsstaetteRepository.requireById(ausbildungsstaetteId);
        ausbildungsstaetteRepository.delete(ausbildungsstaette);
    }

    private void persistAusbildungsstaette(
        AusbildungsstaetteUpdateDto ausbildungsstaetteUpdate,
        Ausbildungsstaette ausbildungsstaetteToUpdate) {
        ausbildungsstaetteMapper.partialUpdate(ausbildungsstaetteUpdate, ausbildungsstaetteToUpdate);
        ausbildungsstaetteRepository.persist(ausbildungsstaetteToUpdate);
    }

    private Ausbildungsstaette persistAusbildungsstaette(
        AusbildungsstaetteCreateDto ausbildungsstaetteCreate) {
        Ausbildungsstaette ausbildungsstaette = ausbildungsstaetteMapper.toEntity(ausbildungsstaetteCreate);
        ausbildungsstaetteRepository.persist(ausbildungsstaette);
        return ausbildungsstaette;
    }

}
