package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;

public interface BerechnungRequestBuilder {
    BerechnungRequest buildRequest(final Gesuch gesuch, final GesuchTranche gesuchTranche, final ElternTyp elternTyp);
}
