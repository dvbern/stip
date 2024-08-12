package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AuszahlungService {
    private final AuszahlungRepository auszahlungRepository;

    public void deleteAuszahlung(final Auszahlung auszahlung) {
        auszahlungRepository.delete(auszahlung);
    }
}
