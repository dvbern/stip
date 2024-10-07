package ch.dvbern.stip.api.common.entity;

import java.util.Map;
import java.util.Optional;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FamilieEntityWohnsitzValidator {
    // map of valid Wohnsitz values when ElternteilUnbekanntVerstorben is true
    private static final Map<Wohnsitz, Optional<Boolean>> ONE_ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP = Map.of(
        // will be evaluated
        Wohnsitz.MUTTER_VATER, Optional.empty(),
        Wohnsitz.FAMILIE, Optional.of(false),
        // every other entry is valid = true
        Wohnsitz.EIGENER_HAUSHALT, Optional.of(true)
    );

    // map of valid Wohnsitz values when ElternVerheiratetZusammen is false
    private static final Map<Wohnsitz, Boolean> ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP = Map.of(
        Wohnsitz.FAMILIE, false,
        // every other entry is valid = true
        Wohnsitz.MUTTER_VATER, true,
        Wohnsitz.EIGENER_HAUSHALT, true
    );

    public boolean isValid(AbstractFamilieEntity familieEntity, Familiensituation familiensituation) {
        if ((familiensituation == null) || (familieEntity == null)) {
            return true;
        }

        boolean elternTogetherOrMarried = Boolean.TRUE.equals(familiensituation.getElternVerheiratetZusammen());
        boolean oneOrMoreElternteilAbsent = Boolean.TRUE.equals(familiensituation.getElternteilUnbekanntVerstorben());

        if (elternTogetherOrMarried) {
            return isWohnsitzValidWhenElternTogetherOrMarried(familieEntity);
        }

        if (oneOrMoreElternteilAbsent) {
            return isWohnsitzValidWhenOneElternteilIsAbsent(familieEntity, familiensituation);
        }
        return isWohnsitzValidWhenElternSeparated(familieEntity);
    }

    private boolean isWohnsitzValidWhenElternTogetherOrMarried(AbstractFamilieEntity familieEntity) {
        return familieEntity.getWohnsitz() != Wohnsitz.MUTTER_VATER;
    }

    private boolean isWohnsitzValidWhenElternSeparated(AbstractFamilieEntity familieEntity) {
        return ELTERN_SEPARATED_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz());
    }

    private boolean isWohnsitzValidWhenOneElternteilIsAbsent(
        AbstractFamilieEntity familieEntity,
        Familiensituation familiensituation
    ) {
        boolean bothElternteilsDead =
            familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN &&
            familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN;

        if (bothElternteilsDead) {
            return familieEntity.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT;
        }
        return ONE_ELTERNTEIL_ABSENT_WOHNSITUATION_VALID_MAP.get(familieEntity.getWohnsitz())
            .orElseGet(() -> isWohnsitzanteilValidWhenOneElternteilIsAbsent(familieEntity, familiensituation));
    }

    private boolean isWohnsitzanteilValidWhenOneElternteilIsAbsent(
        AbstractFamilieEntity familieEntity,
        Familiensituation familiensituation
    ) {
        if (!familiensituation.getElternteilUnbekanntVerstorben()) {
            return true;
        }

        boolean isAnteilMutter100Percent =
            FamilieEntityWohnsitzValidatorUtils.getIsWohnsitzanteilMutter100Percent(familieEntity);
        boolean isAnteilVater100Percent =
            FamilieEntityWohnsitzValidatorUtils.getIsWohnsitzanteilVater100Percent(familieEntity);

        final boolean isMutterAbsent = FamilieEntityWohnsitzValidatorUtils.getIsMutterAbsent(familiensituation);
        final boolean isVaterAbsent = FamilieEntityWohnsitzValidatorUtils.getIsVaterAbsent(familiensituation);

        final boolean isMutterExisting = FamilieEntityWohnsitzValidatorUtils.getIsMutterExisting(familiensituation);
        final boolean isVaterExisting = FamilieEntityWohnsitzValidatorUtils.getIsVaterExisting(familiensituation);

        // mutter is absent - vater must be 100%
        if (isMutterAbsent && isVaterExisting) {
            return isAnteilVater100Percent;
        }
        // vater is absent - mutter must be 100%
        else if (isVaterAbsent && isMutterExisting) {
            return isAnteilMutter100Percent;
        } else {
            // one of both has to be 100%
            return isAnteilMutter100Percent
                || isAnteilVater100Percent;
        }
    }

}
