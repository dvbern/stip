package ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class KomplettEingereichtHandler implements GesuchStatusStateChangeHandler {
    private final MailService mailService;

    private final NotificationService notificationService;

    private final GesuchStatusService gesuchStatusService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getDestination() == Gesuchstatus.EINGEREICHT;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
        notificationService.createGesuchEingereichtNotification(gesuch);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
        gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .forEach(tranche -> tranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN));
    }
}
