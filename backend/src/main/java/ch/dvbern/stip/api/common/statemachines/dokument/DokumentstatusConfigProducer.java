package ch.dvbern.stip.api.common.statemachines.dokument;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Dependent
@RequiredArgsConstructor
@Slf4j
public class DokumentstatusConfigProducer {
    private final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config = new StateMachineConfig<>();

    @Produces
    public StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> createStateMachineConfig() {
        config.configure(Dokumentstatus.AUSSTEHEND)
            .permit(DokumentstatusChangeEvent.ABGELEHNT_IN_BEARBEITUNG, Dokumentstatus.ABGELEHNT_IN_BEARBEITUNG)
            .permit(DokumentstatusChangeEvent.AKZEPTIERT, Dokumentstatus.AKZEPTIERT);

        config.configure(Dokumentstatus.ABGELEHNT_IN_BEARBEITUNG)
            .permit(DokumentstatusChangeEvent.ABGELEHNT, Dokumentstatus.ABGELEHNT);

        config.configure(Dokumentstatus.ABGELEHNT);
        config.configure(Dokumentstatus.AKZEPTIERT);

        for (final var status : Dokumentstatus.values()) {
            final var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(this::onStateEntry);
        }

        return config;
    }

    private void onStateEntry(
        final Transition<Dokumentstatus, DokumentstatusChangeEvent> transition,
        final Object[] args
    ) {
        final var gesuchDokument = extractGesuchDokumentFromArgs(args);
        // TODO KSTIP-993 save a "history" entry (extract comment from args if targetState = ABGELEHNT_IN_BEARBEITUNG)
    }

    private GesuchDokument extractGesuchDokumentFromArgs(final Object[] args) {
        if (args.length == 0 || !(args[0] instanceof GesuchDokument)) {
            throw new AppErrorException(
                "Dokumentstatus statemachine transition handlers must have a 'GesuchDokument' as the first parameter"
            );
        }

        return (GesuchDokument) args[0];
    }
}
