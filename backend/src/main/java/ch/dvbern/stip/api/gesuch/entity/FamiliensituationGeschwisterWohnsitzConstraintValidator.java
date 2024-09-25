package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityWohnsitzValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationGeschwisterWohnsitzConstraintValidator
    implements ConstraintValidator<FamiliensituationGeschwisterWohnsitzConstraint, GesuchFormular>
{
    private String property = "";
    private AbstractFamilieEntityWohnsitzValidator validator;

    @Override
    public void initialize(FamiliensituationGeschwisterWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    public FamiliensituationGeschwisterWohnsitzConstraintValidator(){
        validator = new AbstractFamilieEntityWohnsitzValidator();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        return gesuchFormular.getGeschwisters().stream().allMatch(geschwister -> validator.isValid(geschwister,gesuchFormular.getFamiliensituation()));
    }


}
