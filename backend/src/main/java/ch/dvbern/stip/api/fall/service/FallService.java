package ch.dvbern.stip.api.fall.service;

import java.util.List;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.service.SqidsService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallNummerSeqRepository;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.FallDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class FallService {
    private final FallMapper fallMapper;
    private final FallRepository fallRepository;
    private final BenutzerService benutzerService;
    private final SqidsService sqidsService;
    private final FallNummerSeqRepository fallNummerSeqRepository;

    @Transactional
    public FallDto createFallForGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = new Fall();
        fall.setGesuchsteller(benutzer);
        fall.setFallNummer(createFallNummer());
        fallRepository.persistAndFlush(fall);
        return fallMapper.toDto(fall);
    }

    public List<FallDto> findFaelleForSb() {
        final var sachbearbeiterId = benutzerService.getCurrentBenutzer().getId();
        return fallRepository.findFaelleForSb(sachbearbeiterId).map(fallMapper::toDto).toList();
    }

    public FallDto findFallForGs() {
        final var gesuchstellerId = benutzerService.getCurrentBenutzer().getId();
        return fallMapper.toDto(
            fallRepository.findFallForGsOptional(gesuchstellerId).orElse(null)
        );
    }

    private String createFallNummer() {
        var nextValue = fallNummerSeqRepository.getNextValue();
        var encoded = sqidsService.encodeLenghtSix(nextValue);

        return String.format("BE.G.%s", encoded);
    }
}
