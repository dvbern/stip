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

package ch.dvbern.stip.api.generator.api.model.land;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.LandDtoSpec;

public final class LandDtoSpecModel {
    public static LandDtoSpec landDtoSpec() {
        return TestUtil.createUpdateDtoSpec(LandDtoSpec::new, (model) -> {
            model.setIsEuEfta(true);
            model.setLaendercodeBfs("7900");
            model.setIso3code("YYY");
            model.setDeKurzform("Testing Land");
            model.setFrKurzform("Testing Land");
            model.setItKurzform("Testing Land");
            model.setEnKurzform("Testing Land");
            model.setEintragGueltig(true);
        });
    }
}
