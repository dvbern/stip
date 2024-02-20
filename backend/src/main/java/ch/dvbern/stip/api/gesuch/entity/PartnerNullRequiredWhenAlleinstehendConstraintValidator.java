package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE;

public class PartnerNullRequiredWhenAlleinstehendConstraintValidator
    implements ConstraintValidator<PartnerNullRequiredWhenAlleinstehendConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null) {
            return true;
        }
        if (gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
            && gesuchFormular.getPartner() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE)
                .addConstraintViolation();
            return false;
        }

        return gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
            || gesuchFormular.getPartner() == null;
    }
}
