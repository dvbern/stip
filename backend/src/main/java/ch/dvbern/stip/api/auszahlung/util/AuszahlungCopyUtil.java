package ch.dvbern.stip.api.auszahlung.util;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuszahlungCopyUtil {
    public Auszahlung createCopyIgnoreReferences(final Auszahlung other) {
        final var copy = new Auszahlung();

        copy.setKontoinhaber(other.getKontoinhaber());
        copy.setVorname(other.getVorname());
        copy.setNachname(other.getNachname());
        copy.setIban(other.getIban());

        return copy;
    }
}
