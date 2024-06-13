package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.dto.BerechnungModelVersion;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import jakarta.inject.Singleton;

@Singleton
@BerechnungModelVersion(major = 1, minor = 0)
public class BerechnungRequestV1Builder implements BerechnungRequestBuilder {
    @Override
    public BerechnungRequest buildRequest(Gesuch gesuch, UUID tranchenId, ElternTyp elternTyp) {
        return BerechnungRequestV1.createRequest(gesuch, tranchenId, elternTyp);
    }
}
