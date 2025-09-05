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

package ch.dvbern.stip.berechnung.service.dv.v1;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.service.CalculatorMandant;
import ch.dvbern.stip.berechnung.service.StipendienCalculator;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@CalculatorVersion(major = 1, minor = 0)
@CalculatorMandant(MandantIdentifier.DV)
public class StipendienCalculatorV1 implements StipendienCalculator {
    private final ch.dvbern.stip.berechnung.service.bern.v1.StipendienCalculatorV1 stipendienCalculatorBern;

    @Override
    public BerechnungResult calculateStipendien(CalculatorRequest model) {
        return stipendienCalculatorBern.calculateStipendien(model);
    }
}
