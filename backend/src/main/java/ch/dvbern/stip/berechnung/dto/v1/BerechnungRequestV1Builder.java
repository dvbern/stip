package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.dto.BerechnungModelVersion;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import jakarta.inject.Singleton;

@Singleton
@BerechnungModelVersion(1)
public class BerechnungRequestV1Builder implements BerechnungRequestBuilder {
    @Override
    public BerechnungRequest buildRequest(Gesuch gesuch) {
        return BerechnungRequestV1.createRequest(gesuch);
    }
}
