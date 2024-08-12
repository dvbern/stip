package ch.dvbern.stip.api.adresse.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdresseCopyUtil {
    public Adresse createCopy(final Adresse other) {
        final var copy = new Adresse();

        copy.setLand(other.getLand());
        copy.setCoAdresse(other.getCoAdresse());
        copy.setStrasse(other.getStrasse());
        copy.setHausnummer(other.getHausnummer());
        copy.setPlz(other.getPlz());
        copy.setOrt(other.getOrt());

        return copy;
    }
}
