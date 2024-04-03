package ch.dvbern.stip.api.common.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlzOrtService {
    public boolean isInBern(final Adresse adresse) {
        if (adresse == null) {
            return false;
        }

        // Replace with actual logic once an external service has been implemented
        final var plz = adresse.getPlz();
        if (plz == null) {
            return false;
        }

        return plz.startsWith("3");
    }
}
