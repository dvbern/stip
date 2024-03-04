package ch.dvbern.stip.api.ausbildung.service;

import java.util.Collection;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteService {

    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsstaetteMapper ausbildungsstaetteMapper;

    @Transactional
    public Collection<AusbildungsstaetteDto> getAusbildungsstaetten() {
        return ausbildungsstaetteRepository.findAll()
            .stream()
            .filter(staetten -> !staetten.getAusbildungsgaenge().isEmpty())
            .map(ausbildungsstaetteMapper::toDto)
            .toList();
    }

    public AusbildungsstaetteDto findById(UUID ausbildungsstetteId) {
        var ausbildungsstaette = ausbildungsstaetteRepository.findById(ausbildungsstetteId);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

}
