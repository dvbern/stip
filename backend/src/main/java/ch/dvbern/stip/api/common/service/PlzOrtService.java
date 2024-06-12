package ch.dvbern.stip.api.common.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlzOrtService {
    private PlzService plzService;
    public PlzOrtService(PlzService plzService){
        this.plzService = plzService;
    }

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
