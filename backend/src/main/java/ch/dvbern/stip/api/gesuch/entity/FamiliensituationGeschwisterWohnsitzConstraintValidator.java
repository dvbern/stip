package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.FamilieEntityWohnsitzValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationGeschwisterWohnsitzConstraintValidator
    implements ConstraintValidator<FamiliensituationGeschwisterWohnsitzConstraint, GesuchFormular>
{
    private String property = "";
    private FamilieEntityWohnsitzValidator validator;

    @Override
    public void initialize(FamiliensituationGeschwisterWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    public FamiliensituationGeschwisterWohnsitzConstraintValidator(){
        validator = new FamilieEntityWohnsitzValidator();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        return gesuchFormular.getGeschwisters().stream().allMatch(geschwister -> validator.isValid(geschwister,gesuchFormular.getFamiliensituation()));
    }


}
