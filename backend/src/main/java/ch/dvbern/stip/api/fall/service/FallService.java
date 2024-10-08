package ch.dvbern.stip.api.fall.service;

import java.util.List;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.service.IdEncryptionService;
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
    private final IdEncryptionService idEncryptionService;
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
        // TODO KSTIP-1411: Mandantenkürzel
        var nextValue = fallNummerSeqRepository.getNextValue("BE");
        var encoded = idEncryptionService.encryptLengthSix(nextValue);

        // TODO KSTIP-1411: Mandantenkürzel
        return String.format("BE.F.%s", encoded);
    }
}
