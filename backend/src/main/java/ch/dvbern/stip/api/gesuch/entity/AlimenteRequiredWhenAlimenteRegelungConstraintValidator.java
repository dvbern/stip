package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALIMENTE_NULL_WHEN_NO_ALIMENTEREGELUNG;

public class AlimenteRequiredWhenAlimenteRegelungConstraintValidator
    implements ConstraintValidator<AlimenteRequiredWhenAlimenteregelungConstraint, GesuchFormular> {
    private String property;

    @Override
    public void initialize(AlimenteRequiredWhenAlimenteregelungConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
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

        if (gesuchFormular.getEinnahmenKosten().getAlimente() != null) {
            return GesuchValidatorUtil.addProperty(
                constraintValidatorContext,
                VALIDATION_ALIMENTE_NULL_WHEN_NO_ALIMENTEREGELUNG,
                property
            );
        }

        return true;
    }

}
