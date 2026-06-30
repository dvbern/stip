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

package ch.dvbern.stip.berechnung.dto.v1;

import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternUtil;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
public class FamiliensituationV1 {
    Boolean elternVerheiratetZusammen;
    Boolean gerichtlicheAlimentenregelung;
    Elternschaftsteilung werZahltAlimente;
    Boolean elternteilUnbekanntVerstorben;
    Boolean vaterUnbekanntVerstorben;
    Boolean mutterUnbekanntVerstorben;
    Boolean vaterWiederverheiratet;
    Boolean mutterWiederverheiratet;

    public static FamiliensituationV1 fromFamiliensituation(
        final Familiensituation familiensituation,
        final Set<Eltern> elterns
    ) {
        return new FamiliensituationV1Builder()
            .elternVerheiratetZusammen(familiensituation.getElternVerheiratetZusammen())
            .gerichtlicheAlimentenregelung(
                Objects.requireNonNullElse(familiensituation.getGerichtlicheAlimentenregelung(), false)
            )
            .werZahltAlimente(
                Objects.requireNonNullElse(familiensituation.getWerZahltAlimente(), Elternschaftsteilung.GEMEINSAM)
            )
            .elternteilUnbekanntVerstorben(
                Objects.requireNonNullElse(familiensituation.getElternteilUnbekanntVerstorben(), false)
            )
            .vaterUnbekanntVerstorben(
                Objects.requireNonNullElse(
                    familiensituation.getVaterUnbekanntVerstorben(),
                    ElternAbwesenheitsGrund.WEDER_NOCH
                ) != ElternAbwesenheitsGrund.WEDER_NOCH
            )
            .mutterUnbekanntVerstorben(
                Objects.requireNonNullElse(
                    familiensituation.getMutterUnbekanntVerstorben(),
                    ElternAbwesenheitsGrund.WEDER_NOCH
                ) != ElternAbwesenheitsGrund.WEDER_NOCH
            )
            .vaterWiederverheiratet(
                ElternUtil.getElternByType(elterns, ElternTyp.VATER).map(Eltern::getWiederverheiratet).orElse(false)
            )
            .mutterWiederverheiratet(
                ElternUtil.getElternByType(elterns, ElternTyp.MUTTER).map(Eltern::getWiederverheiratet).orElse(false)
            )
            .build();
    }
}
