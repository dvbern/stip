package ch.dvbern.stip.api.plz.service;

import java.util.List;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.plz.repo.PlzRepository;
import ch.dvbern.stip.generated.dto.PlzDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class PlzService {
    private final PlzRepository plzRepository;
    private final PlzMapper plzMapper;

    @Transactional
    public List<PlzDto> getAllPlz() {
        return plzRepository.findAll().stream().map(plzMapper::toDto).toList();
    }

//    @Transactional
    public boolean isInBern(final String postleitzahl) {
        if (postleitzahl == null) {
            return false;
        }
        return plzRepository.isPlzInKanton(postleitzahl, "be");
    }

//    @Transactional
    public boolean isInBern(final Adresse adresse) {
        if (adresse == null) {
            return false;
        }
        // Replace with actual logic once an external service has been implemented
        final var plz = adresse.getPlz();
        return isInBern(plz);
    }
}
