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
}
