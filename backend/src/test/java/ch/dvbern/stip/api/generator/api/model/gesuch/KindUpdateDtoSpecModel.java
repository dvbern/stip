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

package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungssituationDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KindUpdateDtoSpec;

public class KindUpdateDtoSpecModel {
    public static List<KindUpdateDtoSpec> kindUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(KindUpdateDtoSpec::new, (model) -> {
            model.setVorname("Test");
            model.setNachname("Kind");
            model.setGeburtsdatum(
                TestUtil.getRandomLocalDateBetween(
                    LocalDate.of(1990, 1, 1),
                    LocalDate.of(2002, 1, 1)
                )
            );
            model.setAusbildungssituation(TestUtil.getRandomElementFromArray(AusbildungssituationDtoSpec.values()));
            model.setWohnsitzAnteilPia(TestUtil.getRandomInt(0, 100));
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecKinder() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setKinds(kindUpdateDtoSpecs())
        );
    }
}
