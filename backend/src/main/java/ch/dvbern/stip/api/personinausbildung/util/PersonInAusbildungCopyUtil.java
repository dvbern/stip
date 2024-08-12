package ch.dvbern.stip.api.personinausbildung.util;

import ch.dvbern.stip.api.common.util.AbstractFamilieEntityCopyUtil;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonInAusbildungCopyUtil {
    /**
     * Creates a copy ignoring properties that are a reference to another entity
     * */
    public PersonInAusbildung createCopyIgnoreReferences(final PersonInAusbildung other) {
        final var copy = new PersonInAusbildung();
        AbstractFamilieEntityCopyUtil.copy(other, copy);

        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setAnrede(other.getAnrede());
        copy.setIdentischerZivilrechtlicherWohnsitz(other.isIdentischerZivilrechtlicherWohnsitz());
        copy.setIdentischerZivilrechtlicherWohnsitzOrt(other.getIdentischerZivilrechtlicherWohnsitzOrt());
        copy.setIdentischerZivilrechtlicherWohnsitzPLZ(other.getIdentischerZivilrechtlicherWohnsitzPLZ());
        copy.setEmail(other.getEmail());
        copy.setTelefonnummer(other.getTelefonnummer());
        copy.setNationalitaet(other.getNationalitaet());
        copy.setHeimatort(other.getHeimatort());
        copy.setNiederlassungsstatus(other.getNiederlassungsstatus());
        copy.setEinreisedatum(other.getEinreisedatum());
        copy.setZivilstand(other.getZivilstand());
        copy.setSozialhilfebeitraege(other.isSozialhilfebeitraege());
        copy.setVormundschaft(other.isVormundschaft());
        copy.setKorrespondenzSprache(other.getKorrespondenzSprache());

        return copy;
    }
}
