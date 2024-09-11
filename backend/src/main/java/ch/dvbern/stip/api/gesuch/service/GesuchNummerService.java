package ch.dvbern.stip.api.gesuch.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.service.SqidsService;
import ch.dvbern.stip.api.gesuch.repo.GesuchNummerSeqRepository;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchNummerService {
    private final GesuchNummerSeqRepository gesuchNummerSeqRepository;
    private final SqidsService sqidsService;
    private final GesuchsperiodenService gesuchsperiodenService;
    private final GesuchsjahrService gesuchsjahrService;

    private static final String MANDANT = "BE"; // TODO: KSTIP-1411

    @Transactional
    public String createGesuchNummer(final UUID gesuchsperiodeId) {
        final var gesuchsperiode =
            gesuchsperiodenService.getGesuchsperiode(gesuchsperiodeId).orElseThrow(NotFoundException::new);
        final var gesuchsjahr = gesuchsjahrService.getGesuchsjahr(gesuchsperiode.getGesuchsjahrId());
        final var technischesJahr = gesuchsjahr.getTechnischesJahr();

        var nextValue = gesuchNummerSeqRepository.getNextSequenceValue(MANDANT, technischesJahr);

        var encoded = sqidsService.encodeLenghtFive(nextValue);

        return String.format("%s.%s.G.%s", technischesJahr, MANDANT, encoded);
    }

}
