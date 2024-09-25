package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityWohnsitzValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationPersonInAusbildungWohnsitzConstraintValidator
    implements ConstraintValidator<FamiliensituationPersonInAusbildungWohnsitzConstraint, GesuchFormular>
{
    private String property = "";
    private AbstractFamilieEntityWohnsitzValidator validator;

    @Override
    public void initialize(FamiliensituationPersonInAusbildungWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    public FamiliensituationPersonInAusbildungWohnsitzConstraintValidator(){
        validator = new AbstractFamilieEntityWohnsitzValidator();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(gesuchFormular.getPersonInAusbildung(), gesuchFormular.getFamiliensituation());
    }


}
