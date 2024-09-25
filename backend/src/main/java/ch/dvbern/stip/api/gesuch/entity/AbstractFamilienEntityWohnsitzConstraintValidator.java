package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AbstractFamilienEntityWohnsitzConstraintValidator
    implements ConstraintValidator<AbstractFamilieEntityWohnsitzConstraint, GesuchFormular>
{
    private String property = "";

    @Override
    public void initialize(AbstractFamilieEntityWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        Familiensituation familiensituation = gesuchFormular.getFamiliensituation();
        if (familiensituation != null) {
            AbstractFamilieEntity personInAusbildung = gesuchFormular.getPersonInAusbildung();
            if (personInAusbildung != null) {
                if(!familiensituation.getElternVerheiratetZusammen()){
                    if(familiensituation.getElternteilUnbekanntVerstorben()){
                        switch(personInAusbildung.getWohnsitz()){
                            // validate wohnsitzanteil
                            case MUTTER_VATER: return isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(personInAusbildung,familiensituation);
                            // option is not valid
                            case FAMILIE: return false;
                            // all other options are valid
                            default: return true;
                        }
                    }else{
                        switch(personInAusbildung.getWohnsitz()){
                            // validate wohnsitzanteil
                            case MUTTER_VATER: return true;
                            // option is not valid
                            case FAMILIE: return false;
                            // all other options are valid
                            default: return true;
                        }
                    }


                }else{
                    // every wohnsituation option is allowed, but wohnsitzanteile have to be null
                    return personInAusbildung.getWohnsitzAnteilMutter() == null && personInAusbildung.getWohnsitzAnteilVater() == null;
                }

            }
        }
        return true;
    }

    private boolean isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(AbstractFamilieEntity familieEntity, Familiensituation familiensituation) {
        if (familiensituation.getElternteilUnbekanntVerstorben()) {
            // mutter is absent: wohnsitzanteil vater is not null
            if (familiensituation.getMutterUnbekanntVerstorben() != null) {
                return (familieEntity.getWohnsitzAnteilMutter() == null) && (familieEntity.getWohnsitzAnteilVater() != null);
            }
            // vater is absent: : wohnsitzanteil mutter is not null
            if (familiensituation.getVaterUnbekanntVerstorben() != null) {
                return (familieEntity.getWohnsitzAnteilVater() == null) && (familieEntity.getWohnsitzAnteilMutter() != null);

            }
        }
        return true;
    }
}
