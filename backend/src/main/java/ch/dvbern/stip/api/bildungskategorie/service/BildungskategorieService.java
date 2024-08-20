package ch.dvbern.stip.api.bildungskategorie.service;

import java.util.List;

import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.generated.dto.BildungskategorieDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BildungskategorieService {
    private final BildungskategorieRepository bildungskategorieRepository;
    private final BildungskategorieMapper bildungskategorieMapper;

    @Transactional
    public List<BildungskategorieDto> findAll() {
        return bildungskategorieRepository.findAll().stream().map(bildungskategorieMapper::toDto).toList();
    }
}
