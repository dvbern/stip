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
            model.setNettoerwerbseinkommen(TestUtil.getRandomInt(1, 2500));
            model.setZulagen(TestUtil.getRandomInt(1, 2500));
            model.setRenten(TestUtil.getRandomInt(1, 2500));
            model.setEoLeistungen(TestUtil.getRandomInt(1, 2500));
            model.setErgaenzungsleistungen(TestUtil.getRandomInt(1, 2500));
            model.setBeitraege(Math.min(TestUtil.getRandomInt(1, 2500), 1));
            model.setAusbildungskostenSekundarstufeZwei(TestUtil.getRandomInt(1, 2500));
            model.setAusbildungskostenTertiaerstufe(TestUtil.getRandomInt(1, 2500));
            model.setFahrkosten(TestUtil.getRandomInt(1, 2500));
            model.setWohnkosten(TestUtil.getRandomInt(1, 2500));
            model.setVerdienstRealisiert(false);
            model.setWillDarlehen(false);
            model.setAuswaertigeMittagessenProWoche(3);
            model.setBetreuungskostenKinder(TestUtil.getRandomInt(1, 2500));
            model.setVeranlagungsCode(0);
            model.setSteuerjahr(0);
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecEinnahmenKosten() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setEinnahmenKosten(einnahmenKostenUpdateDtoSpec())
        );
    }
}
