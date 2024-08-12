package ch.dvbern.stip.api.eltern.service;

import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.repo.ElternRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class ElternService {
    private final ElternRepository elternRepository;

    public void deleteElterns(final Set<Eltern> eltern) {
        for (final var elternteil : eltern) {
            elternRepository.delete(elternteil);
        }
    }
}
