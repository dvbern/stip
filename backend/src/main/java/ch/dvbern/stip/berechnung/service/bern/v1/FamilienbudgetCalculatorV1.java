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
        final var grundbedarf = elternteil.getGrundbedarf();
        final var effektiveWohnkosten = elternteil.getEffektiveWohnkosten();
        final var medizinischeGrundversorgung = elternteil.getMedizinischeGrundversorgung();
        final var steuernStaat = elternteil.getSteuernStaat();
        final var steuernBund = elternteil.getSteuernBund();
        final var integrationszulage = elternteil.getIntegrationszulage();
        final var fahrkostenPerson1 = elternteil.getFahrkostenPerson1();
        final var fahrkostenPerson2 = elternteil.getFahrkostenPerson2();
        final var essenskostenPerson1 = elternteil.getEssenskostenPerson1();
        final var essenskostenPerson2 = elternteil.getEssenskostenPerson2();

        final var ausgaben =
            grundbedarf
            + effektiveWohnkosten
            + medizinischeGrundversorgung
            + steuernStaat
            + steuernBund
            + integrationszulage
            + fahrkostenPerson1
            + fahrkostenPerson2
            + essenskostenPerson1
            + essenskostenPerson2;

        // Set calculated values on dto
        result.setGrundbedarf(grundbedarf);
        result.setEffektiveWohnkosten(effektiveWohnkosten);
        result.setMedizinischeGrundversorgung(medizinischeGrundversorgung);
        result.setSteuernKantonGemeinde(steuernStaat);
        result.setSteuernBund(steuernBund);
        result.setIntegrationszulage(integrationszulage);
        result.setFahrkostenPerson1(fahrkostenPerson1);
        result.setFahrkostenPerson2(fahrkostenPerson2);
        result.setEssenskostenPerson1(essenskostenPerson1);
        result.setEssenskostenPerson2(essenskostenPerson2);

        result.setAusgabenFamilienbudget(ausgaben);
    }

    private void calculateAndSetEinnahmen(
        final FamilienBudgetresultatDto result,
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        final var ergaenzungsleistungen = elternteil.getErgaenzungsleistungen();
        final var totalEinkuenfte = elternteil.getTotalEinkuenfte();
        final var eigenmietwert = elternteil.getEigenmietwert();
        final var alimente = elternteil.getAlimente();
        final var steuerbaresVermoegen = elternteil.getSteuerbaresVermoegen();
        final var saeule3a = getSaeule3a(elternteil, stammdaten);
        final var saeule2 = getSaeule2(elternteil);
        final var anrechenbaresVermoegen = getAnrechenbaresVermoegen(elternteil, stammdaten);
        final var einnahmenBGSA = elternteil.getEinnahmenBGSA();
        final var andereEinnahmen = elternteil.getAndereEinnahmen();
        final var renten = elternteil.getRenten();

        final var einnahmenBeforeVermoegen = max(
            totalEinkuenfte
            + ergaenzungsleistungen
            + einnahmenBGSA
            + andereEinnahmen
            - eigenmietwert
            - alimente
            - saeule3a
            - saeule2
            - renten
            - stammdaten.getEinkommensfreibetrag(),
            0
        );

        final var einnahmen = einnahmenBeforeVermoegen + anrechenbaresVermoegen;

        // Set calculated values on dto
        result.setErgaenzungsleistungen(ergaenzungsleistungen);
        result.setTotalEinkuenfte(totalEinkuenfte);
        result.setEigenmietwert(eigenmietwert);
        result.setAlimente(alimente);
        result.setSteuerbaresVermoegen(steuerbaresVermoegen);
        result.setSaeule3a(saeule3a);
        result.setSaeule2(saeule2);
        result.setAnrechenbaresVermoegen(anrechenbaresVermoegen);
        final var selbststaendigErwerbend = elternteil.isSelbststaendigErwerbend();
        result.setSelbststaendigErwerbend(selbststaendigErwerbend);

        result.setEinnahmenFamilienbudget(einnahmen);
    }

    private int getSaeule3a(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        var saeule3a = 0;
        if (elternteil.isSelbststaendigErwerbend()) {
            saeule3a = max(elternteil.getEinzahlungSaeule3a() - stammdaten.getMaxSaeule3a(), 0);
        }
        return saeule3a;
    }

    private int getSaeule2(
        final ElternteilV1 elternteil
    ) {
        var saeule2 = 0;
        if (elternteil.isSelbststaendigErwerbend()) {
            saeule2 = elternteil.getEinzahlungSaeule2();
        }
        return saeule2;
    }

    private int getAnrechenbaresVermoegen(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        var anrechenbaresVermoegen = BigDecimal.valueOf(
            elternteil.getSteuerbaresVermoegen()
        );
        if (elternteil.isSelbststaendigErwerbend()) {
            // steuerbaresVermoegen - freibetragVermoegen
            anrechenbaresVermoegen =
                BigDecimal.valueOf(
                    max(
                        elternteil.getSteuerbaresVermoegen() - stammdaten.getFreibetragVermoegen(),
                        0
                    )
                );
        }
        return roundHalfUp(
            anrechenbaresVermoegen.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );
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
