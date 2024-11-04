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

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungsPensumDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;

import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public final class AusbildungUpdateDtoSpecModel {
    public static AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungUpdateDtoSpec::new, (model) -> {
            model.setAusbildungBegin(LocalDate.now().plusYears(1).with(firstDayOfYear()).format(DATE_TIME_FORMATTER));
            model.setAusbildungEnd(LocalDate.now().plusYears(1).with(lastDayOfYear()).format(DATE_TIME_FORMATTER));
            model.setAusbildungNichtGefunden(false);
            model.setAusbildungsgangId(TestConstants.TEST_AUSBILDUNGSGANG_ID);
            model.setFachrichtung("Informatik");
            model.setPensum(TestUtil.getRandomElementFromArray(AusbildungsPensumDtoSpec.values()));
            model.setIsAusbildungAusland(false);
            model.setAusbildungsort("Bern");
        });
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAusbildung() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setAusbildung(ausbildungUpdateDtoSpec())
        );
    }
}
