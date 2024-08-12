package ch.dvbern.stip.api.familiensituation.util;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FamiliensituationCopyUtil {
    public Familiensituation createCopy(final Familiensituation other) {
        final var copy = new Familiensituation();

        copy.setElternVerheiratetZusammen(other.getElternVerheiratetZusammen());
        copy.setElternteilUnbekanntVerstorben(other.getElternteilUnbekanntVerstorben());
        copy.setGerichtlicheAlimentenregelung(other.getGerichtlicheAlimentenregelung());
        copy.setMutterUnbekanntVerstorben(other.getMutterUnbekanntVerstorben());
        copy.setMutterUnbekanntGrund(other.getMutterUnbekanntGrund());
        copy.setMutterWiederverheiratet(other.getMutterWiederverheiratet());
        copy.setVaterUnbekanntVerstorben(other.getVaterUnbekanntVerstorben());
        copy.setVaterUnbekanntGrund(other.getVaterUnbekanntGrund());
        copy.setVaterWiederverheiratet(other.getVaterWiederverheiratet());
        copy.setSorgerecht(other.getSorgerecht());
        copy.setObhut(other.getObhut());
        copy.setObhutMutter(other.getObhutMutter());
        copy.setObhutVater(other.getObhutVater());
        copy.setWerZahltAlimente(other.getWerZahltAlimente());

        return copy;
    }
}
