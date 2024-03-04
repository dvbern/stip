package ch.dvbern.stip.api.fall.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
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

    private final BenutzerRepository benutzerRepository;

    @Transactional
    public FallDto createFall(UUID benutzerId) {
        var benutzer = benutzerRepository.findByIdOptional(benutzerId).orElseThrow(NotFoundException::new);
        var fall = new Fall();
        fall.setGesuchsteller(benutzer);
        fallRepository.persist(fall);
        return fallMapper.toDto(fall);
    }

    public Optional<FallDto> getFall(UUID id) {
        var optionalFall = fallRepository.findByIdOptional(id);
        return optionalFall.map(fallMapper::toDto);
    }

    public List<FallDto> findFaelleForBenutzer(UUID benutzerId) {
        return fallRepository.findAllForBenutzer(benutzerId).map(fallMapper::toDto).toList();
    }
}
