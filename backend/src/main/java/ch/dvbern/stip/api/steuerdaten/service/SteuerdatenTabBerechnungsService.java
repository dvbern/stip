/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
        // We need boxed equality as we can only assume it isn't null if it's been saved to/ loaded from the DB
        if (Boolean.TRUE.equals(familiensituation.getElternVerheiratetZusammen())) {
            return List.of(SteuerdatenTyp.FAMILIE);
        }

        if (Boolean.TRUE.equals(familiensituation.getGerichtlicheAlimentenregelung())) {
            return getAlimentenregelungTabs(familiensituation);
        }

        if (Boolean.TRUE.equals(familiensituation.getElternteilUnbekanntVerstorben())) {
            return getElternteilUnbekanntVerstorbenTabs(familiensituation);
        }

        return List.of(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER);
    }

    private List<SteuerdatenTyp> getAlimentenregelungTabs(final Familiensituation familiensituation) {
        if (familiensituation.getWerZahltAlimente() == VATER) {
            return List.of(SteuerdatenTyp.MUTTER);
        }

        if (familiensituation.getWerZahltAlimente() == MUTTER) {
            return List.of(SteuerdatenTyp.VATER);
        }

        return List.of();
    }

    private List<SteuerdatenTyp> getElternteilUnbekanntVerstorbenTabs(final Familiensituation familiensituation) {
        if (
            isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben()) &&
            isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())
        ) {
            return List.of();
        }

        if (isParentUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben())) {
            return List.of(SteuerdatenTyp.VATER);
        }

        if (isParentUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben())) {
            return List.of(SteuerdatenTyp.MUTTER);
        }

        return List.of(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER);
    }

    private boolean isParentUnbekanntVerstorben(final ElternAbwesenheitsGrund parentStatus) {
        return parentStatus == null
        || parentStatus == ElternAbwesenheitsGrund.VERSTORBEN
        || parentStatus == ElternAbwesenheitsGrund.UNBEKANNT;
    }
}
