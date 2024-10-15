package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public final class GesuchTrancheStatusChangeEventTriggerWithComment
    extends TriggerWithParameters2<GesuchTranche, String, GesuchTrancheStatusChangeEvent> {
    private GesuchTrancheStatusChangeEventTriggerWithComment(GesuchTrancheStatusChangeEvent underlyingTrigger) {
        super(underlyingTrigger, GesuchTranche.class, String.class);
    }

    public static GesuchTrancheStatusChangeEventTriggerWithComment createTrigger(GesuchTrancheStatusChangeEvent trigger) {
        return new GesuchTrancheStatusChangeEventTriggerWithComment(trigger);
    }
}
