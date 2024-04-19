package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenAlimenteRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenAlimenteRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenAlimenteRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getFamiliensituation() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }

        final var alimentenregelung = gesuchFormular.getFamiliensituation().getGerichtlicheAlimentenregelung();
        if (alimentenregelung != null && alimentenregelung) {
            if (gesuchFormular.getEinnahmenKosten().getAlimente() == null) {
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            } else {
                return true;
            }
        }
        return true;
    }

}
