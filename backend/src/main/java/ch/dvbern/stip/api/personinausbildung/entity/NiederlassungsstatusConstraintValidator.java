package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE;

public class NiederlassungsstatusConstraintValidator implements ConstraintValidator<NiederlassungsstatusRequiredConstraint, PersonInAusbildung> {

    @Override
    public boolean isValid(
        PersonInAusbildung personInAusbildung,
        ConstraintValidatorContext constraintValidatorContext) {
        if (personInAusbildung.getNationalitaet() != Land.CH) {
            return personInAusbildung.getNiederlassungsstatus() != null;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE)
            .addConstraintViolation();
        return personInAusbildung.getNiederlassungsstatus() == null;
    }
}
