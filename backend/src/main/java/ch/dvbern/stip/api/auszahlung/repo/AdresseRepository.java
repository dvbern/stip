package ch.dvbern.stip.api.auszahlung.repo;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AdresseRepository implements BaseRepository<Adresse> {
}
