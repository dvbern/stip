package ch.dvbern.stip.api.benutzer.repo;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BenutzerRepository implements BaseRepository<Benutzer> {
    public Optional<Benutzer> findByKeycloakId(String keycloakId) {
        return find("keycloakId", keycloakId).singleResultOptional();
    }
}
