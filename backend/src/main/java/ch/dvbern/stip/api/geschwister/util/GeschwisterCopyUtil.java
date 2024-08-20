package ch.dvbern.stip.api.geschwister.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractFamilieEntityCopyUtil;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeschwisterCopyUtil {
    public Geschwister createCopy(final Geschwister other) {
        final var copy = new Geschwister();

        AbstractFamilieEntityCopyUtil.copy(other, copy);
        copy.setAusbildungssituation(other.getAusbildungssituation());

        return copy;
    }

    public Set<Geschwister> createCopyOfSet(final Set<Geschwister> geschwisters) {
        final var copy = new LinkedHashSet<Geschwister>();
        for (final var geschwister : geschwisters) {
            copy.add(createCopy(geschwister));
        }

        return copy;
    }
}
