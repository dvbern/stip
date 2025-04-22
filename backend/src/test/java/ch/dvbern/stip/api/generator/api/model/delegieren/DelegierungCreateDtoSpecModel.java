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

package ch.dvbern.stip.api.generator.api.model.delegieren;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AnredeDtoSpec;
import ch.dvbern.stip.generated.dto.DelegierungCreateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DelegierungCreateDtoSpecModel {
    public DelegierungCreateDtoSpec delegierungCreateDto() {
        return TestUtil.createUpdateDtoSpec(DelegierungCreateDtoSpec::new, model -> {
            model.setAnrede(AnredeDtoSpec.HERR);
            model.setNachname("Test");
            model.setVorname("Test");
            model.setGeburtsdatum(LocalDate.now().minusYears(16));
            model.adresse(AdresseSpecModel.adresseDtoSpec());
        });
    }
}
