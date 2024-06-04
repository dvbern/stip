package ch.dvbern.stip.api.fall.service;

import java.util.List;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.FallDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class FallService {

    private final FallMapper fallMapper;

    private final FallRepository fallRepository;

    private final BenutzerService benutzerService;

    @Transactional
    public FallDto createFallForGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = new Fall();
        fall.setGesuchsteller(benutzer);
        fallRepository.persistAndFlush(fall);
        return fallMapper.toDto(fall);
    }

    public List<FallDto> findFaelleForSb() {
        final var sachbearbeiterId = benutzerService.getCurrentBenutzer().getId();
        return fallRepository.findFaelleForSb(sachbearbeiterId).map(fallMapper::toDto).toList();
    }

    public FallDto findFallForGs() {
        final var gesuchstellerId = benutzerService.getCurrentBenutzer().getId();
        return fallMapper.toDto(fallRepository.findFallForGsOptional(gesuchstellerId).orElseThrow(NotFoundException::new));
    }
}
