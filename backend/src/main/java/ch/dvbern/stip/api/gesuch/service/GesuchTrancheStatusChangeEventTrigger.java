package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public class GesuchTrancheStatusChangeEventTrigger
    extends TriggerWithParameters1<GesuchTranche, GesuchTrancheStatusChangeEvent> {
    private GesuchTrancheStatusChangeEventTrigger(GesuchTrancheStatusChangeEvent underlyingTrigger) {
        super(underlyingTrigger, GesuchTranche.class);
    }

    public static GesuchTrancheStatusChangeEventTrigger createTrigger(final GesuchTrancheStatusChangeEvent event) {
        return new GesuchTrancheStatusChangeEventTrigger(event);
    }
}
