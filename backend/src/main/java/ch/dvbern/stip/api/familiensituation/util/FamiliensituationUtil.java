package ch.dvbern.stip.api.familiensituation.util;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FamiliensituationUtil {
    public boolean isElternteilOfTypRequired(final FamiliensituationUpdateDto updateDto, final ElternTyp ofTyp) {
        return isElternteilOfTypRequiredImpl(
            ofTyp,
            updateDto.getElternVerheiratetZusammen(),
            updateDto.getGerichtlicheAlimentenregelung(),
            updateDto.getWerZahltAlimente(),
            updateDto.getElternteilUnbekanntVerstorben(),
            updateDto.getVaterUnbekanntVerstorben(),
            updateDto.getMutterUnbekanntVerstorben()
        );
    }

    public boolean isElternteilOfTypRequired(final Familiensituation familiensituation, final ElternTyp ofTyp) {
        return isElternteilOfTypRequiredImpl(
            ofTyp,
            familiensituation.getElternVerheiratetZusammen(),
            familiensituation.getGerichtlicheAlimentenregelung(),
            familiensituation.getWerZahltAlimente(),
            familiensituation.getElternteilUnbekanntVerstorben(),
            familiensituation.getVaterUnbekanntVerstorben(),
            familiensituation.getMutterUnbekanntVerstorben()
        );
    }

    private static boolean isElternteilOfTypRequiredImpl(
        final ElternTyp ofTyp,
        final Boolean elternVerheiratetZusammen,
        final Boolean gerichtlicheAlimentenregelung,
        final Elternschaftsteilung werZahltAlimente,
        final Boolean elternteilUnbekanntVerstorben,
        final ElternAbwesenheitsGrund vaterUnbekanntVerstorben,
        final ElternAbwesenheitsGrund mutterUnbekanntVerstorben
    ) {
        if (Boolean.TRUE.equals(elternVerheiratetZusammen)) {
            return true;
        }

        if (Boolean.TRUE.equals(gerichtlicheAlimentenregelung)) {
            return switch (werZahltAlimente) {
                case VATER -> ofTyp == ElternTyp.MUTTER;
                case MUTTER -> ofTyp == ElternTyp.VATER;
                case GEMEINSAM -> false;
            };
        }

        if (Boolean.TRUE.equals(elternteilUnbekanntVerstorben)) {
            return switch (ofTyp) {
                case VATER -> vaterUnbekanntVerstorben == ElternAbwesenheitsGrund.WEDER_NOCH;
                case MUTTER -> mutterUnbekanntVerstorben == ElternAbwesenheitsGrund.WEDER_NOCH;
            };
        }

        return true;
    }
}
