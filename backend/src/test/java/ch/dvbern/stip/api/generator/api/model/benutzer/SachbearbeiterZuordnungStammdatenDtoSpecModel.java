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

package ch.dvbern.stip.api.generator.api.model.benutzer;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDtoSpec;

public class SachbearbeiterZuordnungStammdatenDtoSpecModel {
    public static List<SachbearbeiterZuordnungStammdatenListDtoSpec> sachbearbeiterZuordnungStammdatenListDtoSpecs(
        final int amount
    ) {
        return TestUtil.createUpdateDtoSpecs(SachbearbeiterZuordnungStammdatenListDtoSpec::new, (model) -> {
            model.setSachbearbeiter(UUID.randomUUID());
            model.setZuordnung(sachbearbeiterZuordnungStammdatenDtoSpec());
        }, amount);
    }

    public static SachbearbeiterZuordnungStammdatenDtoSpec sachbearbeiterZuordnungStammdatenDtoSpec() {
        return TestUtil.createUpdateDtoSpec(SachbearbeiterZuordnungStammdatenDtoSpec::new, (model) -> {
            model.setBuchstabenDe("A-D");
            model.setBuchstabenFr("A-C,E");
        });
    }
}
