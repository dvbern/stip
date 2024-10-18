package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.common.type.Anrede;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationTemplateUtils {
    public String getAnredeText(Anrede anrede){
        switch (anrede){
            case FRAU : return "Sehr geehrte Frau".concat(" ");
            default : return "Sehr geehrter Herr".concat(" ");
        }
    }
}
