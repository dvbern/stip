package ch.dvbern.stip.berechnung.dto;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;

public interface BerechnungRequestBuilder {
    BerechnungRequest buildRequest(final Gesuch gesuch, final UUID tranchenId);
}
