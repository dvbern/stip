package ch.dvbern.stip.api.personinausbildung.service;

import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.repo.PersonInAusbildungRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class PersonInAusbildungService {
    private final PersonInAusbildungRepository personInAusbildungRepository;

    public void deletePersonInAusbildung(final PersonInAusbildung toDelete) {
        personInAusbildungRepository.delete(toDelete);
    }
}
