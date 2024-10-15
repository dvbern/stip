package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.FamilieEntityWohnsitzValidator;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationPersonInAusbildungWohnsitzConstraintValidator
    implements ConstraintValidator<FamiliensituationPersonInAusbildungWohnsitzConstraint, GesuchFormular> {
    private String property = "";
    @Inject
    FamilieEntityWohnsitzValidator validator;

    @Override
    public void initialize(FamiliensituationPersonInAusbildungWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        final var isValid =
            validator.isValid(gesuchFormular.getPersonInAusbildung(), gesuchFormular.getFamiliensituation());

        if (isValid) {
            return true;
        }

        return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
    }

}
