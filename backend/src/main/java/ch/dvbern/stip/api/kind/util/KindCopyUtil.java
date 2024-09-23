package ch.dvbern.stip.api.kind.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.kind.entity.Kind;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KindCopyUtil {
    public Kind createCopy(final Kind other) {
        final Kind copy = new Kind();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setWohnsitzAnteilPia(other.getWohnsitzAnteilPia());
        copy.setAusbildungssituation(other.getAusbildungssituation());
        copy.setErhalteneAlimentebeitraege(other.getErhalteneAlimentebeitraege());

        return copy;
    }

    public Set<Kind> createCopySet(final Set<Kind> other) {
        final var copy = new LinkedHashSet<Kind>();
        for (final var kind : other) {
            copy.add(createCopy(kind));
        }

        return copy;
    }
}
