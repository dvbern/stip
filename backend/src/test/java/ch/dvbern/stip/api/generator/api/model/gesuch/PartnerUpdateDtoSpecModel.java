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

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PARTNER;

public class PartnerUpdateDtoSpecModel {
    public static PartnerUpdateDtoSpec partnerUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec::new, (model) -> {
            model.setVorname("Test");
            model.setNachname("Partner");
            model.setGeburtsdatum(
                TestUtil.getRandomLocalDateBetween(
                    LocalDate.of(1990, 1, 1),
                    LocalDate.of(2002, 1, 1)
                )
            );
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setSozialversicherungsnummer(AHV_NUMMER_VALID_PARTNER);
            model.setAusbildungMitEinkommenOderErwerbstaetig(true);
            model.setJahreseinkommen(5000);
            model.setFahrkosten(2500);
            model.setVerpflegungskosten(TestUtil.getRandomInt(1, 2000));
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecPartner() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setPartner(partnerUpdateDtoSpec())
        );
    }
}
