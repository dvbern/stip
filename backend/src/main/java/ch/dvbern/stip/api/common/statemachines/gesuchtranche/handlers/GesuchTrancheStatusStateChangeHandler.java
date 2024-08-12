package ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers;

import ch.dvbern.stip.api.common.statemachines.StateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;

public interface GesuchTrancheStatusStateChangeHandler
    extends StateChangeHandler<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent, GesuchTranche> {
}
