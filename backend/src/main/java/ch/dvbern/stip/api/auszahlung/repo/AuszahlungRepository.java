package ch.dvbern.stip.api.auszahlung.repo;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuszahlungRepository implements BaseRepository<Auszahlung> {
}
