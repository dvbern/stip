package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;

public interface PersonenImHaushaltRequestBuilder {
    DmnRequest buildRequest(final GesuchFormular gesuchFormular, final ElternTyp elternToPrioritize);
}
