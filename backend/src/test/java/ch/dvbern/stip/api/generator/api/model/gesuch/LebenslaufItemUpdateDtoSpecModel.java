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

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufAusbildungsArtDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzKantonDtoSpec;

import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public final class LebenslaufItemUpdateDtoSpecModel {
    public static List<LebenslaufItemUpdateDtoSpec> lebenslaufItemUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(LebenslaufItemUpdateDtoSpec::new, (model) -> {
            model.setWohnsitz(TestUtil.getRandomElementFromArray(WohnsitzKantonDtoSpec.values()));
            model.setAusbildungAbgeschlossen(true);
            model.setFachrichtung("Testrichtung");
            model.setTaetigkeitsBeschreibung("Ein Test");
            model.setVon(LocalDate.now().withMonth(8).withDayOfMonth(1).minusYears(1).format(DATE_TIME_FORMATTER));
            model.setBis(LocalDate.now().with(lastDayOfYear()).format(DATE_TIME_FORMATTER));
            model.setBildungsart(LebenslaufAusbildungsArtDtoSpec.MASTER);
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecLebenslauf() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setLebenslaufItems(lebenslaufItemUpdateDtoSpecs())
        );
    }
}
