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

import ch.dvbern.stip.berechnung.dto.InputUtils;
import ch.dvbern.stip.berechnung.dto.v1.AntragsstellerV1;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputFamilienbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.ElternteilV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.util.MathUtil.roundHalfUp;
import static java.lang.Math.max;

@UtilityClass
public class FamilienbudgetCalculatorV1 {
    public FamilienBudgetresultatDto calculateFamilienbudget(
        final InputFamilienbudgetV1 input,
        final AntragsstellerV1 antragssteller,
        final StammdatenV1 stammdaten
    ) {
        if (input == null || !input.getElternteil().isInitialized()) {
            return null;
        }

        final var elternteil = input.getElternteil();

        final var anzahlPersonenImHaushalt = elternteil.getAnzahlPersonenImHaushalt();
        final var anzahlKinderInAusbildung = elternteil.getAnzahlGeschwisterInAusbildung() + InputUtils.PIA_COUNT;
        final var einnahmen = calculateEinnahmen(elternteil, stammdaten);
        final var kosten = calculateKosten(elternteil);
        final var einnahmenMinusKosten =
            BigDecimal.valueOf(einnahmen.getTotal()).subtract(BigDecimal.valueOf(kosten.getTotal()));
        var total = einnahmenMinusKosten;
        var einnahmeUeberschuss = BigDecimal.ZERO;
        var proKopfTeilungKinderInAusbildung = 0;
        var anrechenbareElterlicheLeistung = BigDecimal.ZERO;
        var halbierungsReduktion = BigDecimal.ZERO;
        var fehlbetrag = BigDecimal.ZERO;
        var proKopfTeilung = 0;
        var ungedeckterAnteilLebenshaltungskosten = BigDecimal.ZERO;

        // If Kosten exceeds einnahmen, fehlbetrag values are filled
        if (einnahmenMinusKosten.signum() < 0) {
            fehlbetrag = total.abs();
            proKopfTeilung = anzahlPersonenImHaushalt;
            ungedeckterAnteilLebenshaltungskosten =
                calculateAnteilLebenshaltungskosten(
                    antragssteller,
                    total,
                    anzahlPersonenImHaushalt,
                    elternteil.getElternhaushalt()
                );
        }
        // otherwise einnahmeUeberschuss values are filled
        else {
            proKopfTeilungKinderInAusbildung = anzahlKinderInAusbildung;
            einnahmeUeberschuss = total;
            anrechenbareElterlicheLeistung = einnahmeUeberschuss;
            if (proKopfTeilungKinderInAusbildung > 0) {
                anrechenbareElterlicheLeistung =
                    total.divide(BigDecimal.valueOf(proKopfTeilungKinderInAusbildung), RoundingMode.HALF_UP);
            }
            total = anrechenbareElterlicheLeistung;
            if (antragssteller.isHalbierungElternbeitrag()) {
                halbierungsReduktion = anrechenbareElterlicheLeistung.divide(BigDecimal.TWO, RoundingMode.HALF_UP);
                total = halbierungsReduktion;
            }
        }

        return new FamilienBudgetresultatDto()
            .steuerdatenTyp(elternteil.getSteuerdatenTyp())
            .vorname(antragssteller.getVorname())
            .nachname(antragssteller.getNachname())
            .sozialversicherungsnummer(antragssteller.getSozialversicherungsnummer())
            .geburtsdatum(antragssteller.getGeburtsdatum())
            .steuerjahr(elternteil.getSteuerjahr())
            .veranlagungscode(elternteil.getVeranlagungscode())
            .total(roundHalfUp(total))
            .einnahmenMinusKosten(roundHalfUp(einnahmenMinusKosten))
            .anzahlPersonenImHaushalt(anzahlPersonenImHaushalt)
            .anzahlKinderInAusbildung(anzahlKinderInAusbildung)
            .einnahmeUeberschuss(roundHalfUp(einnahmeUeberschuss))
            .proKopfTeilungKinderInAusbildung(proKopfTeilungKinderInAusbildung)
            .anrechenbareElterlicheLeistung(roundHalfUp(anrechenbareElterlicheLeistung))
            .halbierungsReduktion(roundHalfUp(halbierungsReduktion))
            .fehlbetrag(roundHalfUp(fehlbetrag))
            .proKopfTeilung(proKopfTeilung)
            .ungedeckterAnteilLebenshaltungskosten(roundHalfUp(ungedeckterAnteilLebenshaltungskosten))
            .einnahmen(einnahmen)
            .kosten(kosten)
            .vornamePartner(antragssteller.getVornamePartner())
            .nachnamePartner(antragssteller.getNachnamePartner());
    }

    private FamilienBudgetresultatKostenDto calculateKosten(final ElternteilV1 elternteil) {
        final var grundbedarf = elternteil.getGrundbedarf();
        final var effektiveWohnkosten = elternteil.getEffektiveWohnkosten();
        final var medizinischeGrundversorgung = elternteil.getMedizinischeGrundversorgung();
        final var kantonsGemeindesteuern = elternteil.getSteuernKantonGemeinde();
        final var bundessteuern = elternteil.getSteuernBund();
        final var integrationszulage = elternteil.getIntegrationszulage();
        final var integrationszulageAnzahl = elternteil.getIntegrationszulageAnzahl();
        final var integrationszulageTotal = elternteil.getIntegrationszulageTotal();
        final var fahrkostens = elternteil.getFahrkostens();
        final var fahrkostenTotal = InputUtils.sumValues(fahrkostens);
        final var verpflegungskostens = elternteil.getVerpflegungskostens();
        final var verpflegungskostenTotal = InputUtils.sumValues(verpflegungskostens);

        final var ausgaben =
            grundbedarf
            + InputUtils.sumNullables(
                effektiveWohnkosten,
                medizinischeGrundversorgung,
                kantonsGemeindesteuern,
                bundessteuern,
                integrationszulageTotal,
                fahrkostenTotal,
                verpflegungskostenTotal
            );

        // Set calculated values on dto
        return new FamilienBudgetresultatKostenDto()
            .total(ausgaben)
            .grundbedarf(grundbedarf)
            .wohnkosten(effektiveWohnkosten)
            .medizinischeGrundversorgung(medizinischeGrundversorgung)
            .integrationszulage(integrationszulage)
            .integrationszulageAnzahl(integrationszulageAnzahl)
            .integrationszulageTotal(integrationszulageTotal)
            .kantonsGemeindesteuern(kantonsGemeindesteuern)
            .bundessteuern(bundessteuern)
            .fahrkosten(fahrkostens)
            .fahrkostenTotal(fahrkostenTotal)
            .verpflegung(verpflegungskostens)
            .verpflegungTotal(verpflegungskostenTotal);
    }

    private FamilienBudgetresultatEinnahmenDto calculateEinnahmen(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        final var ergaenzungsleistungen = Objects.requireNonNullElse(elternteil.getErgaenzungsleistungen(), 0);
        final var totalEinkuenfte = elternteil.getTotalEinkuenfte();
        final var eigenmietwert = elternteil.getEigenmietwert();
        final var unterhaltsbeitraege = elternteil.getUnterhaltsbeitraege();
        final var steuerbaresVermoegen = elternteil.getVermoegen();
        final var saeule3a = getSaeule3a(elternteil, stammdaten);
        final var saeule2 = getSaeule2(elternteil);
        final var anrechenbaresVermoegen = getAnrechenbaresVermoegen(elternteil, stammdaten);
        final var einnahmenBGSA = Objects.requireNonNullElse(elternteil.getEinnahmenBGSA(), 0);
        final var andereEinnahmen = Objects.requireNonNullElse(elternteil.getAndereEinnahmen(), 0);
        final var renten = elternteil.getRenten();

        final var einnahmenBeforeVermoegen = max(
            totalEinkuenfte
            + InputUtils.sumNullables(
                ergaenzungsleistungen,
                einnahmenBGSA,
                andereEinnahmen
            )
            - InputUtils.sumNullables(
                eigenmietwert,
                unterhaltsbeitraege,
                saeule3a,
                saeule2,
                renten,
                stammdaten.getEinkommensfreibetrag()
            ),
            0
        );

        final var einnahmen = einnahmenBeforeVermoegen + anrechenbaresVermoegen;

        // Set calculated values on dto
        return new FamilienBudgetresultatEinnahmenDto()
            .total(einnahmen)
            .totalEinkuenfte(totalEinkuenfte)
            .einnahmenBGSA(einnahmenBGSA)
            .ergaenzungsleistungen(ergaenzungsleistungen)
            .andereEinnahmen(andereEinnahmen)
            .eigenmietwert(eigenmietwert)
            .unterhaltsbeitraege(unterhaltsbeitraege)
            .sauele3(saeule3a)
            .sauele2(saeule2)
            .renten(renten)
            .einkommensfreibetrag(stammdaten.getEinkommensfreibetrag())
            .zwischentotal(einnahmenBeforeVermoegen)
            .anrechenbaresVermoegen(anrechenbaresVermoegen)
            .steuerbaresVermoegen(steuerbaresVermoegen);
    }

    private BigDecimal calculateAnteilLebenshaltungskosten(
        final AntragsstellerV1 antragssteller,
        final BigDecimal total,
        final int anzahlPersonenImHaushalt,
        final int elternHaushalt
    ) {
        if (
            (total.signum() >= 0)
            || antragssteller.isEigenerHaushalt()
            || (antragssteller.getPiaWohntInElternHaushalt() != elternHaushalt)
        ) {
            return BigDecimal.ZERO;
        }

        return total
            .divide(
                BigDecimal.valueOf(anzahlPersonenImHaushalt),
                RoundingMode.HALF_UP
            )
            .abs();
    }

    private int getSaeule3a(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        var saeule3a = 0;
        if (elternteil.isSelbststaendigErwerbend()) {
            saeule3a =
                max(Objects.requireNonNullElse(elternteil.getEinzahlungSaeule3a(), 0) - stammdaten.getMaxSaeule3a(), 0);
        }
        return saeule3a;
    }

    private int getSaeule2(
        final ElternteilV1 elternteil
    ) {
        var saeule2 = 0;
        if (elternteil.isSelbststaendigErwerbend()) {
            saeule2 = Objects.requireNonNullElse(elternteil.getEinzahlungSaeule2(), 0);
        }
        return saeule2;
    }

    private int getAnrechenbaresVermoegen(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        var anrechenbaresVermoegen = BigDecimal.valueOf(
            elternteil.getVermoegen()
        );
        if (elternteil.isSelbststaendigErwerbend()) {
            // steuerbaresVermoegen - freibetragVermoegen
            anrechenbaresVermoegen =
                BigDecimal.valueOf(
                    max(
                        elternteil.getVermoegen() - stammdaten.getFreibetragVermoegen(),
                        0
                    )
                );
        }
        return roundHalfUp(
            anrechenbaresVermoegen.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(stammdaten.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                .abs()
        );
    }
}
