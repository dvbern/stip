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

package ch.dvbern.stip.berechnung.service.bern.v1;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.service.CalculatorMandant;
import ch.dvbern.stip.berechnung.service.StipendienCalculator;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import jakarta.inject.Singleton;

@Singleton
@CalculatorVersion(major = 1, minor = 0)
@CalculatorMandant(MandantIdentifier.BERN)
public class StipendienCalculatorV1 implements StipendienCalculator {
    @Override
    public BerechnungResult calculateStipendien(final CalculatorRequest model) {
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
            persoenlichesBudget.getTotal(),
            0, // TODO KSTIP-2584: add correct value once it is calculated
            familienbudgets.stream().flatMap(Optional::stream).toList(),
            persoenlichesBudget
        );
    }

    private List<Optional<FamilienBudgetresultatDto>> calculateFamilienbudgets(final BerechnungRequestV1 model) {
        return Stream.of(model.getInputFamilienbudget1(), model.getInputFamilienbudget2())
            .map(
                budget -> FamilienbudgetCalculatorV1.calculateFamilienbudget(
                    budget,

                    model.getInputPersoenlichesBudget().getAntragssteller(),
                    model.getStammdaten()
                )
            )
            .map(Optional::ofNullable)
            .toList();
    }

    private PersoenlichesBudgetresultatDto calculatePersoenlichesBudgetresult(
        final BerechnungRequestV1 model,
        final Optional<FamilienBudgetresultatDto> familienBudgetresultat1,
        final Optional<FamilienBudgetresultatDto> familienBudgetresultat2
    ) {
        return PersoenlichesBudgetCalculatorV1.calculatePersoenlichesBudget(
            model.getInputPersoenlichesBudget(),
            familienBudgetresultat1,
            familienBudgetresultat2,
            model.getStammdaten()
        );
    }
}
