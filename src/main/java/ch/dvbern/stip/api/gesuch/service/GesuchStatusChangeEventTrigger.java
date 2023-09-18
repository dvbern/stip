package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public final class GesuchStatusChangeEventTrigger extends TriggerWithParameters1<Gesuch, GesuchStatusChangeEvent> {

	private GesuchStatusChangeEventTrigger(GesuchStatusChangeEvent underlyingTrigger) {
		super(underlyingTrigger, Gesuch.class);
	}

	public static GesuchStatusChangeEventTrigger createTrigger(GesuchStatusChangeEvent event) {
		return new GesuchStatusChangeEventTrigger(event);
	}
}
