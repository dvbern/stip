package ch.dvbern.stip.api.notification.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.generated.dto.KommentarDto;
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

    @Transactional
    public void createNotification(final NotificationType notificationType, final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setGesuch(gesuch);
        final var pia = gesuch.getCurrentGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();
        String msg = Templates.getGesuchEingereichtText(anrede, nachname, sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createAenderungAbgelehntNotification(final Gesuch gesuch, final KommentarDto kommentarDto) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_ABGELEHNT)
            .setGesuch(gesuch);
        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        ;
        final var nachname = pia.getNachname();
        final String msg = Templates.getAenderungAbgelehnt(anrede, nachname, kommentarDto.getText(), sprache).render();
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
        public static TemplateInstance getGesuchEingereichtText(
            final String anrede,
            final String nachname,
            final Sprache korrespondenzSprache) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return gesuchEingereichtFR(anrede, nachname);
            }
            return gesuchEingereichtDE(anrede, nachname);
        }

        public static TemplateInstance getAenderungAbgelehnt(
            final String anrede,
            final String nachname,
            final String msg,
            final Sprache korrespondenzSprache) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return aenderungAbgelehntFR(anrede, nachname, msg);
            }
            return aenderungAbgelehntDE(anrede, nachname, msg);
        }

        public static native TemplateInstance gesuchEingereichtDE(final String anrede, final String nachname);

        public static native TemplateInstance gesuchEingereichtFR(final String anrede, final String nachname);

        public static native TemplateInstance aenderungAbgelehntDE(
            final String anrede,
            final String nachname,
            final String msg);

        public static native TemplateInstance aenderungAbgelehntFR(
            final String anrede,
            final String nachname,
            final String msg);
    }
}
