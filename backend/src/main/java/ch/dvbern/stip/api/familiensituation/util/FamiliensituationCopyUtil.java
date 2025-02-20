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

package ch.dvbern.stip.api.familiensituation.util;

import java.util.Objects;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FamiliensituationCopyUtil {
    public Familiensituation createCopy(final Familiensituation other) {
        final var copy = new Familiensituation();

        copy.setElternVerheiratetZusammen(other.getElternVerheiratetZusammen());
        copy.setElternteilUnbekanntVerstorben(other.getElternteilUnbekanntVerstorben());
        copy.setGerichtlicheAlimentenregelung(other.getGerichtlicheAlimentenregelung());
        copy.setMutterUnbekanntVerstorben(other.getMutterUnbekanntVerstorben());
        copy.setMutterUnbekanntGrund(other.getMutterUnbekanntGrund());
        copy.setMutterWiederverheiratet(other.getMutterWiederverheiratet());
        copy.setVaterUnbekanntVerstorben(other.getVaterUnbekanntVerstorben());
        copy.setVaterUnbekanntGrund(other.getVaterUnbekanntGrund());
        copy.setVaterWiederverheiratet(other.getVaterWiederverheiratet());
        copy.setWerZahltAlimente(other.getWerZahltAlimente());

        return copy;
    }

    public void overrideItem(final Familiensituation replacement, Familiensituation toBeReplaced) {
        if (Objects.isNull(replacement)) {
            return;
        }
        if (Objects.nonNull(replacement.getElternVerheiratetZusammen())) {
            toBeReplaced.setElternVerheiratetZusammen(replacement.getElternVerheiratetZusammen());
        }

        if (Objects.nonNull(replacement.getElternteilUnbekanntVerstorben())) {
            toBeReplaced.setElternteilUnbekanntVerstorben(replacement.getElternteilUnbekanntVerstorben());
        }

        if (Objects.nonNull(replacement.getGerichtlicheAlimentenregelung())) {
            toBeReplaced.setGerichtlicheAlimentenregelung(replacement.getGerichtlicheAlimentenregelung());
        }

        if (Objects.nonNull(replacement.getMutterUnbekanntVerstorben())) {
            toBeReplaced.setMutterUnbekanntVerstorben(replacement.getMutterUnbekanntVerstorben());
        }

        toBeReplaced.setMutterUnbekanntGrund(replacement.getMutterUnbekanntGrund());
        toBeReplaced.setVaterUnbekanntGrund(replacement.getVaterUnbekanntGrund());
        toBeReplaced.setWerZahltAlimente(replacement.getWerZahltAlimente());

        if (Objects.nonNull(replacement.getMutterWiederverheiratet())) {
            toBeReplaced.setMutterWiederverheiratet(replacement.getMutterWiederverheiratet());
        }
        if (Objects.nonNull(replacement.getVaterUnbekanntVerstorben())) {
            toBeReplaced.setVaterUnbekanntVerstorben(replacement.getVaterUnbekanntVerstorben());
        }
        if (Objects.nonNull(replacement.getVaterWiederverheiratet())) {
            toBeReplaced.setVaterWiederverheiratet(replacement.getVaterWiederverheiratet());
        }
    }

}
