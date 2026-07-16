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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.domain.dto.PersonValueList;
import ch.dvbern.stip.berechnung.domain.util.InputUtils;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.domain.util.InputUtils.toJahresWert;
import static java.lang.Math.max;

@UtilityClass
public class FamilienBudgetCalculator {
    public FamilienBudgetresultatDto calculateFamilienBudget(
        final List<Eltern> elterns,
        final Steuerdaten steuerdaten,
        final Steuererklaerung steuererklaerung,
        final Gesuchsperiode gesuchsperiode,
        final List<AbstractFamilieEntity> kinderImHaushalt,
        // THIS IS WEIRD, for some reason in some of the calculations (Integrationsbeitrag) ALL the geschwisters are
        // used even if they dont live here. For others only the ones in this haushalt are relevant. Weird but true.
        // This number is ALL the Geschwisters + PiA.
        // TODO: Verify again that each time this is used it should be used
        final int anzahlKinderInAusbildung,
        final boolean halbierungElternbeitrag,
        final int gesuchsjahr
    ) {
        final var steuerdatenTyp = steuerdaten.getSteuerdatenTyp();

        var anzahlPersonenImHaushalt = 0;
        final List<String> haushaltNames = new ArrayList<>();

        String vorname = null;
        String nachname = null;
        String sozialversicherungsnummer = null;
        LocalDate geburtsdatum = null;
        String vornamePartner = null;
        String nachnamePartner = null;
        String sozialversicherungsnummerPartner = null;
        LocalDate geburtsdatumPartner = null;

        for (final Eltern eltern : elterns) {
            anzahlPersonenImHaushalt += 1;
            haushaltNames.add(eltern.getFullName());

            if (elterns.getFirst().equals(eltern)) {
                vorname = eltern.getVorname();
                nachname = eltern.getNachname();
                sozialversicherungsnummer = eltern.getSozialversicherungsnummer();
                geburtsdatum = eltern.getGeburtsdatum();
            } else {
                vornamePartner = eltern.getVorname();
                nachnamePartner = eltern.getNachname();
                sozialversicherungsnummerPartner = eltern.getSozialversicherungsnummer();
                geburtsdatumPartner = eltern.getGeburtsdatum();
            }

            // If the Elterns are separated there can! only be one eltern in elterns
            if (Objects.requireNonNullElse(eltern.getWiederverheiratet(), false)) {
                anzahlPersonenImHaushalt += 1;
                haushaltNames.add(BernCalculatorUtil.getElternPartnerName(eltern.getElternTyp()));
            }
        }

        for (final AbstractFamilieEntity kind : kinderImHaushalt) {
            anzahlPersonenImHaushalt += 1;
            haushaltNames.add(kind.getFullName());
        }

        final var einnahmen = calculateEinnahmen(
            steuerdaten,
            steuererklaerung,
            gesuchsperiode
        );
        final var kosten = calculateKosten(
            elterns,
            steuerdaten,
            gesuchsperiode,
            anzahlPersonenImHaushalt,
            kinderImHaushalt,
            anzahlKinderInAusbildung,
            gesuchsjahr
        );

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

        final boolean antragsstellerWohntInDiesemHaushalt = kinderImHaushalt.stream()
            .anyMatch(
                abstractFamilieEntity -> abstractFamilieEntity instanceof PersonInAusbildung
            );

        // If Kosten exceeds einnahmen, fehlbetrag values are filled
        if (einnahmenMinusKosten.signum() < 0) {
            fehlbetrag = total.abs();
            proKopfTeilung = anzahlPersonenImHaushalt;
            ungedeckterAnteilLebenshaltungskosten =
                BernCalculatorUtil.calculateAnteilLebenshaltungskosten(
                    antragsstellerWohntInDiesemHaushalt,
                    total,
                    anzahlPersonenImHaushalt
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
            if (halbierungElternbeitrag) {
                halbierungsReduktion = anrechenbareElterlicheLeistung.divide(BigDecimal.TWO, RoundingMode.HALF_UP);
                total = halbierungsReduktion;
            }
        }

        final var teilzeitKinderProzente = kinderImHaushalt.stream()
            .filter(
                abstractFamilieEntity -> abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue() > 0
                && abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue() < 100
            )
            .mapToInt(abstractFamilieEntity -> abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue())
            .sum();

        final var steuerjahr = steuerdaten.getSteuerjahr();
        final var veranlagungscode = steuerdaten.getVeranlagungsStatus();

        return new FamilienBudgetresultatDto()
            .steuerdatenTyp(steuerdatenTyp)
            .haushaltNames(haushaltNames)
            .vorname(vorname)
            .nachname(nachname)
            .sozialversicherungsnummer(sozialversicherungsnummer)
            .geburtsdatum(geburtsdatum)
            .vornamePartner(vornamePartner)
            .nachnamePartner(nachnamePartner)
            .sozialversicherungsnummerPartner(sozialversicherungsnummerPartner)
            .geburtsdatumPartner(geburtsdatumPartner)
            .steuerjahr(steuerjahr)
            .veranlagungscode(veranlagungscode)
            .total(total.intValue())
            .einnahmenMinusKosten(einnahmenMinusKosten.intValue())
            .anzahlPersonenImHaushalt(anzahlPersonenImHaushalt)
            .anzahlKinderInAusbildung(anzahlKinderInAusbildung)
            .einnahmeUeberschuss(einnahmeUeberschuss.intValue())
            .proKopfTeilungKinderInAusbildung(proKopfTeilungKinderInAusbildung)
            .anrechenbareElterlicheLeistung(anrechenbareElterlicheLeistung.intValue())
            .halbierungsReduktion(halbierungsReduktion.intValue())
            .fehlbetrag(fehlbetrag.intValue())
            .proKopfTeilung(proKopfTeilung)
            .ungedeckterAnteilLebenshaltungskosten(ungedeckterAnteilLebenshaltungskosten.intValue())
            .teilzeitKinderProzente(teilzeitKinderProzente)
            .einnahmen(einnahmen)
            .kosten(kosten);
    }

    private FamilienBudgetresultatEinnahmenDto calculateEinnahmen(
        final Steuerdaten steuerdaten,
        final Steuererklaerung steuererklaerung,
        final Gesuchsperiode gesuchsperiode
    ) {
        final var totalEinkuenfte = Objects.requireNonNullElse(steuerdaten.getTotalEinkuenfte(), 0);
        final var ergaenzungsleistungen = Objects.requireNonNullElse(steuererklaerung.getErgaenzungsleistungen(), 0);
        final var einnahmenBGSA = Objects.requireNonNullElse(steuererklaerung.getEinnahmenBGSA(), 0);
        final var andereEinnahmen = Objects.requireNonNullElse(steuererklaerung.getAndereEinnahmen(), 0);

        final var eigenmietwert = Objects.requireNonNullElse(steuerdaten.getEigenmietwert(), 0);
        final var unterhaltsbeitraege =
            toJahresWert(Objects.requireNonNullElse(steuererklaerung.getUnterhaltsbeitraege(), 0));
        final var saeule3a = BernCalculatorUtil.getSaeule3a(steuerdaten, gesuchsperiode);
        final var saeule2 = BernCalculatorUtil.getSaeule2(steuerdaten);

        final var renten = Objects.requireNonNullElse(steuererklaerung.getRenten(), 0);

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
                gesuchsperiode.getEinkommensfreibetrag()
            ),
            0
        );

        final var steuerbaresVermoegen = steuerdaten.getVermoegen();

        final var anrechenbaresVermoegen = BernCalculatorUtil.getAnrechenbaresVermoegen(
            steuerbaresVermoegen,
            steuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            gesuchsperiode
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
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .zwischentotal(einnahmenBeforeVermoegen)
            .anrechenbaresVermoegen(anrechenbaresVermoegen)
            .steuerbaresVermoegen(steuerbaresVermoegen);
    }

    private FamilienBudgetresultatKostenDto calculateKosten(
        final List<Eltern> elterns,
        final Steuerdaten steuerdaten,
        final Gesuchsperiode gesuchsperiode,
        final Integer anzahlPersonenImHaushalt,
        final List<AbstractFamilieEntity> kinderImHaushalt,
        final int anzahlKinderInAusbildung,
        final int gesuchsjahr
    ) {

        final var grundbedarf = BernCalculatorUtil.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false);
        final var effektiveWohnkosten = BernCalculatorUtil.getEffektiveWohnkostenFamilie(
            InputUtils.toJahresWert(elterns.getFirst().getWohnkosten()),
            gesuchsperiode,
            anzahlPersonenImHaushalt
        );
        final var kantonsGemeindesteuern = Objects.requireNonNullElse(steuerdaten.getSteuernKantonGemeinde(), 0);
        final var bundessteuern = Objects.requireNonNullElse(steuerdaten.getSteuernBund(), 0);

        final var integrationszulage = gesuchsperiode.getIntegrationszulage();
        final var integrationszulageTotal = Integer.min(
            gesuchsperiode.getIntegrationszulage() * anzahlKinderInAusbildung,
            gesuchsperiode.getLimiteEkFreibetragIntegrationszulage() - gesuchsperiode.getEinkommensfreibetrag()
        );

        final var verpflegungskostens = new PersonValueList();
        final var fahrkostens = new PersonValueList();

        int medizinischeGrundversorgung = 0;
        for (final Eltern eltern : elterns) {
            if (elterns.indexOf(eltern) == 0) {
                verpflegungskostens.setPersonValue(eltern.getVorname(), steuerdaten.getVerpflegung());
                fahrkostens.setPersonValue(eltern.getVorname(), steuerdaten.getFahrkosten());
            } else {
                verpflegungskostens.setPartnerValue(eltern.getVorname(), steuerdaten.getVerpflegungPartner());
                fahrkostens.setPartnerValue(eltern.getVorname(), steuerdaten.getFahrkostenPartner());
            }

            medizinischeGrundversorgung += BernCalculatorUtil.getMedizinischeGrundversorgung(
                eltern.getGeburtsdatum(),
                gesuchsjahr,
                gesuchsperiode
            );

            // If the Elterns are separated there can! only be one eltern in elterns
            if (Objects.requireNonNullElse(eltern.getWiederverheiratet(), false)) {
                verpflegungskostens.setPersonValue(
                    BernCalculatorUtil.getElternPartnerName(eltern.getElternTyp()),
                    steuerdaten.getVerpflegungPartner()
                );
                fahrkostens.setPersonValue(
                    BernCalculatorUtil.getElternPartnerName(eltern.getElternTyp()),
                    steuerdaten.getFahrkostenPartner()
                );
                medizinischeGrundversorgung += gesuchsperiode.getErwachsene2599();
            }
        }

        for (final AbstractFamilieEntity kind : kinderImHaushalt) {
            medizinischeGrundversorgung += BernCalculatorUtil.getMedizinischeGrundversorgung(
                kind.getGeburtsdatum(),
                gesuchsjahr,
                gesuchsperiode
            );
        }

        final var fahrkostenTotal = InputUtils.sumValues(fahrkostens.toList());
        final var verpflegungskostenTotal = InputUtils.sumValues(verpflegungskostens.toList());

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
            .integrationszulageAnzahl(anzahlKinderInAusbildung)
            .integrationszulageTotal(integrationszulageTotal)
            .kantonsGemeindesteuern(kantonsGemeindesteuern)
            .bundessteuern(bundessteuern)
            .fahrkosten(fahrkostens.toList())
            .fahrkostenTotal(fahrkostenTotal)
            .verpflegung(verpflegungskostens.toList())
            .verpflegungTotal(verpflegungskostenTotal);
    }
}
