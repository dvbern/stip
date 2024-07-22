package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public class DokumentstatusChangeEventTrigger
    extends TriggerWithParameters1<GesuchDokument, DokumentstatusChangeEvent> {
    private DokumentstatusChangeEventTrigger(DokumentstatusChangeEvent trigger) {
        super(trigger, GesuchDokument.class);
    }

    public static DokumentstatusChangeEventTrigger createTrigger(DokumentstatusChangeEvent trigger) {
        return new DokumentstatusChangeEventTrigger(trigger);
    }
}
