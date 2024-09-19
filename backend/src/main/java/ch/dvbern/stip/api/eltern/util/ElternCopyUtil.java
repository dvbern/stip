package ch.dvbern.stip.api.eltern.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ElternCopyUtil {
    public Eltern createCopyWithoutReferences(final Eltern other) {
        final var copy = new Eltern();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setAdresse(other.getAdresse());
        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setSozialhilfebeitraege(other.getSozialhilfebeitraege());
        copy.setErgaenzungsleistungen(other.getErgaenzungsleistungen());
        copy.setElternTyp(other.getElternTyp());
        copy.setTelefonnummer(other.getTelefonnummer());
        copy.setAusweisbFluechtling(other.getAusweisbFluechtling());
        copy.setIdentischerZivilrechtlicherWohnsitz(other.isIdentischerZivilrechtlicherWohnsitz());
        copy.setIdentischerZivilrechtlicherWohnsitzOrt(other.getIdentischerZivilrechtlicherWohnsitzOrt());
        copy.setIdentischerZivilrechtlicherWohnsitzPLZ(other.getIdentischerZivilrechtlicherWohnsitzPLZ());
        return copy;
    }

    public Set<Eltern> createCopyOfSetWithoutReferences(final Set<Eltern> other) {
        final var copy = new LinkedHashSet<Eltern>();
        for (final var eltern : other) {
            copy.add(createCopyWithoutReferences(eltern));
        }

        return copy;
    }
}
