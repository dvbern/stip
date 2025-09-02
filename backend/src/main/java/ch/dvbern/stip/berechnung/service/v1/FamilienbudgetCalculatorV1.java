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
            mapAndReturn(FamilienBudgetresultatDto::setGrundbedarf, e -> e.getGrundbedarf()),
            mapAndReturn(FamilienBudgetresultatDto::setEffektiveWohnkosten, e -> e.getEffektiveWohnkosten()),
            mapAndReturn(
                FamilienBudgetresultatDto::setMedizinischeGrundversorgung,
                e -> e.getMedizinischeGrundversorgung()
            ),
            mapAndReturn(FamilienBudgetresultatDto::setSteuernBund, e -> e.getSteuernBund()),
            mapAndReturn(FamilienBudgetresultatDto::setSteuernKantonGemeinde, e -> e.getSteuernStaat()),
            mapAndReturn(FamilienBudgetresultatDto::setSteuernBund, e -> e.getSteuernBund()),
            mapAndReturn(FamilienBudgetresultatDto::setIntegrationszulage, e -> e.getIntegrationszulage()),
            mapAndReturn(FamilienBudgetresultatDto::setFahrkostenPerson1, e -> e.getFahrkostenPerson1()),
            mapAndReturn(FamilienBudgetresultatDto::setFahrkostenPerson2, e -> e.getFahrkostenPerson2()),
            mapAndReturn(FamilienBudgetresultatDto::setEssenskostenPerson1, e -> e.getEssenskostenPerson1()),
            mapAndReturn(FamilienBudgetresultatDto::setEssenskostenPerson2, e -> e.getEssenskostenPerson2())
        );

        final var ausgaben = applyAndSum(toApply, result, elternteil);
        result.setAusgabenFamilienbudget(ausgaben);
    }

    private void calculateAndSetEinnahmen(
        final FamilienBudgetresultatDto result,
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        final var summands = Stream.of(
            mapAndReturn(FamilienBudgetresultatDto::setErgaenzungsleistungen, e -> e.getErgaenzungsleistungen()),
            mapAndReturn(FamilienBudgetresultatDto::setTotalEinkuenfte, e -> e.getTotalEinkuenfte())
        );

        final var subtrahends = Stream.of(
            mapAndReturn(FamilienBudgetresultatDto::setEigenmietwert, e -> e.getEigenmietwert()),
            mapAndReturn(FamilienBudgetresultatDto::setAlimente, e -> e.getAlimente())
        );

        final Stream<FamilienbudgetFunction> conditionalSummands;
        final Stream<FamilienbudgetFunction> conditionalSubtrahends;
        if (elternteil.isSelbststaendigErwerbend()) {
            conditionalSubtrahends = Stream.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSaeule3a,
                    e -> max(e.getEinzahlungSaeule3a() - stammdaten.getMaxSaeule3a(), 0)
                ),
                mapAndReturn(FamilienBudgetresultatDto::setSaeule2, e -> e.getEinzahlungSaeule2())
            );

            conditionalSummands = Stream.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSteuerbaresVermoegen,
                    e -> roundHalfUp(
                        BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent())
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                            .multiply(
                                BigDecimal
                                    .valueOf(max(e.getSteuerbaresVermoegen() - stammdaten.getFreibetragVermoegen(), 0))
                            )
                    )
                )
            );
        } else {
            conditionalSubtrahends = Stream.of(
                mapAndReturn(FamilienBudgetresultatDto::setSaeule3a, e -> 0),
                mapAndReturn(FamilienBudgetresultatDto::setSaeule2, e -> 0)
            );

            conditionalSummands = Stream.of(
                mapAndReturn(
                    FamilienBudgetresultatDto::setSteuerbaresVermoegen,
                    e -> roundHalfUp(
                        BigDecimal.valueOf(e.getSteuerbaresVermoegen())
                            .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                    )
                )
            );
        }

        final var summand = applyAndSum(Stream.concat(summands, conditionalSummands).toList(), result, elternteil);
        final var subtrahend =
            applyAndSum(Stream.concat(subtrahends, conditionalSubtrahends).toList(), result, elternteil);
        final var vermoegen =
            applyAndSum(Stream.concat(conditionalSummands, conditionalSubtrahends).toList(), result, elternteil);

        final var einnahmen = Math.max(summand - subtrahend - stammdaten.getEinkommensfreibetrag(), 0) + vermoegen;
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

    private interface FamilienbudgetFunction
        extends GenericBiConsumerAndIntegerProducer<FamilienBudgetresultatDto, ElternteilV1> {
    }

    private FamilienbudgetFunction mapAndReturn(
        final BiConsumer<FamilienBudgetresultatDto, Integer> setter,
        final Function<ElternteilV1, Integer> getter
    ) {
        return (result, input) -> {
            final var value = getter.apply(input);
            setter.accept(result, value);
            return value;
        };
    }

    private int applyAndSum(
        final List<FamilienbudgetFunction> toApply,
        final FamilienBudgetresultatDto result,
        final ElternteilV1 elternteil
    ) {
        return toApply.stream()
            .map(applier -> applier.apply(result, elternteil))
            .mapToInt(Integer::intValue)
            .sum();
    }
}
