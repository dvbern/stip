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

package ch.dvbern.stip.berechnung.service.v1;

import java.util.List;

import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.service.StipendienCalculator;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;

@CalculatorVersion(major = 1, minor = 0)
public class StipendienCalculatorV1 implements StipendienCalculator {
    @Override
    public BerechnungResult calculateStipendien(final DmnRequest model) {
        if (model instanceof BerechnungRequestV1 v1) {
            return calculateStipendien(v1);
        }

        throw new IllegalArgumentException("The passed BerechnungRequest was not the correct version");
    }

    public BerechnungResult calculateStipendien(final BerechnungRequestV1 model) {
        final var familienbudgets = calculateFamilienbudgets(model);
        final var persoenlichesBudget =
            calculatePersoenlichesBudgetresult(model, familienbudgets.get(0), familienbudgets.get(1));

        return new BerechnungResult(
            persoenlichesBudget.getPersoenlichesbudgetBerechnet(),
            familienbudgets,
            persoenlichesBudget
        );
    }

    private List<FamilienBudgetresultatDto> calculateFamilienbudgets(final BerechnungRequestV1 model) {
        final var one = FamilienbudgetCalculatorV1.calculateFamilienbudget(
            model.getInputFamilienbudget1(),
            model.getStammdaten()
        );

        final var two = FamilienbudgetCalculatorV1.calculateFamilienbudget(
            model.getInputFamilienbudget2(),
            model.getStammdaten()
        );

        return List.of(one, two);
    }

    private PersoenlichesBudgetresultatDto calculatePersoenlichesBudgetresult(
        final BerechnungRequestV1 model,
        final FamilienBudgetresultatDto familienBudgetresultat1,
        final FamilienBudgetresultatDto familienBudgetresultat2
    ) {
        return PersoenlichesBudgetCalculatorV1.calculatePersoenlichesBudget(
            model.getInputPersoenlichesBudget(),
            familienBudgetresultat1,
            familienBudgetresultat2,
            model.getStammdaten()
        );
    }
}
