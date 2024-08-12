package ch.dvbern.stip.api.steuerdaten.service;

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SteuerdatenRepository implements PanacheRepository<Steuerdaten> {
}
