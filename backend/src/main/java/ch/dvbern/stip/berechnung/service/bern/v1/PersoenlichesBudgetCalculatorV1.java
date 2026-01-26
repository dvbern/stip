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
import java.util.Objects;
import java.util.Optional;

import ch.dvbern.stip.berechnung.dto.InputUtils;
import ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputPersoenlichesbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.divideByTranchen;
import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;

@UtilityClass
public class PersoenlichesBudgetCalculatorV1 {
    public PersoenlichesBudgetresultatDto calculatePersoenlichesBudget(
        final InputPersoenlichesbudgetV1 input,
        final Optional<FamilienBudgetresultatDto> familienbudget1,
        final Optional<FamilienBudgetresultatDto> familienbudget2,
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
            budgetTranche = total;
            if (anzahlMonate != 12) {
                budgetTranche = divideByTranchen(total, anzahlMonate);
            }
        }
        total = budgetTranche;

        return new PersoenlichesBudgetresultatDto()
            .vorname(antragssteller.getVorname())
            .nachname(antragssteller.getNachname())
            .sozialversicherungsnummer(antragssteller.getSozialversicherungsnummer())
            .geburtsdatum(antragssteller.getGeburtsdatum())
            .total(roundHalfUp(total)) // TODO Remove
            .einnahmenMinusKosten(roundHalfUp(einnahmenMinusKosten))
            .fehlbetrag(roundHalfUp(fehlbetrag))
            .proKopfTeilung(proKopfTeilung)
            .eigenerHaushalt(antragssteller.isEigenerHaushalt())
            .budgetTranche(roundHalfUp(budgetTranche))
            .anzahlMonate(anzahlMonate)
            .gesetzlichesDarlehen(0) // TODO KSTIP-2584: will be definable once gesetztliche Darlehen are defined
            .gesetzlichesDarlehenStipendium(0) // TODO KSTIP-2584: same as above
            .anzahlPersonenImHaushalt(antragssteller.getAnzahlPersonenImHaushalt())
            .einnahmen(einnahmen)
            .kosten(kosten)
            .vornamePartner(antragssteller.getVornamePartner())
            .nachnamePartner(antragssteller.getNachnamePartner());
    }

    private PersoenlichesBudgetresultatKostenDto calculateKosten(
        final AntragsstellerV1 antragssteller,
        final Optional<FamilienBudgetresultatDto> familienbudget1,
        final Optional<FamilienBudgetresultatDto> familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        var grundbedarf = 0;
        var wohnkosten = 0;
        var medizinischeGrundversorgungs = antragssteller.getMedizinischeGrundversorgungs();
        var medizinischeGrundversorgungTotal = 0;

        if (antragssteller.isEigenerHaushalt()) {
            grundbedarf = antragssteller.getGrundbedarf();
            wohnkosten = antragssteller.getWohnkosten();
            medizinischeGrundversorgungTotal = InputUtils.sumValues(medizinischeGrundversorgungs);
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
        var fahrkostenPartner = antragssteller.getFahrkostenPartner();

        if (antragssteller.isVerheiratetKonkubinat()) {
            fahrkosten = roundHalfUp(
                BigDecimal.valueOf(fahrkosten)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );

            fahrkostenPartner = roundHalfUp(
                BigDecimal.valueOf(fahrkostenPartner)
                    .multiply(BigDecimal.valueOf(antragssteller.getAnzahlPersonenImHaushalt()))
            );
        }

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

        final var anteilLebenshaltungskosten1 =
            familienbudget1.map(FamilienBudgetresultatDto::getUngedeckterAnteilLebenshaltungskosten);
        final var anteilLebenshaltungskosten2 =
            familienbudget2.map(FamilienBudgetresultatDto::getUngedeckterAnteilLebenshaltungskosten);
        final var anteilLebenshaltungskosten =
            InputUtils.sumNullables(anteilLebenshaltungskosten1.orElse(null), anteilLebenshaltungskosten2.orElse(null));

        final var ausgaben =
            grundbedarf
            + InputUtils.sumNullables(
                wohnkosten,
                medizinischeGrundversorgungTotal,
                ausbildungskosten,
                steuern,
                steuernPartner,
                fahrkosten,
                fahrkostenPartner,
                verpflegung,
                verpflegungPartner,
                fremdbetreuung,
                anteilLebenshaltungskosten
            );

        // Set calculated values on dto
        return new PersoenlichesBudgetresultatKostenDto()
            .total(ausgaben)
            .ausbildungskosten(ausbildungskosten)
            .fahrkosten(fahrkosten)
            .verpflegungskosten(verpflegung)
            .grundbedarf(grundbedarf)
            .wohnkosten(wohnkosten)
            .medizinischeGrundversorgung(medizinischeGrundversorgungs)
            .medizinischeGrundversorgungTotal(medizinischeGrundversorgungTotal)
            .fahrkostenPartner(fahrkostenPartner)
            .verpflegungPartner(verpflegungPartner)
            .betreuungskostenKinder(fremdbetreuung)
            .kantonsGemeindesteuern(steuern)
            .bundessteuern(0) // Momentan kann dieses Feld nicht gesetzt werden, wird wahrscheinlich entfernt werden via
                              // CR
            .anteilLebenshaltungskosten(anteilLebenshaltungskosten);
    }

    private PersoenlichesBudgetresultatEinnahmenDto calculateEinnahmen(
        final AntragsstellerV1 antragssteller,
        final Optional<FamilienBudgetresultatDto> familienbudget1,
        final Optional<FamilienBudgetresultatDto> familienbudget2,
        final StammdatenV1 stammdaten
    ) {
        final var einkommens = antragssteller.getEinkommens();
        final var einkommenTotal = InputUtils.sumValues(einkommens);
        final var kinderAusbildungszulagens = antragssteller.getKinderAusbildungszulagens();
        final var kinderAusbildungszulagenTotal = InputUtils.sumValues(kinderAusbildungszulagens);
        final var unterhaltsbeitraeges = antragssteller.getUnterhaltsbeitraeges();
        final var unterhaltsbeitraegeTotal = InputUtils.sumValues(unterhaltsbeitraeges);
        final var eoLeistungens = antragssteller.getEoLeistungens();
        final var eoLeistungenTotal = InputUtils.sumValues(eoLeistungens);
        final var taggelderAHVIVs = antragssteller.getTaggelds();
        final var taggelderAHVIVTotal = InputUtils.sumValues(taggelderAHVIVs);
        final var rentens = antragssteller.getRentens();
        final var rentenTotal = InputUtils.sumValues(rentens);
        final var ergaenzungsleistungens = antragssteller.getErgaenzungsleistungens();
        final var ergaenzungsleistungenTotal = InputUtils.sumValues(ergaenzungsleistungens);
        final var gemeindeInstitutionen = Objects.requireNonNullElse(antragssteller.getGemeindeInstitutionen(), 0);
        final var andereEinnahmens = antragssteller.getAndereEinnahmens();
        final var andereEinnahmenTotal = InputUtils.sumValues(andereEinnahmens);
        final var anrechenbaresVermoegen = roundHalfUp(
            BigDecimal.valueOf(Objects.requireNonNullElse(antragssteller.getVermoegen(), 0))
                .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );
        final var steuerbaresVermoegen = antragssteller.getVermoegen();
        final var elternbeitrag1 = familienbudget1.map(budget -> calculateElternbeitrag(antragssteller, budget));
        final var elternbeitrag2 = familienbudget2.map(budget -> calculateElternbeitrag(antragssteller, budget));
        final var einnahmenBGSAs = antragssteller.getEinnahmenBGSAs();
        final var einnahmenBGSATotal = InputUtils.sumValues(einnahmenBGSAs);;

        final var einnahmen =
            einkommenTotal
            + InputUtils.sumNullables(
                anrechenbaresVermoegen,
                unterhaltsbeitraegeTotal,
                rentenTotal,
                kinderAusbildungszulagenTotal,
                ergaenzungsleistungenTotal,
                eoLeistungenTotal,
                gemeindeInstitutionen,
                einnahmenBGSATotal,
                taggelderAHVIVTotal,
                andereEinnahmenTotal,
                elternbeitrag1.orElse(null),
                elternbeitrag2.orElse(null)
            );

        // Set calculated values on dto
        return new PersoenlichesBudgetresultatEinnahmenDto()
            .total(einnahmen)
            .nettoerwerbseinkommen(einkommens)
            .nettoerwerbseinkommenTotal(einkommenTotal)
            .einnahmenBGSA(einnahmenBGSAs)
            .einnahmenBGSATotal(einnahmenBGSATotal)
            .kinderAusbildungszulagen(kinderAusbildungszulagens)
            .kinderAusbildungszulagenTotal(kinderAusbildungszulagenTotal)
            .unterhaltsbeitraege(unterhaltsbeitraeges)
            .unterhaltsbeitraegeTotal(unterhaltsbeitraegeTotal)
            .eoLeistungen(eoLeistungens)
            .eoLeistungenTotal(eoLeistungenTotal)
            .taggelderAHVIV(taggelderAHVIVs)
            .taggelderAHVIVTotal(taggelderAHVIVTotal)
            .renten(rentens)
            .rentenTotal(rentenTotal)
            .ergaenzungsleistungen(ergaenzungsleistungens)
            .ergaenzungsleistungenTotal(ergaenzungsleistungenTotal)
            .beitraegeGemeindeInstitutionen(gemeindeInstitutionen)
            .andereEinnahmen(andereEinnahmens)
            .andereEinnahmenTotal(andereEinnahmenTotal)
            .anrechenbaresVermoegen(anrechenbaresVermoegen)
            .steuerbaresVermoegen(steuerbaresVermoegen)
            .elterlicheLeistung(
                InputUtils.sumNullables(
                    elternbeitrag1.orElse(null),
                    elternbeitrag2.orElse(null)
                )
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

        final var anzahlGeschwisterInAusbildung =
            BigDecimal.valueOf(familienbudget.getAnzahlKinderInAusbildung());
        final var fractionalValue = BigDecimal.valueOf(familienbudget.getEinnahmeUeberschuss())
            .divide(anzahlGeschwisterInAusbildung, RoundingMode.HALF_UP);
        if (antragssteller.isHalbierungElternbeitrag()) {
            return fractionalValue.divide(BigDecimal.TWO, RoundingMode.HALF_UP).intValue();
        }
        return fractionalValue.intValue();
    }
}
