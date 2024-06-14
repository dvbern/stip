package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.plz.repo.PlzRepository;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequestScoped
@RequiredArgsConstructor
public class PlzService {
    private final PlzRepository plzRepository;
    private final PlzMapper plzMapper;

    @Transactional
    public List<PlzDto> getAllPlz() {
        return plzRepository.findAll().stream().map(plzMapper::toDto).toList();
    }

    @Transactional
    public List<PlzDto> getAllPlzByKantonsKuerzel(String kantonsKuerzel) {
        return plzRepository.findAll().stream().filter(plz -> plz.getKantonskuerzel().equalsIgnoreCase(kantonsKuerzel)).map(plzMapper::toDto).toList();
    }
}
