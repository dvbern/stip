package ch.dvbern.stip.gesuch.repo;

import ch.dvbern.stip.common.repostitory.BaseRepository;
import ch.dvbern.stip.gesuch.entity.Gesuch;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchRepository implements BaseRepository<Gesuch> {
}
