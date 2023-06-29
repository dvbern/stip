package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.repo.AusbildungstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungstaetteDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungstaetteService {


    private final AusbildungstaetteRepository ausbildungstaetteRepository;
    private final AusbildungstaetteMapper ausbildungstaetteMapper;

    public Collection<AusbildungstaetteDto> getAusbildungsstaetten() {
        return ausbildungstaetteRepository.findAll().stream().map(ausbildungstaetteMapper::toDto).collect(Collectors.toList());
    }

}
