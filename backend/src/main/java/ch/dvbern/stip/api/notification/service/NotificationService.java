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
    public void createGesuchEingereichtNotification(final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.GESUCH_EINGEREICHT)
            .setGesuch(gesuch);
        final var pia = gesuch.getCurrentGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();

        String msg = Templates.getGesuchEingereichtText(anrede,nachname,sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createGesuchStatusChangeWithCommentNotification(final Gesuch gesuch, final KommentarDto kommentar) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.GESUCH_STATUS_CHANGE_WITH_COMMENT)
            .setGesuch(gesuch);

        final var pia = gesuch.getCurrentGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();
        String msg = Templates.getGesuchStatusChangeWithKommentarText(anrede, nachname, kommentar.getText(), sprache).render();
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

    public void createMissingDocumentNotification(final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.FEHLENDE_DOKUMENTE)
            .setGesuch(gesuch);
        final var pia = gesuch.getCurrentGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();

        String msg = Templates.getGesuchFehlendeDokumenteText(sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @CheckedTemplate
    public static class Templates {
        public static TemplateInstance getGesuchFehlendeDokumenteText(Sprache korrespondenzSprache) {
            if(korrespondenzSprache.equals(Sprache.FRANZOESISCH)){
                return gesuchFehlendeDokumenteFR();
            }
            return gesuchFehlendeDokumenteDE();
        }
        public static TemplateInstance getGesuchEingereichtText(String anrede, String nachname, Sprache korrespondenzSprache) {
            if(korrespondenzSprache.equals(Sprache.FRANZOESISCH)){
                return gesuchEingereichtFR(anrede, nachname);
            }
            return gesuchEingereichtDE(anrede, nachname);
        }
        public static TemplateInstance getGesuchStatusChangeWithKommentarText(String anrede, String nachname, String kommentar, Sprache korrespondenzSprache) {
            if(korrespondenzSprache.equals(Sprache.FRANZOESISCH)){
                return gesuchStatusChangeWithKommentarFR(anrede, nachname, kommentar);
            }
            return gesuchStatusChangeWithKommentarDE(anrede, nachname, kommentar);
        }
        public static native TemplateInstance gesuchEingereichtDE(String anrede, String nachname);
        public static native TemplateInstance gesuchEingereichtFR(String anrede, String nachname);
        public static native TemplateInstance gesuchStatusChangeWithKommentarDE(String anrede, String nachname, String kommentar);
        public static native TemplateInstance gesuchStatusChangeWithKommentarFR(String anrede, String nachname, String kommentar);
        public static native TemplateInstance gesuchFehlendeDokumenteDE();
        public static native TemplateInstance gesuchFehlendeDokumenteFR();

    }
}
