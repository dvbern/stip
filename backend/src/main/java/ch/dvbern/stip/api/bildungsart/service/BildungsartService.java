package ch.dvbern.stip.api.bildungsart.service;

import java.util.List;

import ch.dvbern.stip.api.bildungsart.repo.BildungsartRepository;
import ch.dvbern.stip.generated.dto.BildungsartDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BildungsartService {
    private final BildungsartRepository bildungsartRepository;
    private final BildungsartMapper bildungsartMapper;

    @Transactional
    public List<BildungsartDto> findAll() {
        return bildungsartRepository.findAll().stream().map(bildungsartMapper::toDto).toList();
    }
}
