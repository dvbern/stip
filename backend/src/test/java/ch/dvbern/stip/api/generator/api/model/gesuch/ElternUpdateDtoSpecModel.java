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
import java.util.List;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

public class ElternUpdateDtoSpecModel {
    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs(final int amount) {
        return TestUtil.createUpdateDtoSpecs(ElternUpdateDtoSpec::new, (model) -> {
            model.setAdresse(AdresseSpecModel.adresseDtoSpec());
            model.setVorname("Test");
            model.setNachname("Elternteil");
            model.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATTER);
            model.setElternTyp(ElternTypDtoSpec.VATER);
            model.setWohnkosten(1);
            model.setSozialhilfebeitraege(true);
            model.setErgaenzungsleistungen(1);
            model.setGeburtsdatum(
                TestUtil.getRandomLocalDateBetween(LocalDate.of(1920, 1, 1), LocalDate.of(2002, 1, 1))
            );
            model.setIdentischerZivilrechtlicherWohnsitz(false);
            model.setIdentischerZivilrechtlicherWohnsitzOrt("Bern");
            model.setIdentischerZivilrechtlicherWohnsitzPLZ("3011");
            model.setTelefonnummer("+41 79 111 11 11");
            model.setAusweisbFluechtling(false);
        }, amount);
    }

    public static List<ElternUpdateDtoSpec> elternUpdateDtoSpecs() {
        return elternUpdateDtoSpecs(1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecElterns() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setElterns(elternUpdateDtoSpecs())
        );
    }
}
