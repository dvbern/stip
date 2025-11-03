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
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class EinnahmenKostenUpdateDtoSpecModel {
    public static EinnahmenKostenUpdateDtoSpec einnahmenKostenUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(EinnahmenKostenUpdateDtoSpec::new, (model) -> {
            model.setNettoerwerbseinkommen(500);
            model.setArbeitspensumProzent(100);
            model.setZulagen(500);
            model.setRenten(500);
            model.setEoLeistungen(500);
            model.setErgaenzungsleistungen(500);
            model.setBeitraege(Math.min(2500, 1));
            model.setAusbildungskosten(2500);
            model.setFahrkosten(2500);
            model.setWohnkosten(2500);
            model.setWgWohnend(false);
            model.setAuswaertigeMittagessenProWoche(3);
            model.setBetreuungskostenKinder(TestUtil.getRandomInt(1, 2500));
            model.setSteuerjahr(0);
            model.setVeranlagungsStatus("test");
            model.setAlternativeWohnformWohnend(false);
        });
    }

    public static EinnahmenKostenUpdateDtoSpec einnahmenKostenPartnerUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(EinnahmenKostenUpdateDtoSpec::new, (model) -> {
            model.setNettoerwerbseinkommen(500);
            model.setArbeitspensumProzent(100);
            model.setZulagen(500);
            model.setRenten(500);
            model.setEoLeistungen(500);
            model.setVermoegen(1000);
            model.setErgaenzungsleistungen(500);
            model.setBeitraege(Math.min(2500, 1));
            model.setFahrkosten(2500);
            model.setVerpflegungskosten(3);
            model.setBetreuungskostenKinder(TestUtil.getRandomInt(1, 2500));
            model.setSteuerjahr(0);
            model.setVeranlagungsStatus("test");
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKosten() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setEinnahmenKosten(einnahmenKostenUpdateDtoSpec())
        );
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKostenPartner() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setEinnahmenKostenPartner(einnahmenKostenPartnerUpdateDtoSpec())
        );
    }
}
