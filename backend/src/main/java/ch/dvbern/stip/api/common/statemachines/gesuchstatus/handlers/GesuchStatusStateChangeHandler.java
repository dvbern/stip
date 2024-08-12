package ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers;

import ch.dvbern.stip.api.common.statemachines.StateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;

public interface GesuchStatusStateChangeHandler
    extends StateChangeHandler<Gesuchstatus, GesuchStatusChangeEvent, Gesuch> {
}
