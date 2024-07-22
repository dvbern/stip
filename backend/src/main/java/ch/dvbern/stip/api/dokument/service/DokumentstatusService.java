package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentstatusService {
    private final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config;
    private final GesuchDokumentKommentarService dokumentKommentarService;

    public void triggerStatusChange(final GesuchDokument gesuchDokument, final DokumentstatusChangeEvent event) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTrigger.createTrigger(event), gesuchDokument);
        dokumentKommentarService.createKommentarForGesuchDokument(gesuchDokument, null);
    }

    public void triggerStatusChangeWithComment(
        final GesuchDokument gesuchDokument,
        final DokumentstatusChangeEvent event,
        final String comment
    ) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTriggerWithComment.createTrigger(event), gesuchDokument, comment);
        dokumentKommentarService.createKommentarForGesuchDokument(gesuchDokument, comment);
    }

    private StateMachine<Dokumentstatus, DokumentstatusChangeEvent> createStateMachine(
        final GesuchDokument gesuchDokument
    ) {
        return new StateMachine<>(
            gesuchDokument.getStatus(),
            gesuchDokument::getStatus,
            gesuchDokument::setStatus,
            config
        );
    }
}
