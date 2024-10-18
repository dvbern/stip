package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class NotificationTemplateUtils {
    private static final Map<Sprache,String> ANREDE_FRAU_MAP = Map.of(
        Sprache.FRANZOESISCH, "Chère Madame",
        Sprache.DEUTSCH, "Sehr geehrte Frau"
    );
    private static final Map<Sprache,String> ANREDE_HERR_MAP = Map.of(
        Sprache.FRANZOESISCH, "Cher Monsieur",
        Sprache.DEUTSCH, "Sehr geehrter Herr"
    );
    public String getAnredeText(Anrede anrede, Sprache korrespondenzSprache){
        switch (anrede){
            case FRAU : return ANREDE_FRAU_MAP.get(korrespondenzSprache);
            default : return ANREDE_HERR_MAP.get(korrespondenzSprache);
        }
    }
}
