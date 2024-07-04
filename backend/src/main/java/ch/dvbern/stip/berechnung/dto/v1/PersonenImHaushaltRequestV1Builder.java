package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import jakarta.inject.Singleton;

@Singleton
@DmnModelVersion(major = 1, minor = 0)
public class PersonenImHaushaltRequestV1Builder implements PersonenImHaushaltRequestBuilder {
    @Override
    public DmnRequest buildRequest(final GesuchFormular gesuchFormular, final ElternTyp elternToPrioritize) {
        return PersonenImHaushaltRequestV1.createRequest(gesuchFormular, elternToPrioritize);
    }
}
