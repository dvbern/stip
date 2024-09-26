package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class AbstractFamilieEntityWohnsitzValidator {
    //todo: refactor method

    public boolean isValid(AbstractFamilieEntity personInAusbildung, Familiensituation familiensituation) {
        if (familiensituation != null) {
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
        boolean isAnteilMutter100Percent = familieEntity.getWohnsitzAnteilMutter().equals(BigDecimal.valueOf(100));
        boolean isAnteilVater100Percent = familieEntity.getWohnsitzAnteilVater().equals(BigDecimal.valueOf(100));

        if (familiensituation.getElternteilUnbekanntVerstorben()) {
            if(familiensituation.getVaterUnbekanntVerstorben() != null
            && familiensituation.getMutterUnbekanntVerstorben() != null){
                // one of both anteile has to be 100 %
                return isAnteilMutter100Percent
                        || isAnteilVater100Percent;
            }
            // mutter is absent: wohnsitzanteil vater has to be 100 %
            if (familiensituation.getMutterUnbekanntVerstorben() != null) {
                return isAnteilVater100Percent;
            }
            // vater is absent: : wohnsitzanteil mutter has to be 100 %
            if (familiensituation.getVaterUnbekanntVerstorben() != null) {
                return isAnteilMutter100Percent;
            }
        }
        return true;
    }
}
