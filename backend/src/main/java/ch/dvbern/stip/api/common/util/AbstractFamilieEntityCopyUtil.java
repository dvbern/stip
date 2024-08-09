package ch.dvbern.stip.api.common.util;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AbstractFamilieEntityCopyUtil {
    /**
     * Copies all properties from a given source {@link AbstractFamilieEntity} to a target {@link AbstractFamilieEntity}
     * Including all properties from the base {@link AbstractPerson}
     * */
    public void copy(final AbstractFamilieEntity source, final AbstractFamilieEntity target) {
        AbstractPersonCopyUtil.copy(source, target);
        target.setWohnsitz(source.getWohnsitz());
        target.setWohnsitzAnteilMutter(source.getWohnsitzAnteilMutter());
        target.setWohnsitzAnteilVater(source.getWohnsitzAnteilVater());
    }
}
