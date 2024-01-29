package ch.dvbern.stip.api.benutzereinstellungen.repo;

import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BenutzereinstellungenRepository implements BaseRepository<Benutzereinstellungen> {

}
