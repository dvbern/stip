package ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FehlendeDokumenteHandler implements GesuchStatusStateChangeHandler {
    private final GesuchDokumentService gesuchDokumentService;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getDestination() == Gesuchstatus.FEHLENDE_DOKUMENTE;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        gesuchDokumentService.deleteAbgelehnteDokumenteForGesuch(gesuch);
        gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
            .forEach(tranche -> tranche.setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS));
        sendNotification(gesuch);
    }

    private void sendNotification(Gesuch gesuch) {
        notificationService.createMissingDocumentNotification(gesuch);
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService,gesuch);
    }
}
