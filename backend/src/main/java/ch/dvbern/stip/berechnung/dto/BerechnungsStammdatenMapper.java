package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;

public interface BerechnungsStammdatenMapper {
    BerechnungsStammdatenDto mapFromRequest(
        final DmnRequest request
    );
}
