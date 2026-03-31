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

package ch.dvbern.stip.api.generator.entities.lebenslauf;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemBuilder;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;

public final class LebenslaufItemTestBuilder extends AbstractTestBuilder<LebenslaufItem, LebenslaufItemTestBuilder> {
    LebenslaufItemTestBuilder(LebenslaufItem entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static LebenslaufItemTestBuilder empty(LocalDate referenceDate) {
        LebenslaufItem lebenslaufItem = LebenslaufItemBuilder.lebenslaufItem()
            .abschluss(null)
            .von(referenceDate.minusYears(2))
            .bis(referenceDate)
            .taetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
            .taetigkeitsBeschreibung("Test work")
            .fachrichtungBerufsbezeichnung("Testing")
            .ausbildungAbgeschlossen(true)
            .wohnsitz(WohnsitzKanton.BE)
            .copyOfId(null)
            .build();

        return new LebenslaufItemTestBuilder(lebenslaufItem, referenceDate);
    }
}
