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

import java.util.UUID;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;

public final class AdresseSpecModel {
    public static AdresseDtoSpec adresseDtoSpec() {
        return adresseDtoSpecForLand(TestConstants.TEST_LAND_SCHWEIZ_ID);
    }

    public static AdresseDtoSpec adresseWithoutIso2codeDtoSpec() {
        return adresseDtoSpecForLand(TestConstants.TEST_LAND_STATELESS_ID);
    }

    private static AdresseDtoSpec adresseDtoSpecForLand(final UUID landId) {
        return TestUtil.createUpdateDtoSpec(AdresseDtoSpec::new, (model) -> {
            model.setLandId(landId);
            model.setCoAdresse("");
            model.setStrasse("Nussbaumstrasse");
            model.setHausnummer("22");
            model.setPlz("3006");
            model.setOrt("Bern");
        });
    }
}
