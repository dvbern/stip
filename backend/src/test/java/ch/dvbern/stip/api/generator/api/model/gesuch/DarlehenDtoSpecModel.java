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
import ch.dvbern.stip.generated.dto.DarlehenDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class DarlehenDtoSpecModel {
    public static DarlehenDtoSpec darlehenDtoSpec() {
        return TestUtil.createUpdateDtoSpec(DarlehenDtoSpec::new, (model) -> {
            model.setWillDarlehen(true);
            model.setGrundZweitausbildung(true);
            model.setGrundAnschaffungenFuerAusbildung(false);
            model.setGrundNichtBerechtigt(false);
            model.setGrundHoheGebuehren(false);
            model.setGrundAusbildungZwoelfJahre(false);
            model.setBetragDarlehen(500);
            model.setAnzahlBetreibungen(1);
            model.setSchulden(0);
            model.setBetragBezogenKanton(20);
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecDarlehen() {
        return TestUtil
            .createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model) -> model.setDarlehen(darlehenDtoSpec()));
    }
}
