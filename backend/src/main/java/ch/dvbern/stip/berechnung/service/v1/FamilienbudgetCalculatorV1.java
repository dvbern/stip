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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputFamilienbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.ElternteilV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;
import static java.lang.Math.max;

@UtilityClass
public class FamilienbudgetCalculatorV1 {
    public FamilienBudgetresultatDto calculateFamilienbudget(
        final InputFamilienbudgetV1 input,
        final StammdatenV1 stammdaten
    ) {
        if (input == null) {
            return null;
        }

        final var result = new FamilienBudgetresultatDto();
        final var elternteil = input.getElternteil();

        calculateAndSetAusgaben(result, elternteil);
        calculateAndSetEinnahmen(result, elternteil, stammdaten);
        calculateAndSetFamilienbudgetBerechnet(result);
        result.setAnzahlGeschwisterInAusbildung(elternteil.getAnzahlGeschwisterInAusbildung());
        result.setAnzahlPersonenImHaushalt(elternteil.getAnzahlPersonenImHaushalt());

        return result;
    }

    private void calculateAndSetAusgaben(final FamilienBudgetresultatDto result, final ElternteilV1 elternteil) {
        final var toApply = List.of(
            mapAndReturn(FamilienBudgetresultatDto::setGrundbedarf, elternteil.getGrundbedarf()),
            mapAndReturn(FamilienBudgetresultatDto::setEffektiveWohnkosten, elternteil.getEffektiveWohnkosten()),
            mapAndReturn(
                FamilienBudgetresultatDto::setMedizinischeGrundversorgung,
                elternteil.getMedizinischeGrundversorgung()
            ),
            mapAndReturn(FamilienBudgetresultatDto::setSteuernKantonGemeinde, elternteil.getSteuernStaat()),
            mapAndReturn(FamilienBudgetresultatDto::setSteuernBund, elternteil.getSteuernBund()),
            mapAndReturn(FamilienBudgetresultatDto::setIntegrationszulage, elternteil.getIntegrationszulage()),
            mapAndReturn(FamilienBudgetresultatDto::setFahrkostenPerson1, elternteil.getFahrkostenPerson1()),
            mapAndReturn(FamilienBudgetresultatDto::setFahrkostenPerson2, elternteil.getFahrkostenPerson2()),
            mapAndReturn(FamilienBudgetresultatDto::setEssenskostenPerson1, elternteil.getEssenskostenPerson1()),
            mapAndReturn(FamilienBudgetresultatDto::setEssenskostenPerson2, elternteil.getEssenskostenPerson2())
        );

        final var ausgaben = applyAndSum(toApply, result);
        result.setAusgabenFamilienbudget(ausgaben);
    }

    private void calculateAndSetEinnahmen(
        final FamilienBudgetresultatDto result,
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        final var summands = List.of(
            mapAndReturn(FamilienBudgetresultatDto::setErgaenzungsleistungen, elternteil.getErgaenzungsleistungen()),
            mapAndReturn(FamilienBudgetresultatDto::setTotalEinkuenfte, elternteil.getTotalEinkuenfte())
        );

        final var subtrahends = List.of(
            mapAndReturn(FamilienBudgetresultatDto::setEigenmietwert, elternteil.getEigenmietwert()),
            mapAndReturn(FamilienBudgetresultatDto::setAlimente, elternteil.getAlimente())
        );

        final List<Function<FamilienBudgetresultatDto, Integer>> conditionalSummands;
        final List<Function<FamilienBudgetresultatDto, Integer>> conditionalSubtrahends;
        if (elternteil.isSelbststaendigErwerbend()) {
            conditionalSubtrahends = List.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSaeule3a,
                    max(elternteil.getEinzahlungSaeule3a() - stammdaten.getMaxSaeule3a(), 0)
                ),
                mapAndReturn(FamilienBudgetresultatDto::setSaeule2, elternteil.getEinzahlungSaeule2())
            );

            conditionalSummands = List.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSteuerbaresVermoegen,
                    roundHalfUp(
                        BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent())
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                            .multiply(
                                BigDecimal.valueOf(
                                    max(elternteil.getSteuerbaresVermoegen() - stammdaten.getFreibetragVermoegen(), 0)
                                )
                            )
                    )
                )
            );
        } else {
            conditionalSubtrahends = List.of(
                mapAndReturn(FamilienBudgetresultatDto::setSaeule3a, 0),
                mapAndReturn(FamilienBudgetresultatDto::setSaeule2, 0)
            );

            conditionalSummands = List.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSteuerbaresVermoegen,
                    roundHalfUp(
                        BigDecimal.valueOf(elternteil.getSteuerbaresVermoegen())
                            .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                    )
                )
            );
        }

        final var summand =
            applyAndSum(Stream.concat(summands.stream(), conditionalSummands.stream()).toList(), result);
        final var subtrahend =
            applyAndSum(Stream.concat(subtrahends.stream(), conditionalSubtrahends.stream()).toList(), result);

        final var einnahmen = Math.max(summand - subtrahend - stammdaten.getEinkommensfreibetrag(), 0);
        result.setEinnahmenFamilienbudget(einnahmen);
    }

    private void calculateAndSetFamilienbudgetBerechnet(final FamilienBudgetresultatDto result) {
        result.setFamilienbudgetBerechnet(
            roundHalfUp(
                BigDecimal.valueOf(result.getEinnahmenFamilienbudget())
                    .subtract(BigDecimal.valueOf(result.getAusgabenFamilienbudget()))
            )
        );
    }

    private Function<FamilienBudgetresultatDto, Integer> mapAndReturn(
        final BiConsumer<FamilienBudgetresultatDto, Integer> setter,
        final int value
    ) {
        return CalculatorUtilV1.mapAndReturn(setter, value);
    }

    private int applyAndSum(
        final List<Function<FamilienBudgetresultatDto, Integer>> toApply,
        final FamilienBudgetresultatDto result
    ) {
        return CalculatorUtilV1.applyAndSum(
            toApply.stream(),
            result
        );
    }
}
