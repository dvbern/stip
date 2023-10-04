package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsstaetteService {

    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsstaetteMapper ausbildungsstaetteMapper;

    public Collection<AusbildungsstaetteDto> getAusbildungsstaetten() {
        return ausbildungsstaetteRepository.findAll().stream().map(ausbildungsstaetteMapper::toDto).toList();
    }

    public AusbildungsstaetteDto findById(UUID ausbildungsstetteId) {
        var ausbildungsstaette =  ausbildungsstaetteRepository.findById(ausbildungsstetteId);
        return ausbildungsstaetteMapper.toDto(ausbildungsstaette);
    }

}
