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
import java.util.Arrays;
import java.util.List;

import ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputPersoenlichesbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDto;
import ch.dvbern.stip.generated.dto.PersonValueItemDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.divideByTranchen;
import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;

@UtilityClass
public class PersoenlichesBudgetCalculatorV1 {
    public PersoenlichesBudgetresultatDto calculatePersoenlichesBudget(
        final InputPersoenlichesbudgetV1 input,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var antragssteller = input.getAntragssteller();

        final var einnahmen = calculateEinnahmen(
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );
        final var kosten = calculateKosten(
            antragssteller,
            familienbudget1,
            familienbudget2,
            stammdaten
        );

        final var einnahmenMinusKosten = BigDecimal.valueOf(einnahmen.getTotal() - kosten.getTotal());
        var total = BigDecimal.ZERO;
        var fehlbetrag = BigDecimal.ZERO;
        var proKopfTeilung = 0;
        final var anzahlMonate = stammdaten.getAnzahlMonate();
        var budgetTranche = BigDecimal.ZERO;

        if (einnahmenMinusKosten.signum() < 0) {
            total = einnahmenMinusKosten;
            fehlbetrag = total;
            if (antragssteller.isVerheiratetKonkubinat() && antragssteller.isEigenerHaushalt()) {
                proKopfTeilung = 1; // TODO: Check if 1? Can it even be more?
                total = total
                    .divide(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()), RoundingMode.HALF_UP);;
            }
            budgetTranche = divideByTranchen(total, anzahlMonate);
        }
        total = budgetTranche;

        return new PersoenlichesBudgetresultatDto(
            roundHalfUp(total),
            roundHalfUp(einnahmenMinusKosten),
            roundHalfUp(fehlbetrag),
            proKopfTeilung,
            antragssteller.isEigenerHaushalt(),
            roundHalfUp(budgetTranche),
            anzahlMonate,
            0, // TODO KSTIP-2584: will be definable once gesetztliche Darlehen are defined
            0, // TODO KSTIP-2584: same as above
            antragssteller.getAnzahlPersonenImHaushalt(),
            einnahmen,
            kosten
        );
    }

    private PersoenlichesBudgetresultatKostenDto calculateKosten(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        var grundbedarf = 0;
        var wohnkosten = 0;
        var medizinischeGrundversorgung = 0;
        var medizinischeGrundversorgungPartner = 0;
        List<PersonValueItemDto> medizinischeGrundversorgungKinder = null;
        var medizinischeGrundversorgungTotal = 0;

        if (antragssteller.isEigenerHaushalt()) {
            grundbedarf = antragssteller.getGrundbedarf();
            wohnkosten = antragssteller.getWohnkosten();
            medizinischeGrundversorgung = antragssteller.getMedizinischeGrundversorgung();
            medizinischeGrundversorgungPartner = antragssteller.getMedizinischeGrundversorgungPartner();
            medizinischeGrundversorgungKinder = antragssteller.getMedizinischeGrundversorgungKinder();
            medizinischeGrundversorgungTotal = sumValues(
                medizinischeGrundversorgungKinder,
                medizinischeGrundversorgung,
                medizinischeGrundversorgungPartner
            );
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
                BigDecimal.valueOf(antragssteller.getAuswaertigeMittagessenProWoche())
                    .multiply(BigDecimal.valueOf(anzahlWochen))
                    .multiply(BigDecimal.valueOf(stammdaten.getPreisProMahlzeit()))
            );
        }

        final var verpflegungPartner = antragssteller.getVerpflegungskostenPartner();
        final var fremdbetreuung = antragssteller.getFremdbetreuung();

        final var anteilLebenshaltungskosten1 = familienbudget1.getUngedeckterAnteilLebenshaltungskosten();
        final var anteilLebenshaltungskosten2 = familienbudget2.getUngedeckterAnteilLebenshaltungskosten();
        final var anteilLebenshaltungskosten = anteilLebenshaltungskosten1 + anteilLebenshaltungskosten2;

        final var ausgaben =
            grundbedarf
            + wohnkosten
            + medizinischeGrundversorgungTotal
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
        return new PersoenlichesBudgetresultatKostenDto(
            ausgaben,
            ausbildungskosten,
            fahrkosten,
            verpflegung,
            grundbedarf,
            wohnkosten,
            medizinischeGrundversorgung,
            medizinischeGrundversorgungPartner,
            medizinischeGrundversorgungKinder,
            medizinischeGrundversorgungTotal,
            fahrkostenPartner,
            verpflegungPartner,
            fremdbetreuung,
            steuern,
            0, // Momentan kann dieses Feld nicht gesetzt werden, wird wahrscheinlich entfernt werden via CR
            anteilLebenshaltungskosten
        );
    }

    private PersoenlichesBudgetresultatEinnahmenDto calculateEinnahmen(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget1,
        final FamilienBudgetresultatDto familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var einkommen = antragssteller.getEinkommen();
        final var einkommenPartner = antragssteller.getEinkommenPartner();
        final var einkommenTotal = einkommen + einkommenPartner;
        final var kinderAusbildungszulagen = antragssteller.getKinderAusbildungszulagen();
        final var kinderAusbildungszulagenPartner = antragssteller.getKinderAusbildungszulagenPartner();
        final var kinderAusbildungszulagenKinder = antragssteller.getKinderAusbildungszulagenKinder();
        final var kinderAusbildungszulagenTotal =
            sumValues(kinderAusbildungszulagenKinder, kinderAusbildungszulagen, kinderAusbildungszulagenPartner);
        final var unterhaltsbeitraege = antragssteller.getUnterhaltsbeitraege();
        final var unterhaltsbeitraegePartner = antragssteller.getUnterhaltsbeitraegePartner();
        final var unterhaltsbeitraegeKinder = antragssteller.getUnterhaltsbeitraegeKinder();
        final var unterhaltsbeitraegeTotal =
            sumValues(unterhaltsbeitraegeKinder, unterhaltsbeitraege, unterhaltsbeitraegePartner);
        final var eoLeistungen = antragssteller.getEoLeistungen();
        final var eoLeistungenPartner = antragssteller.getEoLeistungenPartner();
        final var eoLeistungenTotal = eoLeistungen + eoLeistungenPartner;
        final var taggelderAHVIV = antragssteller.getTaggeld();
        final var taggelderAHVIVPartner = antragssteller.getTaggeldPartner();
        final var taggelderAHVIVTotal = taggelderAHVIV + taggelderAHVIVPartner;
        final var renten = antragssteller.getRenten();
        final var rentenPartner = antragssteller.getRentenPartner();
        final var rentenKinder = antragssteller.getRentenKinder();
        final var rentenTotal = sumValues(rentenKinder, renten, rentenPartner);
        final var ergaenzungsleistungen = antragssteller.getErgaenzungsleistungen();
        final var ergaenzungsleistungenPartner = antragssteller.getErgaenzungsleistungenPartner();
        final var ergaenzungsleistungenKinder = antragssteller.getErgaenzungsleistungenKinder();
        final var ergaenzungsleistungenTotal =
            sumValues(ergaenzungsleistungenKinder, ergaenzungsleistungen, ergaenzungsleistungenPartner);
        final var gemeindeInstitutionen = antragssteller.getGemeindeInstitutionen();
        final var andereEinnahmen = antragssteller.getAndereEinnahmen();
        final var andereEinnahmenPartner = antragssteller.getAndereEinnahmenPartner();
        final var andereEinnahmenKinder = antragssteller.getAndereEinnahmenKinder();
        final var andereEinnahmenTotal = sumValues(andereEinnahmenKinder, andereEinnahmen, andereEinnahmenPartner);
        final var anrechenbaresVermoegen = roundHalfUp(
            BigDecimal.valueOf(antragssteller.getVermoegen())
                .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );
        final var steuerbaresVermoegen = antragssteller.getVermoegen();
        final var elternbeitrag1 = calculateElternbeitrag(antragssteller, familienbudget1);
        final var elternbeitrag2 = calculateElternbeitrag(antragssteller, familienbudget2);
        final var einnahmenBGSA = antragssteller.getEinnahmenBGSA();
        final var einnahmenBGSAPartner = antragssteller.getEinnahmenBGSAPartner();
        final var einnahmenBGSATotal = einnahmenBGSA + einnahmenBGSAPartner;

        final var einnahmen =
            einkommenTotal
            + anrechenbaresVermoegen
            + unterhaltsbeitraegeTotal
            + rentenTotal
            + kinderAusbildungszulagenTotal
            + ergaenzungsleistungenTotal
            + eoLeistungenTotal
            + gemeindeInstitutionen
            + einnahmenBGSATotal
            + taggelderAHVIVTotal
            + andereEinnahmenTotal
            + elternbeitrag1
            + elternbeitrag2;

        // Set calculated values on dto
        return new PersoenlichesBudgetresultatEinnahmenDto(
            einnahmen,
            einkommen,
            einkommenPartner,
            einkommenTotal,
            einnahmenBGSA,
            einnahmenBGSAPartner,
            einnahmenBGSATotal,
            kinderAusbildungszulagen,
            kinderAusbildungszulagenPartner,
            kinderAusbildungszulagenKinder,
            kinderAusbildungszulagenTotal,
            unterhaltsbeitraege,
            unterhaltsbeitraegePartner,
            unterhaltsbeitraegeKinder,
            unterhaltsbeitraegeTotal,
            eoLeistungen,
            eoLeistungenPartner,
            eoLeistungenTotal,
            taggelderAHVIV,
            taggelderAHVIVPartner,
            taggelderAHVIVTotal,
            renten,
            rentenPartner,
            rentenKinder,
            rentenTotal,
            ergaenzungsleistungen,
            ergaenzungsleistungenPartner,
            ergaenzungsleistungenKinder,
            ergaenzungsleistungenTotal,
            gemeindeInstitutionen,
            andereEinnahmen,
            andereEinnahmenPartner,
            andereEinnahmenKinder,
            andereEinnahmenTotal,
            anrechenbaresVermoegen,
            steuerbaresVermoegen,
            elternbeitrag1 + elternbeitrag2
        );
    }

    private Integer calculateElternbeitrag(
        final AntragsstellerV1 antragssteller,
        final FamilienBudgetresultatDto familienbudget
    ) {
        if (familienbudget == null) {
            return 0;
        }
        if (familienbudget.getEinnahmeUeberschuss() <= 0) {
            return 0;
        }

        final var anzahlKinderInAusbildung =
            BigDecimal.valueOf(familienbudget.getAnzahlKinderInAusbildung());
        final var fractionalValue = BigDecimal.valueOf(familienbudget.getEinnahmeUeberschuss())
            .divide(anzahlKinderInAusbildung, RoundingMode.HALF_UP);
        if (antragssteller.isHalbierungElternbeitrag()) {
            return fractionalValue.divide(BigDecimal.TWO, RoundingMode.HALF_UP).intValue();
        }
        return fractionalValue.intValue();
    }

    public static int sumValues(int... otherValues) {
        return Arrays.stream(otherValues).sum();
    }

    public static int sumValues(List<PersonValueItemDto> list, int... otherValues) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return sumValues(otherValues) + list.stream().mapToInt(PersonValueItemDto::getValue).sum();
    }
}
