package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class PlzOrtService {
    private PlzService plzService;

    public boolean isInBern(final Adresse adresse) {
        if (adresse == null) {
            return false;
        }

        // Replace with actual logic once an external service has been implemented
        final var plz = adresse.getPlz();
        if (plz == null) {
            return false;
        }

        return plzService.getAllPlzByKantonsKuerzel("be").stream().map(PlzDto::getPlz).toList().contains(plz);
    }
}
