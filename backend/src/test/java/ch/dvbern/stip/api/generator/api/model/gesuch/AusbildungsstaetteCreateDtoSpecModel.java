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

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDtoSpec;

public class AusbildungsstaetteCreateDtoSpecModel {
    public static AusbildungsstaetteCreateDtoSpec ausbildungsstaetteCreateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungsstaetteCreateDtoSpec::new, (model) -> {
            model.setNameDe("Uni Bern");
            model.setNameFr("Uni Bern");
        });
    }

    public static AusbildungsstaetteCreateDtoSpec ausbildungsstaetteCreateDtoSpec2() {
        return TestUtil.createUpdateDtoSpec(AusbildungsstaetteCreateDtoSpec::new, (model) -> {
            model.setNameDe("Uni Zürich");
            model.setNameFr("Uni Zürich");
        });
    }

}
