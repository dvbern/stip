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
import java.util.List;
import java.util.ListIterator;

import ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputPersoenlichesbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;
import static java.lang.Math.max;

@UtilityClass
public class PersoenlichesBudgetCalculatorV1 {
    public PersoenlichesBudgetresultatDto calculatePersoenlichesBudget(
        final InputPersoenlichesbudgetV1 input,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var result = new PersoenlichesBudgetresultatDto();
        final var antragssteller = input.getAntragssteller();

        calculateAndSetEinnahmen(
            result,
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );

        calculateAndSetAusgaben(
            result,
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );

        var rawBudgetBerechnet =
            BigDecimal.valueOf(result.getEinnahmenPersoenlichesBudget() - result.getAusgabenPersoenlichesBudget());
        if (antragssteller.isVerheiratetKonkubinat() && antragssteller.isEigenerHaushalt()) {
            rawBudgetBerechnet =
                rawBudgetBerechnet
                    .divide(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()), RoundingMode.HALF_UP);
        }
        result.setAnzahlPersonenImHaushalt(antragssteller.getAnzahlPersonenImHaushalt());
        result.setEigenerHaushalt(antragssteller.isEigenerHaushalt());
        result.setPersoenlichesbudgetBerechnet(roundHalfUp(rawBudgetBerechnet));

        result.setAnteilLebenshaltungskosten(
            calculateAnteilLebenshaltungskosten(List.of(familienbudget1, familienbudget2), antragssteller)
        );

        return result;
    }

    private static int calculateAnteilLebenshaltungskosten(
        List<FamilienBudgetresultatDto> familienBudgetresultatList,
        AntragsstellerV1 antragssteller
    ) {
        int anteilLebenshaltungskosten = 0;
        int piaWohntInElternHaushalt = antragssteller.getPiaWohntInElternHaushalt();
        ListIterator<FamilienBudgetresultatDto> iterator = familienBudgetresultatList.listIterator();
        while (iterator.hasNext()) {
            final FamilienBudgetresultatDto familienBudgetresultat = iterator.next();
            final var budgetIdx = iterator.nextIndex();
            final var familienBudget = familienBudgetresultat.getFamilienbudgetBerechnet();
            if (familienBudget >= 0 || antragssteller.isEigenerHaushalt() || budgetIdx != piaWohntInElternHaushalt) {
                continue;
            }
            anteilLebenshaltungskosten += familienBudget / familienBudgetresultat.getAnzahlPersonenImHaushalt();
        }
        return anteilLebenshaltungskosten;
    }

    private void calculateAndSetAusgaben(
        final PersoenlichesBudgetresultatDto result,
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        var grundbedarf = 0;
        var wohnkosten = 0;
        var medizinischeGrundversorgung = 0;

        if (antragssteller.isEigenerHaushalt()) {
            grundbedarf = antragssteller.getGrundbedarf();
            wohnkosten = antragssteller.getWohnkosten();
            medizinischeGrundversorgung = antragssteller.getMedizinischeGrundversorgung();
        }
        result.setGrundbedarf(grundbedarf);
        result.setWohnkosten(wohnkosten);
        result.setMedizinischeGrundversorgung(medizinischeGrundversorgung);

        var ausbildungskosten = antragssteller.getAusbildungskosten();
        if (antragssteller.isVerheiratetKonkubinat()) {
            ausbildungskosten = roundHalfUp(
                BigDecimal.valueOf(ausbildungskosten)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );
        }
        result.setAusbildungskosten(ausbildungskosten);

        result.setSteuernKantonGemeinde(antragssteller.getSteuern());

        var fahrkosten = antragssteller.getFahrkosten();
        if (antragssteller.isVerheiratetKonkubinat()) {
            fahrkosten = roundHalfUp(
                BigDecimal.valueOf(fahrkosten)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );
        }
        result.setFahrkosten(fahrkosten);
        result.setFahrkostenPartner(antragssteller.getFahrkostenPartner());

        var verpflegung = 0;
        if (!antragssteller.isEigenerHaushalt()) {
            var anzahlWochen = antragssteller.isLehre()
                ? stammdaten.getAnzahlWochenLehre()
                : stammdaten.getAnzahlWochenSchule();
            verpflegung = roundHalfUp(
                BigDecimal.valueOf(antragssteller.getVerpflegung())
                    .multiply(BigDecimal.valueOf(anzahlWochen))
                    .multiply(BigDecimal.valueOf(stammdaten.getPreisProMahlzeit()))
            );
        }
        result.setVerpflegung(verpflegung);
        result.setVerpflegungPartner(antragssteller.getVerpflegungPartner());

        result.setFremdbetreuung(antragssteller.getFremdbetreuung());

        final var anteilFamilienbudget1 = Math.abs(
            calculateAnteilFamilienbudget(
                antragssteller,
                familienbudget1,
                2,
                1
            )
        );

        final var anteilFamilienbudget2 = Math.abs(
            calculateAnteilFamilienbudget(
                antragssteller,
                familienbudget2,
                1,
                2
            )
        );

        result.setAnteilFamilienbudget(anteilFamilienbudget1 + anteilFamilienbudget2);

        final var ausgaben =
            result.getGrundbedarf()
            + result.getWohnkosten()
            + result.getMedizinischeGrundversorgung()
            + result.getAusbildungskosten()
            + result.getSteuernKantonGemeinde()
            + result.getFahrkosten()
            + result.getFahrkostenPartner()
            + result.getVerpflegung()
            + result.getVerpflegungPartner()
            + result.getFremdbetreuung()
            + result.getAnteilFamilienbudget();

        result.setAusgabenPersoenlichesBudget(ausgaben);
    }

    private Integer calculateAnteilFamilienbudget(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget,
        final int wohntImHaushaltZero,
        final int wohntImHaushaltAnrechnen
    ) {
        if (familienbudget == null) {
            return 0;
        }

        if (
            (familienbudget.getFamilienbudgetBerechnet() >= 0)
            || antragssteller.isEigenerHaushalt()
            || (antragssteller.getPiaWohntInElternHaushalt() == wohntImHaushaltZero)
        ) {
            return 0;
        }

        if (antragssteller.getPiaWohntInElternHaushalt() == wohntImHaushaltAnrechnen) {
            return roundHalfUp(
                BigDecimal.valueOf(familienbudget.getFamilienbudgetBerechnet())
                    .divide(
                        BigDecimal.valueOf(familienbudget.getAnzahlPersonenImHaushalt()),
                        RoundingMode.HALF_UP
                    )
                    .abs()
            );
        }

        return 0;
    }

    private void calculateAndSetEinnahmen(
        final PersoenlichesBudgetresultatDto result,
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final int einkommen;
        if (antragssteller.isTertiaerstufe()) {
            einkommen = max(antragssteller.getEinkommen() - stammdaten.getEinkommensfreibetrag(), 0);
        } else {
            einkommen = antragssteller.getEinkommen();
        }
        result.setEinkommen(einkommen);
        result.setEinkommenPartner(antragssteller.getEinkommenPartner());
        result.setSteuerbaresVermoegen(antragssteller.getVermoegen());
        result.setAnrechenbaresVermoegen(
            roundHalfUp(
                BigDecimal.valueOf(antragssteller.getVermoegen())
                    .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            )
        );
        result.setAlimente(antragssteller.getAlimente());
        result.setRente(antragssteller.getRente());
        result.setKinderAusbildungszulagen(antragssteller.getKinderAusbildungszulagen());
        result.setErgaenzungsleistungen(antragssteller.getErgaenzungsleistungen());
        result.setLeistungenEO(antragssteller.getLeistungenEO());
        result.setGemeindeInstitutionen(antragssteller.getGemeindeInstitutionen());

        final var elternbeitrag1 = calculateElternbeitrag(antragssteller, familienbudget1);
        result.setElternbeitrag1(elternbeitrag1);

        final var elternbeitrag2 = calculateElternbeitrag(antragssteller, familienbudget2);
        result.setElternbeitrag2(elternbeitrag2);

        final var einnahmen =
            result.getEinkommen()
            + result.getEinkommenPartner()
            + result.getAnrechenbaresVermoegen()
            + result.getAlimente()
            + result.getRente()
            + result.getKinderAusbildungszulagen()
            + result.getErgaenzungsleistungen()
            + result.getLeistungenEO()
            + result.getGemeindeInstitutionen()
            + result.getElternbeitrag1()
            + result.getElternbeitrag2();

        result.setEinnahmenPersoenlichesBudget(einnahmen);
    }

    private Integer calculateElternbeitrag(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget
    ) {
        if (familienbudget == null) {
            return 0;
        }
        if (familienbudget.getFamilienbudgetBerechnet() <= 0) {
            return 0;
        }

        final var anzahlGeschwisterInAusbildung =
            BigDecimal.valueOf(familienbudget.getAnzahlGeschwisterInAusbildung() + 1);
        final var fractionalValue = BigDecimal.valueOf(familienbudget.getFamilienbudgetBerechnet())
            .divide(anzahlGeschwisterInAusbildung, RoundingMode.HALF_UP);
        if (antragssteller.isHalbierungElternbeitrag()) {
            return fractionalValue.divide(BigDecimal.TWO, RoundingMode.HALF_UP).intValue();
        }
        return fractionalValue.intValue();
    }
}
