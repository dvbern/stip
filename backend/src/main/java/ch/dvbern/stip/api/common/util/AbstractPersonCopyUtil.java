package ch.dvbern.stip.api.common.util;

import ch.dvbern.stip.api.common.entity.AbstractPerson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AbstractPersonCopyUtil {
    public void copy(final AbstractPerson source, final AbstractPerson target) {
        target.setNachname(source.getNachname());
        target.setVorname(source.getVorname());
        target.setGeburtsdatum(source.getGeburtsdatum());
    }
}
