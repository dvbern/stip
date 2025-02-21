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
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
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

    public void overrideItem(final GesuchFormular source, final GesuchFormular target) {
        if (source.getFamiliensituation() == null) {
            target.setFamiliensituation(null);
            return;
        }

        final var sourceFamsit = source.getFamiliensituation();
        final var targetFamsit = target.getFamiliensituation();

        if (Objects.nonNull(sourceFamsit.getElternVerheiratetZusammen())) {
            targetFamsit.setElternVerheiratetZusammen(sourceFamsit.getElternVerheiratetZusammen());
        }

        if (Objects.nonNull(sourceFamsit.getElternteilUnbekanntVerstorben())) {
            targetFamsit.setElternteilUnbekanntVerstorben(sourceFamsit.getElternteilUnbekanntVerstorben());
        }

        if (Objects.nonNull(sourceFamsit.getGerichtlicheAlimentenregelung())) {
            targetFamsit.setGerichtlicheAlimentenregelung(sourceFamsit.getGerichtlicheAlimentenregelung());
        }

        if (Objects.nonNull(sourceFamsit.getMutterUnbekanntVerstorben())) {
            targetFamsit.setMutterUnbekanntVerstorben(sourceFamsit.getMutterUnbekanntVerstorben());
        }

        targetFamsit.setMutterUnbekanntGrund(sourceFamsit.getMutterUnbekanntGrund());
        targetFamsit.setVaterUnbekanntGrund(sourceFamsit.getVaterUnbekanntGrund());
        targetFamsit.setWerZahltAlimente(sourceFamsit.getWerZahltAlimente());

        if (Objects.nonNull(sourceFamsit.getMutterWiederverheiratet())) {
            targetFamsit.setMutterWiederverheiratet(sourceFamsit.getMutterWiederverheiratet());
        }
        if (Objects.nonNull(sourceFamsit.getVaterUnbekanntVerstorben())) {
            targetFamsit.setVaterUnbekanntVerstorben(sourceFamsit.getVaterUnbekanntVerstorben());
        }
        if (Objects.nonNull(sourceFamsit.getVaterWiederverheiratet())) {
            targetFamsit.setVaterWiederverheiratet(sourceFamsit.getVaterWiederverheiratet());
        }
    }

}
