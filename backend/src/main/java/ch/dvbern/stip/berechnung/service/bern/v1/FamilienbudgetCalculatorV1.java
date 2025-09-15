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

import java.math.BigDecimal;
import java.math.RoundingMode;

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

        result.familienBudgetTyp(elternteil.getSteuerdatenTyp());

        calculateAndSetAusgaben(result, elternteil);
        calculateAndSetEinnahmen(result, elternteil, stammdaten);
        calculateAndSetFamilienbudgetBerechnet(result);

        result.setAnzahlGeschwisterInAusbildung(elternteil.getAnzahlGeschwisterInAusbildung());
        result.setAnzahlPersonenImHaushalt(elternteil.getAnzahlPersonenImHaushalt());

        return result;
    }

    private void calculateAndSetAusgaben(final FamilienBudgetresultatDto result, final ElternteilV1 elternteil) {
        result.setGrundbedarf(elternteil.getGrundbedarf());
        result.setEffektiveWohnkosten(elternteil.getEffektiveWohnkosten());
        result.setMedizinischeGrundversorgung(elternteil.getMedizinischeGrundversorgung());
        result.setSteuernKantonGemeinde(elternteil.getSteuernStaat());
        result.setSteuernBund(elternteil.getSteuernBund());
        result.setIntegrationszulage(elternteil.getIntegrationszulage());
        result.setFahrkostenPerson1(elternteil.getFahrkostenPerson1());
        result.setFahrkostenPerson2(elternteil.getFahrkostenPerson2());
        result.setEssenskostenPerson1(elternteil.getEssenskostenPerson1());
        result.setEssenskostenPerson2(elternteil.getEssenskostenPerson2());

        final var ausgaben =
            result.getGrundbedarf()
            + result.getEffektiveWohnkosten()
            + result.getMedizinischeGrundversorgung()
            + result.getSteuernKantonGemeinde()
            + result.getSteuernBund()
            + result.getIntegrationszulage()
            + result.getFahrkostenPerson1()
            + result.getFahrkostenPerson2()
            + result.getEssenskostenPerson1()
            + result.getEssenskostenPerson2();

        result.setAusgabenFamilienbudget(ausgaben);
    }

    private void calculateAndSetEinnahmen(
        final FamilienBudgetresultatDto result,
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {

        result.setErgaenzungsleistungen(elternteil.getErgaenzungsleistungen());
        result.setTotalEinkuenfte(elternteil.getTotalEinkuenfte());
        result.setEigenmietwert(elternteil.getEigenmietwert());
        result.setAlimente(elternteil.getAlimente());

        result.setSteuerbaresVermoegen(elternteil.getSteuerbaresVermoegen());

        var saeule3a = 0;
        var saeule2 = 0;
        var anrechenbaresVermoegen = BigDecimal.valueOf(
            elternteil.getSteuerbaresVermoegen()
        );

        if (elternteil.isSelbststaendigErwerbend()) {
            saeule3a = max(elternteil.getEinzahlungSaeule3a() - stammdaten.getMaxSaeule3a(), 0);
            saeule2 = elternteil.getEinzahlungSaeule2();

            // steuerbaresVermoegen - freibetragVermoegen
            anrechenbaresVermoegen =
                BigDecimal.valueOf(
                    max(
                        elternteil.getSteuerbaresVermoegen() - stammdaten.getFreibetragVermoegen(),
                        0
                    )
                );
        }
        result.setSelbststaendigErwerbend(elternteil.isSelbststaendigErwerbend());
        result.setSaeule3a(saeule3a);
        result.setSaeule2(saeule2);
        result.setAnrechenbaresVermoegen(
            roundHalfUp(
                anrechenbaresVermoegen.setScale(2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            )
        );

        final var einnahmenBeforeVermoegen = max(
            result.getTotalEinkuenfte()
            + result.getErgaenzungsleistungen()
            - result.getEigenmietwert()
            - result.getAlimente()
            - result.getSaeule3a()
            - result.getSaeule2()
            - stammdaten.getEinkommensfreibetrag(),
            0
        );

        final var einnahmen = einnahmenBeforeVermoegen + result.getAnrechenbaresVermoegen();
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
}
