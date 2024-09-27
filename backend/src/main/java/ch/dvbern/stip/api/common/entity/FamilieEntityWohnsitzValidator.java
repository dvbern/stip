package ch.dvbern.stip.api.common.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FamilieEntityWohnsitzValidator {

    private static final Map<Wohnsitz, Optional<Boolean>> ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP = Map.of(
        // will be evaluated
        Wohnsitz.MUTTER_VATER,Optional.empty(),
        Wohnsitz.FAMILIE,Optional.of(false),
        // every other entry is valid = true
        Wohnsitz.EIGENER_HAUSHALT,Optional.of(true)
        );
    private static final Map<Wohnsitz,Boolean> ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP = Map.of(
        Wohnsitz.FAMILIE,false,
        // every other entry is valid = true
        Wohnsitz.MUTTER_VATER,true,
        Wohnsitz.EIGENER_HAUSHALT,true
    );

    public boolean isValid(AbstractFamilieEntity familieEntity, Familiensituation familiensituation) {
        if ((familiensituation != null) && (familieEntity != null)) {
            if(!familiensituation.getElternVerheiratetZusammen().booleanValue()){
                if(familiensituation.getElternteilUnbekanntVerstorben().booleanValue()){
                    return ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz()).orElseGet(() -> isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(familieEntity,familiensituation));
                }else{
                    return ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz()).booleanValue();
                }
            }else{
                // when elterns are together or married, the option MUTTER_VATER is not available
                return familieEntity.getWohnsitz() != Wohnsitz.MUTTER_VATER;
            }
        }
        return true;
    }

    private boolean isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(AbstractFamilieEntity familieEntity, Familiensituation familiensituation) {
        boolean isAnteilMutter100Percent = familieEntity.getWohnsitzAnteilMutter().equals(BigDecimal.valueOf(100));
        boolean isAnteilVater100Percent = familieEntity.getWohnsitzAnteilVater().equals(BigDecimal.valueOf(100));

        if (familiensituation.getElternteilUnbekanntVerstorben().booleanValue()) {
            // both parents absent: one of both anteile has to be 100 %
            if(familiensituation.getVaterUnbekanntVerstorben() != null
            && familiensituation.getMutterUnbekanntVerstorben() != null){
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
