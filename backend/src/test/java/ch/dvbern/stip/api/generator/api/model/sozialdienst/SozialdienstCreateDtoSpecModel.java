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

package ch.dvbern.stip.api.generator.api.model.sozialdienst;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SozialdienstCreateDtoSpecModel {
    private static final String VALID_IBAN = "CH5089144653587876648";

    public SozialdienstCreateDtoSpec sozialdienstCreateDtoSpec(
        final SozialdienstAdminDtoSpec admin,
        final AdresseDtoSpec adresse
    ) {
        return TestUtil.createUpdateDtoSpec(SozialdienstCreateDtoSpec::new, model -> {
            model.setSozialdienstAdmin(admin);
            model.setAdresse(adresse);
            model.setName("Test Sozialdienst");
            model.setIban(VALID_IBAN);
        });
    }
}
