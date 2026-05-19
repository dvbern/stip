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

package ch.dvbern.stip.api.generator.entities.familiensituation;

import java.time.LocalDate;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.entity.FamiliensituationBuilder;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;

public final class FamiliensituationTestBuilder
extends AbstractTestBuilder<Familiensituation, FamiliensituationTestBuilder> {
    FamiliensituationTestBuilder(Familiensituation entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static FamiliensituationTestBuilder withoutEltern(LocalDate referenceDate) {
        Familiensituation familiensituation = FamiliensituationBuilder.familiensituation()
            .elternVerheiratetZusammen(false)
            .elternteilUnbekanntVerstorben(true)
            .gerichtlicheAlimentenregelung(false)
            .mutterUnbekanntVerstorben(ElternAbwesenheitsGrund.UNBEKANNT)
            .mutterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
            .mutterWiederverheiratet(null)
            .vaterUnbekanntVerstorben(ElternAbwesenheitsGrund.UNBEKANNT)
            .vaterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
            .vaterWiederverheiratet(null)
            .werZahltAlimente(null)
            .build();

        return new FamiliensituationTestBuilder(familiensituation, referenceDate);
    }
}
