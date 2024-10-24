package ch.dvbern.stip.api.common.authorization.util;

import java.util.Objects;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorizerUtil {
    public boolean isGesuchstellerOfGesuch(final Benutzer currentBenutzer, final Gesuch gesuch) {
        return Objects.equals(
            gesuch.getAusbildung().getFall().getGesuchsteller().getId(),
            currentBenutzer.getId()
        );
    }
}
