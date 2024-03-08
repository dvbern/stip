package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE;

public class PartnerNullRequiredWhenAlleinstehendConstraintValidator
    implements ConstraintValidator<PartnerNullRequiredWhenAlleinstehendConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(PartnerNullRequiredWhenAlleinstehendConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null) {
            return true;
        }
        if (gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
            && gesuchFormular.getPartner() == null) {
            return GesuchValidatorUtil.addProperty(
                constraintValidatorContext,
                VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE,
                property
            );
        }

		if (gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
				|| gesuchFormular.getPartner() == null) {
			return true;
		} else {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
		}
    }
}
