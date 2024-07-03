package ch.dvbern.stip.api.common.statemachines.dokument;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import lombok.extern.slf4j.Slf4j;

@Dependent
@Slf4j
@Unremovable
public class DokumentstatusConfigProducer {
    private final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config = new StateMachineConfig<>();

    @Produces
    public StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> createDokumentstatusStateMachine() {
        config.configure(Dokumentstatus.AUSSTEHEND)
            .permit(DokumentstatusChangeEvent.ABGELEHNT, Dokumentstatus.ABGELEHNT)
            .permit(DokumentstatusChangeEvent.AKZEPTIERT, Dokumentstatus.AKZEPTIERT);

        config.configure(Dokumentstatus.ABGELEHNT);
        config.configure(Dokumentstatus.AKZEPTIERT);

        return config;
    }
}
