package ch.dvbern.stip.api.steuerdaten.service;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.enterprise.context.RequestScoped;

import static ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung.MUTTER;
import static ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung.VATER;

@RequestScoped
public class SteuerdatenTabBerechnungsService {

    public List<SteuerdatenTyp> calculateTabs(Familiensituation familiensituation) {
        List<SteuerdatenTyp> tabs = new ArrayList<>();

        if (isElternVerheiratetZusammen(familiensituation)) {
            tabs.add(SteuerdatenTyp.FAMILIE);
            return tabs;
        }

        if (isGerichtlicheAlimentenregelung(familiensituation)) {
            return addAlimentenRegelungTab(tabs, familiensituation);
        }

        if (isElternteilUnbekanntVerstorben(familiensituation)) {
            return addUnbekanntVerstorbenTab(tabs, familiensituation);
        }

        tabs.addAll(List.of(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER));
        return tabs;
    }

    private boolean isElternVerheiratetZusammen(Familiensituation familiensituation) {
        return familiensituation.getElternVerheiratetZusammen();
    }

    private boolean isGerichtlicheAlimentenregelung(Familiensituation familiensituation) {
        return familiensituation.getGerichtlicheAlimentenregelung();
    }

    private boolean isElternteilUnbekanntVerstorben(Familiensituation familiensituation) {
        return familiensituation.getElternteilUnbekanntVerstorben();
    }

    private List<SteuerdatenTyp> addAlimentenRegelungTab(List<SteuerdatenTyp> tabs, Familiensituation familiensituation) {
        if (familiensituation.getWerZahltAlimente() == VATER) {
            tabs.add(SteuerdatenTyp.VATER);
        } else if (familiensituation.getWerZahltAlimente() == MUTTER) {
            tabs.add(SteuerdatenTyp.MUTTER);
        } else {
            return new ArrayList<>();
        }
        return tabs;
    }

    private List<SteuerdatenTyp> addUnbekanntVerstorbenTab(List<SteuerdatenTyp> tabs, Familiensituation familiensituation) {
        if (isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben()) &&
            isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())) {
            return new ArrayList<>();
        }

        if (isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben())) {
            tabs.add(SteuerdatenTyp.VATER);
        } else if (isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())) {
            tabs.add(SteuerdatenTyp.MUTTER);
        }
        return tabs;
    }

    private boolean isParentUnbekanntVerstorben(ElternAbwesenheitsGrund parentStatus) {
        return parentStatus != null;
    }
}
