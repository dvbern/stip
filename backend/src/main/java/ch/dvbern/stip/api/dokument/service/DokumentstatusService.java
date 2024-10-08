package ch.dvbern.stip.api.dokument.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentstatusService {
    private final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config;
    private final GesuchDokumentKommentarService dokumentKommentarService;

    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentareByGesuchAndType(UUID gesuchTrancheId, DokumentTyp dokumentTyp){
        return dokumentKommentarService.getAllKommentareForGesuchTrancheIdAndDokumentTyp(gesuchTrancheId, dokumentTyp);}

    public void triggerStatusChange(final GesuchDokument gesuchDokument, final DokumentstatusChangeEvent event) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTrigger.createTrigger(event), gesuchDokument);
        dokumentKommentarService.createEmptyKommentarForGesuchDokument(gesuchDokument);
    }

    public void triggerStatusChangeWithComment(
        final GesuchDokument gesuchDokument,
        final DokumentstatusChangeEvent event,
        final GesuchDokumentKommentarDto commentDto
    ) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTriggerWithComment.createTrigger(event), gesuchDokument, commentDto.getKommentar());
        dokumentKommentarService.createKommentarForGesuchDokument(gesuchDokument, commentDto);
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
