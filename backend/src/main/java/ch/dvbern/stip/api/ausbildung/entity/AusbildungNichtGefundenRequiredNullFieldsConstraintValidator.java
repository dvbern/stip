package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE;

public class AusbildungNichtGefundenRequiredNullFieldsConstraintValidator implements ConstraintValidator<AusbildungNichtGefundenRequiredNullFieldsConstraint, Ausbildung> {

    @Override
    public boolean isValid(
        Ausbildung ausbildung,
        ConstraintValidatorContext constraintValidatorContext) {
        if (ausbildung.isAusbildungNichtGefunden()) {
            return ausbildung.getAusbildungsgang() == null;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE)
            .addConstraintViolation();
        return ausbildung.getAlternativeAusbildungsstaette() == null && ausbildung.getAlternativeAusbildungsgang() == null;

    }
}
