package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;

public interface BerechnungRequestBuilder {
    BerechnungRequest buildRequest(final Gesuch gesuch);
}
