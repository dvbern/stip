package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@DmnModelVersion(major = 1, minor = 0)
public class BerechnungRequestV1Builder implements BerechnungRequestBuilder {
    private final PersonenImHaushaltService personenImHaushaltService;

    @Override
    public DmnRequest buildRequest(Gesuch gesuch, GesuchTranche gesuchTranche, ElternTyp elternTyp) {
        return BerechnungRequestV1.createRequest(gesuch, gesuchTranche, elternTyp, personenImHaushaltService);
    }
}
