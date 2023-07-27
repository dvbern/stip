package ch.dvbern.stip.api.benutzer.repo;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BenutzerRepository implements BaseRepository<Benutzer> {
}
