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

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KontoinhaberDtoSpec;

public final class AuszahlungUpdateDtoSpecModel {
    public static AuszahlungUpdateDtoSpec auszahlungUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AuszahlungUpdateDtoSpec::new, (model) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
            model.setVorname("John");
            model.setNachname("Doe");
            model.setKontoinhaber(KontoinhaberDtoSpec.SOZIALDIENST_INSTITUTION);
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAuszahlung() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setAuszahlung(AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec())
        );
    }
}
