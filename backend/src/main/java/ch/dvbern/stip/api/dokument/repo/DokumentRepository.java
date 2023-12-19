package ch.dvbern.stip.api.dokument.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentRepository implements BaseRepository<Dokument> {

}
