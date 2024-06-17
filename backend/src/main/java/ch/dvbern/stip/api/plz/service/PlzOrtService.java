package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class PlzOrtService {
    private PlzService plzService;
    private EntityManager entityManager;

    public boolean isInBern(final Adresse adresse) {
        if (adresse == null) {
            return false;
        }

        // Replace with actual logic once an external service has been implemented
        final var plz = adresse.getPlz();
        if (plz == null) {
            return false;
        }
        var flushmode = entityManager.getFlushMode();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        final var inBern = plzService.getAllPlzByKantonsKuerzel("be").stream().map(PlzDto::getPlz).toList().contains(plz);
        entityManager.setFlushMode(flushmode);

        return inBern;
    }
}
