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

package ch.dvbern.stip.api.generator.entities.steuererklaerung;

import java.time.LocalDate;

import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.steuererklaerung.entity.SteuererklaerungBuilder;

public final class SteuererklaerungTestBuilder
extends AbstractTestBuilder<Steuererklaerung, SteuererklaerungTestBuilder> {
    SteuererklaerungTestBuilder(Steuererklaerung entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static SteuererklaerungTestBuilder empty(LocalDate referenceDate) {
        Steuererklaerung steuererklaerung = SteuererklaerungBuilder.steuererklaerung()
            .steuerdatenTyp(SteuerdatenTyp.FAMILIE)
            .steuererklaerungInBern(true)
            .ergaenzungsleistungen(0)
            .unterhaltsbeitraege(0)
            .renten(0)
            .einnahmenBGSA(0)
            .andereEinnahmen(0)
            .build();

        return new SteuererklaerungTestBuilder(steuererklaerung, referenceDate);
    }
}
