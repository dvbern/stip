package ch.dvbern.stip.api.steuerdaten.service;

import java.util.List;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.enterprise.context.RequestScoped;

import static ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung.MUTTER;
import static ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung.VATER;

@RequestScoped
public class SteuerdatenTabBerechnungsService {
    public List<SteuerdatenTyp> calculateTabs(final Familiensituation familiensituation) {
        if (isElternVerheiratetZusammen(familiensituation)) {
            return List.of(SteuerdatenTyp.FAMILIE);
        }

        if (isGerichtlicheAlimentenregelung(familiensituation)) {
            return getAlimentenregelungTabs(familiensituation);
        }

        if (isElternteilUnbekanntVerstorben(familiensituation)) {
            return getElternteilUnbekanntVerstorbenTabs(familiensituation);
        }

        return List.of(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER);
    }

    private boolean isElternVerheiratetZusammen(final Familiensituation familiensituation) {
        return familiensituation.getElternVerheiratetZusammen();
    }

    private boolean isGerichtlicheAlimentenregelung(final Familiensituation familiensituation) {
        return Boolean.TRUE.equals(familiensituation.getGerichtlicheAlimentenregelung());
    }

    private boolean isElternteilUnbekanntVerstorben(final Familiensituation familiensituation) {
        return Boolean.TRUE.equals(familiensituation.getElternteilUnbekanntVerstorben());
    }

    private List<SteuerdatenTyp> getAlimentenregelungTabs(final Familiensituation familiensituation) {
        if (familiensituation.getWerZahltAlimente() == VATER) {
            return List.of(SteuerdatenTyp.VATER);
        }

        if (familiensituation.getWerZahltAlimente() == MUTTER) {
            return List.of(SteuerdatenTyp.MUTTER);
        }

        return List.of();
    }

    private List<SteuerdatenTyp> getElternteilUnbekanntVerstorbenTabs(final Familiensituation familiensituation) {
        if (isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben()) &&
            isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())) {
            return List.of();
        }

        if (isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben())) {
            return List.of(SteuerdatenTyp.VATER);
        }

        if (isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())) {
            return List.of(SteuerdatenTyp.MUTTER);
        }

        return List.of();
    }

    private boolean isParentUnbekanntVerstorben(ElternAbwesenheitsGrund parentStatus) {
        return parentStatus != null;
    }
}
