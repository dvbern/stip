package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator
    implements ConstraintValidator<EinreisedatumRequiredIfNiederlassungsstatusConstraint, PersonInAusbildung> {

    @Override
    public boolean isValid(PersonInAusbildung personInAusbildung, ConstraintValidatorContext context) {
        if (personInAusbildung.getNiederlassungsstatus() == Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B) {
            return personInAusbildung.getEinreisedatum() != null;
        }

        return true;
    }
}
