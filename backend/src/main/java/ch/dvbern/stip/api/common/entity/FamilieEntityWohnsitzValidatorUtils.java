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

package ch.dvbern.stip.api.common.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FamilieEntityWohnsitzValidatorUtils {
    boolean getIsMutterAbsent(Familiensituation familiensituation) {
        return familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN
        || familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.UNBEKANNT;
    }

    boolean getIsMutterExisting(Familiensituation familiensituation) {
        return familiensituation.getMutterUnbekanntGrund() == null ||
        familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH;
    }

    boolean getIsVaterAbsent(Familiensituation familiensituation) {
        return familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.VERSTORBEN
        || familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.UNBEKANNT;
    }

    boolean getIsVaterExisting(Familiensituation familiensituation) {
        return familiensituation.getVaterUnbekanntGrund() == null ||
        familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH;
    }

    boolean getIsWohnsitzanteilVater100Percent(AbstractFamilieEntity familieEntity) {
        return familieEntity.getWohnsitzAnteilVater() != null
        && familieEntity.getWohnsitzAnteilVater()
            .compareTo(BigDecimal.valueOf(100)) == 0;
    }

    boolean getIsWohnsitzanteilMutter100Percent(AbstractFamilieEntity familieEntity) {
        return familieEntity.getWohnsitzAnteilMutter() != null
        && familieEntity.getWohnsitzAnteilMutter()
            .compareTo(BigDecimal.valueOf(100)) == 0;
    }
}
