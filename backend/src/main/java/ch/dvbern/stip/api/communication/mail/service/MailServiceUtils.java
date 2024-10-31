package ch.dvbern.stip.api.communication.mail.service;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailServiceUtils {
    public void sendStandardNotificationEmailForGesuch(MailService mailService, Gesuch gesuch){
        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();
        mailService.sendStandardNotificationEmail(
            pia.getNachname(),
            pia.getVorname(),
            pia.getEmail(),
            AppLanguages.fromLocale(pia.getKorrespondenzSprache().getLocale())
        );
    }
}
