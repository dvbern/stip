package ch.dvbern.stip.api.gesuch.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchFormularRepository implements BaseRepository<GesuchFormular> {
}
