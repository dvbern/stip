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
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputFamilienbudgetV1;
import ch.dvbern.stip.berechnung.dto.v1.ElternteilV1;
import ch.dvbern.stip.berechnung.dto.v1.StammdatenV1;
import ch.dvbern.stip.berechnung.util.MathUtil;
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
        final StammdatenV1 stammdaten,
        final int elternHaushalt
    ) {
        if (input == null) {
            return null;
        }

        final var elternteil = input.getElternteil();

        final var anzahlPersonenImHaushalt = elternteil.getAnzahlPersonenImHaushalt();
        final var anzahlKinderInAusbildung = elternteil.getAnzahlGeschwisterInAusbildung() + MathUtil.PIA_COUNT;
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
                calculateAnteilLebenshaltungskosten(antragssteller, total, anzahlPersonenImHaushalt, elternHaushalt); // TODO:
                                                                                                                      // check
                                                                                                                      // if
                                                                                                                      // correct
            total = ungedeckterAnteilLebenshaltungskosten.negate();
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

        return new FamilienBudgetresultatDto(
            elternteil.getSteuerdatenTyp(),
            roundHalfUp(total),
            roundHalfUp(einnahmenMinusKosten),
            anzahlPersonenImHaushalt,
            anzahlKinderInAusbildung,
            roundHalfUp(einnahmeUeberschuss),
            proKopfTeilungKinderInAusbildung,
            roundHalfUp(anrechenbareElterlicheLeistung),
            roundHalfUp(halbierungsReduktion),
            roundHalfUp(fehlbetrag),
            proKopfTeilung,
            roundHalfUp(ungedeckterAnteilLebenshaltungskosten),
            einnahmen,
            kosten
        );
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
        final var fahrkosten = elternteil.getFahrkosten();
        final var fahrkostenPartner = elternteil.getFahrkostenPartner();
        final var verpflegungskosten = elternteil.getVerpflegungskosten();
        final var verpflegungskostenPartner = elternteil.getVerpflegungskostenPartner();

        final var ausgaben =
            grundbedarf
            + effektiveWohnkosten
            + medizinischeGrundversorgung
            + kantonsGemeindesteuern
            + bundessteuern
            + integrationszulageTotal
            + fahrkosten
            + fahrkostenPartner
            + verpflegungskosten
            + verpflegungskostenPartner;

        // Set calculated values on dto
        return new FamilienBudgetresultatKostenDto(
            ausgaben,
            grundbedarf,
            effektiveWohnkosten,
            medizinischeGrundversorgung,
            integrationszulage,
            integrationszulageAnzahl,
            integrationszulageTotal,
            kantonsGemeindesteuern,
            bundessteuern,
            fahrkosten,
            fahrkostenPartner,
            fahrkosten + fahrkostenPartner,
            verpflegungskosten,
            verpflegungskostenPartner,
            verpflegungskosten + verpflegungskostenPartner
        );
    }

    private FamilienBudgetresultatEinnahmenDto calculateEinnahmen(
        final ElternteilV1 elternteil,
        final StammdatenV1 stammdaten
    ) {
        final var ergaenzungsleistungen = elternteil.getErgaenzungsleistungen();
        final var totalEinkuenfte = elternteil.getTotalEinkuenfte();
        final var eigenmietwert = elternteil.getEigenmietwert();
        final var unterhaltsbeitraege = elternteil.getUnterhaltsbeitraege();
        final var steuerbaresVermoegen = elternteil.getVermoegen();
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
            - unterhaltsbeitraege
            - saeule3a
            - saeule2
            - renten
            - stammdaten.getEinkommensfreibetrag(),
            0
        );

        final var einnahmen = einnahmenBeforeVermoegen + anrechenbaresVermoegen;

        // Set calculated values on dto
        return new FamilienBudgetresultatEinnahmenDto(
            einnahmen,
            totalEinkuenfte,
            einnahmenBGSA,
            ergaenzungsleistungen,
            andereEinnahmen,
            eigenmietwert,
            unterhaltsbeitraege,
            saeule3a,
            saeule2,
            renten,
            stammdaten.getEinkommensfreibetrag(),
            einnahmenBeforeVermoegen,
            anrechenbaresVermoegen,
            steuerbaresVermoegen
        );
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
