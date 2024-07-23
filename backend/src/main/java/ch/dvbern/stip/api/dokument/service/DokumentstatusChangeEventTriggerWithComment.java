package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public class DokumentstatusChangeEventTriggerWithComment
    extends TriggerWithParameters2<GesuchDokument, String, DokumentstatusChangeEvent> {
    private DokumentstatusChangeEventTriggerWithComment(DokumentstatusChangeEvent trigger) {
        super(trigger, GesuchDokument.class, String.class);
    }

    public static DokumentstatusChangeEventTriggerWithComment createTrigger(DokumentstatusChangeEvent trigger) {
        return new DokumentstatusChangeEventTriggerWithComment(trigger);
    }
}
