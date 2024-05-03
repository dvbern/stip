package ch.dvbern.stip.api.gesuchsjahr.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchsjahrRepository implements BaseRepository<Gesuchsjahr> {
}
