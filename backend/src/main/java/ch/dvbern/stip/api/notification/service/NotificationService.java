package ch.dvbern.stip.api.notification.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.generated.dto.NotificationDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final BenutzerService benutzerService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    //private final Template gesuchEingereichtDE;

    @Transactional
    public void createNotification(final NotificationType notificationType, final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setGesuch(gesuch);
        final var pia = gesuch.getCurrentGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = pia.getAnrede();
        final var nachname = pia.getNachname();
        String msg = Templates.gesuchEingereichtDE(NotificationTemplateUtils.getAnredeText(anrede),nachname).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }



    @Transactional(TxType.REQUIRES_NEW)
    public void deleteNotificationsForGesuch(final UUID gesuchId) {
        notificationRepository.deleteAllForGesuch(gesuchId);
    }

    @Transactional
    public List<NotificationDto> getNotificationsForCurrentUser() {
        return notificationRepository.getAllForUser(
            benutzerService.getCurrentBenutzer().getId()
        ).map(notificationMapper::toDto)
        .toList();
    }
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance gesuchEingereichtDE(String anrede,String nachname);
        public static native TemplateInstance gesuchEingereichtFR(String anrede,String nachname);

    }
}
