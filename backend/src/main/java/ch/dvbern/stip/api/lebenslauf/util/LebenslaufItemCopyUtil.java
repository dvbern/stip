package ch.dvbern.stip.api.lebenslauf.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LebenslaufItemCopyUtil {
    public LebenslaufItem createCopy(final LebenslaufItem other) {
        final LebenslaufItem copy = new LebenslaufItem();

        copy.setBildungsart(other.getBildungsart());
        copy.setVon(other.getVon());
        copy.setBis(other.getBis());
        copy.setTaetigkeitsart(other.getTaetigkeitsart());
        copy.setTaetigkeitsBeschreibung(other.getTaetigkeitsBeschreibung());
        copy.setBerufsbezeichnung(other.getBerufsbezeichnung());
        copy.setFachrichtung(other.getFachrichtung());
        copy.setTitelDesAbschlusses(other.getTitelDesAbschlusses());
        copy.setAusbildungAbgeschlossen(other.isAusbildungAbgeschlossen());
        copy.setWohnsitz(other.getWohnsitz());

        return copy;
    }

    public Set<LebenslaufItem> createCopyOfSet(final Set<LebenslaufItem> other) {
        final var copy = new LinkedHashSet<LebenslaufItem>();
        for (final var lebenslaufItem : other) {
            copy.add(createCopy(lebenslaufItem));
        }

        return copy;
    }
}
