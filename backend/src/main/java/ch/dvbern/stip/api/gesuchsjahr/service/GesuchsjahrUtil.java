package ch.dvbern.stip.api.gesuchsjahr.service;

import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchsjahrUtil {
    public Integer getDefaultSteuerjahr(final Gesuchsjahr gesuchsjahr) {
        return gesuchsjahr.getTechnischesJahr() - 1;
    }
}
