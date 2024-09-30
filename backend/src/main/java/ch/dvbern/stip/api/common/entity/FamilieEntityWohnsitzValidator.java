package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FamilieEntityWohnsitzValidator {
    // map of valid Wohnsitz values when ElternteilUnbekanntVerstorben is true
    private static final Map<Wohnsitz, Optional<Boolean>> ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP = Map.of(
        // will be evaluated
        Wohnsitz.MUTTER_VATER,Optional.empty(),
        Wohnsitz.FAMILIE,Optional.of(false),
        // every other entry is valid = true
        Wohnsitz.EIGENER_HAUSHALT,Optional.of(true)
        );

    // map of valid Wohnsitz values when ElternVerheiratetZusammen is false
    private static final Map<Wohnsitz,Boolean> ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP = Map.of(
        Wohnsitz.FAMILIE,false,
        // every other entry is valid = true
        Wohnsitz.MUTTER_VATER,true,
        Wohnsitz.EIGENER_HAUSHALT,true
    );

    public boolean isValid(AbstractFamilieEntity familieEntity, Familiensituation familiensituation) {
        if((familiensituation == null) || (familieEntity == null)){
            return true;
        }
        if(familiensituation.getElternVerheiratetZusammen()){
            // when elterns are together or married, the option MUTTER_VATER is not available
            return familieEntity.getWohnsitz() != Wohnsitz.MUTTER_VATER;
        }
        if(familiensituation.getElternteilUnbekanntVerstorben()){
            return ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz())
                .orElseGet(() -> isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(familieEntity,familiensituation));
        }
        return ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz());
    }

    private boolean isWohnsitzanteilValidWhenOneEltnernteilIsAbsent(AbstractFamilieEntity familieEntity,
                                                                    Familiensituation familiensituation) {
        if(!familiensituation.getElternteilUnbekanntVerstorben()){
            return true;
        }

        boolean isAnteilMutter100Percent = familieEntity.getWohnsitzAnteilMutter()
            .equals(BigDecimal.valueOf(100));
        boolean isAnteilVater100Percent = familieEntity.getWohnsitzAnteilVater()
            .equals(BigDecimal.valueOf(100));

        final boolean isMutterAbsent = familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN
            || familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.UNBEKANNT;
        final boolean isVaterAbsent = familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.UNBEKANNT
            || familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN;
        final boolean isMutterExisting = familiensituation.getMutterUnbekanntGrund() == null ||
            familiensituation.getMutterUnbekanntVerstorben().equals(ElternAbwesenheitsGrund.WEDER_NOCH);
        final boolean isVaterExisting = familiensituation.getVaterUnbekanntGrund() == null
        || familiensituation.getVaterUnbekanntVerstorben().equals(ElternAbwesenheitsGrund.WEDER_NOCH);

            // mutter is absent - vater must be 100%
            if(isMutterAbsent && isVaterExisting){
                return isAnteilVater100Percent;
            }
            // vater is absent - mutter must be 100%
            else if(isVaterAbsent && isMutterExisting){
                    return isAnteilMutter100Percent;
            } else {
                // one of both has to be 100%
                return isAnteilMutter100Percent
                    || isAnteilVater100Percent;
            }
    }
}
