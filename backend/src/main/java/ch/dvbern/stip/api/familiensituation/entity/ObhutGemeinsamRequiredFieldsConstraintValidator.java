package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_NULL_MESSAGE;

public class ObhutGemeinsamRequiredFieldsConstraintValidator
    implements ConstraintValidator<ObhutGemeinsamRequiredFieldsConstraint, Familiensituation> {

    @Override
    public boolean isValid(
        Familiensituation familiensituation,
        ConstraintValidatorContext constraintValidatorContext) {
        if (familiensituation.getObhut() == Elternschaftsteilung.GEMEINSAM) {
            return familiensituation.getObhutVater() != null && familiensituation.getObhutMutter() != null;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_NULL_MESSAGE)
            .addConstraintViolation();
        return familiensituation.getObhutVater() == null && familiensituation.getObhutMutter() == null;
    }
}
