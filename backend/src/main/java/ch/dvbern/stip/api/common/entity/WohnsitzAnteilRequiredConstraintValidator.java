package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE;

public class WohnsitzAnteilRequiredConstraintValidator
    implements ConstraintValidator<WohnsitzAnteilRequiredConstraint, AbstractFamilieEntity> {

    @Override
    public boolean isValid(
        AbstractFamilieEntity abstractFamilieEntity,
        ConstraintValidatorContext constraintValidatorContext) {
        if (abstractFamilieEntity.getWohnsitz() == Wohnsitz.MUTTER_VATER) {
            return abstractFamilieEntity.getWohnsitzAnteilVater() != null
                && abstractFamilieEntity.getWohnsitzAnteilMutter() != null;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE)
            .addConstraintViolation();
        return abstractFamilieEntity.getWohnsitzAnteilVater() == null
            && abstractFamilieEntity.getWohnsitzAnteilMutter() == null;
    }
}
