package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenZulagenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenZulagenRequiredConstraint, GesuchFormular> {
    private String property;

    @Override
    public void initialize(EinnahmenKostenZulagenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (!gesuchFormular.getKinds().isEmpty()) {
            if (gesuchFormular.getEinnahmenKosten().getZulagen() == null) {
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            } else {
                return true;
            }
        }
        return true;
    }
}
