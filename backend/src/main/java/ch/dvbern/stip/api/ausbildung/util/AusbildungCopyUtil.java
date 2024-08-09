package ch.dvbern.stip.api.ausbildung.util;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AusbildungCopyUtil {
    /**
     * Creates a copy including Stammdaten references
     * */
    public Ausbildung createCopyIncludingStammdatenReferences(final Ausbildung other) {
        final var copy = new Ausbildung();

        copy.setAusbildungsgang(other.getAusbildungsgang());
        copy.setAlternativeAusbildungsgang(other.getAlternativeAusbildungsgang());
        copy.setAlternativeAusbildungsstaette(other.getAlternativeAusbildungsstaette());
        copy.setFachrichtung(other.getFachrichtung());
        copy.setAusbildungNichtGefunden(other.isAusbildungNichtGefunden());
        copy.setAusbildungBegin(other.getAusbildungBegin());
        copy.setAusbildungEnd(other.getAusbildungEnd());
        copy.setPensum(other.getPensum());
        copy.setAusbildungsort(other.getAusbildungsort());
        copy.setIsAusbildungAusland(other.getIsAusbildungAusland());

        return copy;
    }
}
