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

        return result;
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

        var ausbildungskosten = antragssteller.getAusbildungskosten();
        if (antragssteller.isVerheiratetKonkubinat()) {
            ausbildungskosten = roundHalfUp(
                BigDecimal.valueOf(ausbildungskosten)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );
        }

        final var steuern = antragssteller.getSteuern();
        final var steuernPartner = antragssteller.getSteuernPartner();

        var fahrkosten = antragssteller.getFahrkosten();
        if (antragssteller.isVerheiratetKonkubinat()) {
            fahrkosten = roundHalfUp(
                BigDecimal.valueOf(fahrkosten)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );
        }

        final var fahrkostenPartner = antragssteller.getFahrkostenPartner();

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

        final var verpflegungPartner = antragssteller.getVerpflegungPartner();
        final var fremdbetreuung = antragssteller.getFremdbetreuung();

        final var anteilLebenshaltungskosten1 = Math.abs(
            calculateAnteilLebenshaltungskosten(
                antragssteller,
                familienbudget1,
                2,
                1
            )
        );

        final var anteilLebenshaltungskosten2 = Math.abs(
            calculateAnteilLebenshaltungskosten(
                antragssteller,
                familienbudget2,
                1,
                2
            )
        );
        final var anteilLebenshaltungskosten = anteilLebenshaltungskosten1 + anteilLebenshaltungskosten2;

        final var ausgaben =
            grundbedarf
            + wohnkosten
            + medizinischeGrundversorgung
            + ausbildungskosten
            + steuern
            + steuernPartner
            + fahrkosten
            + fahrkostenPartner
            + verpflegung
            + verpflegungPartner
            + fremdbetreuung
            + anteilLebenshaltungskosten;

        // Set calculated values on dto
        result.setGrundbedarf(grundbedarf);
        result.setWohnkosten(wohnkosten);
        result.setMedizinischeGrundversorgung(medizinischeGrundversorgung);
        result.setAusbildungskosten(ausbildungskosten);
        result.setSteuern(steuern);
        result.setSteuernPartner(steuernPartner);
        result.setFahrkosten(fahrkosten);
        result.setFahrkostenPartner(fahrkostenPartner);
        result.setVerpflegung(verpflegung);
        result.setVerpflegungPartner(verpflegungPartner);
        result.setFremdbetreuung(fremdbetreuung);
        result.setAnteilLebenshaltungskosten(anteilLebenshaltungskosten);

        result.setAusgabenPersoenlichesBudget(ausgaben);
    }

    private Integer calculateAnteilLebenshaltungskosten(
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

        final var einkommenPartner = antragssteller.getEinkommenPartner();
        final var alimentePartner = antragssteller.getAlimentePartner() * 12;

        final var anrechenbaresVermoegen = roundHalfUp(
            BigDecimal.valueOf(antragssteller.getVermoegen())
                .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );

        final var alimente = antragssteller.getAlimente() * 12;
        final var rente = antragssteller.getRente();
        final var kinderAusbildungszulagen = antragssteller.getKinderAusbildungszulagen() * 12;
        final var kinderUnterhaltsbeitraege = antragssteller.getKinderErhalteneUnterhaltsbeitraege() * 12;
        final var ergaenzungsleistungen = antragssteller.getErgaenzungsleistungen();
        final var leistungenEO = antragssteller.getLeistungenEO();
        final var gemeindeInstitutionen = antragssteller.getGemeindeInstitutionen();
        final var elternbeitrag1 = calculateElternbeitrag(antragssteller, familienbudget1);
        final var elternbeitrag2 = calculateElternbeitrag(antragssteller, familienbudget2);

        final var einnahmen =
            einkommen
            + einkommenPartner
            + anrechenbaresVermoegen
            + alimente + alimentePartner
            + rente
            + kinderAusbildungszulagen
            + kinderUnterhaltsbeitraege
            + ergaenzungsleistungen
            + leistungenEO
            + gemeindeInstitutionen
            + elternbeitrag1
            + elternbeitrag2;

        // Set calculated values on dto
        result.setEinkommen(einkommen);
        result.setEinkommenPartner(antragssteller.getEinkommenPartner());
        result.setSteuerbaresVermoegen(antragssteller.getVermoegen());
        result.setAnrechenbaresVermoegen(anrechenbaresVermoegen);
        result.setAlimente(antragssteller.getAlimente());
        result.setRente(antragssteller.getRente());
        result.setKinderAusbildungszulagen(antragssteller.getKinderAusbildungszulagen());
        result.setKinderUnterhaltsbeitraege(antragssteller.getKinderErhalteneUnterhaltsbeitraege());
        result.setErgaenzungsleistungen(antragssteller.getErgaenzungsleistungen());
        result.setLeistungenEO(antragssteller.getLeistungenEO());
        result.setGemeindeInstitutionen(antragssteller.getGemeindeInstitutionen());
        result.setElternbeitrag1(elternbeitrag1);
        result.setElternbeitrag2(elternbeitrag2);

        final var anteilFamilienBudget = elternbeitrag1 + elternbeitrag2;
        result.setAnteilFamilienbudget(anteilFamilienBudget);

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
