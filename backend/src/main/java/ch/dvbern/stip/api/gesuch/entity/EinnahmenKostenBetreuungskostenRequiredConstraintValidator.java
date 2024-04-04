package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenBetreuungskostenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenBetreuungskostenRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenBetreuungskostenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular value, ConstraintValidatorContext context) {
        if (value.getKinds().isEmpty()) {
            return true;
        }

        final var ek = value.getEinnahmenKosten();
        if (ek == null) {
            return true;
        }

        if (ek.getBetreuungskostenKinder() == null) {
            return GesuchValidatorUtil.addProperty(
                context,
                property
            );
        }

        return true;
    }
}
