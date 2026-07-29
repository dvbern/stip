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
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.domain.dto.PersonValueList;
import ch.dvbern.stip.berechnung.domain.util.InputUtils;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDtoBuilder;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDtoBuilder;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDtoBuilder;
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
        final int anzahlKinderInAusbildung,
        final boolean halbierungElternbeitrag,
        final int gesuchsjahr
    ) {
        final SteuerdatenTyp steuerdatenTyp = steuerdaten.getSteuerdatenTyp();

        int anzahlPersonenImHaushalt = 0;
        final List<String> haushaltNames = new ArrayList<>();

        String vorname = null;
        String nachname = null;
        String sozialversicherungsnummer = null;
        LocalDate geburtsdatum = null;
        String vornamePartner = null;
        String nachnamePartner = null;
        String sozialversicherungsnummerPartner = null;
        LocalDate geburtsdatumPartner = null;

        for (final Eltern elternTeil : elterns) {
            anzahlPersonenImHaushalt += 1;
            haushaltNames.add(elternTeil.getFullName());

            if (elterns.getFirst().equals(elternTeil)) {
                vorname = elternTeil.getVorname();
                nachname = elternTeil.getNachname();
                sozialversicherungsnummer = elternTeil.getSozialversicherungsnummer();
                geburtsdatum = elternTeil.getGeburtsdatum();
            } else {
                vornamePartner = elternTeil.getVorname();
                nachnamePartner = elternTeil.getNachname();
                sozialversicherungsnummerPartner = elternTeil.getSozialversicherungsnummer();
                geburtsdatumPartner = elternTeil.getGeburtsdatum();
            }

            // If the Elterns are separated there can! only be one eltern in elterns
            if (Objects.requireNonNullElse(elternTeil.getWiederverheiratet(), false)) {
                anzahlPersonenImHaushalt += 1;
                haushaltNames.add(BernCalculatorUtil.getElternPartnerName(elternTeil.getElternTyp()));
            }
        }

        for (final AbstractFamilieEntity kind : kinderImHaushalt) {
            anzahlPersonenImHaushalt += 1;
            haushaltNames.add(kind.getFullName());
        }

        final FamilienBudgetresultatEinnahmenDto einnahmen = calculateEinnahmen(
            steuerdaten,
            steuererklaerung,
            gesuchsperiode
        );
        final FamilienBudgetresultatKostenDto kosten = calculateKosten(
            elterns,
            steuerdaten,
            gesuchsperiode,
            anzahlPersonenImHaushalt,
            kinderImHaushalt,
            anzahlKinderInAusbildung,
            gesuchsjahr
        );

        final BigDecimal einnahmenMinusKosten =
            BigDecimal.valueOf(einnahmen.getTotal()).subtract(BigDecimal.valueOf(kosten.getTotal()));
        BigDecimal total = einnahmenMinusKosten;

        BigDecimal einnahmeUeberschuss = BigDecimal.ZERO;
        int proKopfTeilungKinderInAusbildung = 0;
        BigDecimal anrechenbareElterlicheLeistung = BigDecimal.ZERO;
        BigDecimal halbierungsReduktion = BigDecimal.ZERO;
        BigDecimal fehlbetrag = BigDecimal.ZERO;
        int proKopfTeilung = 0;
        BigDecimal ungedeckterAnteilLebenshaltungskosten = BigDecimal.ZERO;

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

        final int teilzeitKinderProzente = kinderImHaushalt.stream()
            .filter(
                abstractFamilieEntity -> abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue() > 0
                && abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue() < 100
            )
            .mapToInt(abstractFamilieEntity -> abstractFamilieEntity.getWohnsitzAnteil(steuerdatenTyp).intValue())
            .sum();

        final Integer steuerjahr = steuerdaten.getSteuerjahr();
        final String veranlagungscode = steuerdaten.getVeranlagungsStatus();

        return FamilienBudgetresultatDtoBuilder.familienBudgetresultatDto()
            .haushaltNames(haushaltNames)
            .steuerdatenTyp(steuerdatenTyp)
            .vorname(vorname)
            .nachname(nachname)
            .sozialversicherungsnummer(sozialversicherungsnummer)
            .geburtsdatum(geburtsdatum)
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
            .kosten(kosten)
            .vornamePartner(vornamePartner)
            .nachnamePartner(nachnamePartner)
            .sozialversicherungsnummerPartner(sozialversicherungsnummerPartner)
            .geburtsdatumPartner(geburtsdatumPartner)
            .build();
    }

    private FamilienBudgetresultatEinnahmenDto calculateEinnahmen(
        final Steuerdaten steuerdaten,
        final Steuererklaerung steuererklaerung,
        final Gesuchsperiode gesuchsperiode
    ) {
        final int totalEinkuenfte = BernCalculatorUtil.intOrZero(steuerdaten.getTotalEinkuenfte());
        final int ergaenzungsleistungen = BernCalculatorUtil.intOrZero(steuererklaerung.getErgaenzungsleistungen());
        final int einnahmenBGSA = BernCalculatorUtil.intOrZero(steuererklaerung.getEinnahmenBGSA());
        final int andereEinnahmen = BernCalculatorUtil.intOrZero(steuererklaerung.getAndereEinnahmen());

        final int eigenmietwert = BernCalculatorUtil.intOrZero(steuerdaten.getEigenmietwert());
        final int unterhaltsbeitraege =
            toJahresWert(BernCalculatorUtil.intOrZero(steuererklaerung.getUnterhaltsbeitraege()));
        final int saeule3a = BernCalculatorUtil.getSaeule3a(steuerdaten, gesuchsperiode);
        final int saeule2 = BernCalculatorUtil.getSaeule2(steuerdaten);

        final int renten = BernCalculatorUtil.intOrZero(steuererklaerung.getRenten());

        final int einnahmenBeforeVermoegen = max(
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

        final Integer steuerbaresVermoegen = steuerdaten.getVermoegen();

        final int anrechenbaresVermoegen = BernCalculatorUtil.getAnrechenbaresVermoegen(
            steuerbaresVermoegen,
            steuerdaten.getIsArbeitsverhaeltnisSelbstaendig(),
            gesuchsperiode
        );

        final int einnahmen = einnahmenBeforeVermoegen + anrechenbaresVermoegen;

        // Set calculated values on dto
        return FamilienBudgetresultatEinnahmenDtoBuilder.familienBudgetresultatEinnahmenDto()
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
            .steuerbaresVermoegen(steuerbaresVermoegen)
            .build();
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

        final int grundbedarf = BernCalculatorUtil.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false);
        final int effektiveWohnkosten = BernCalculatorUtil.getEffektiveWohnkostenFamilie(
            InputUtils.toJahresWert(elterns.getFirst().getWohnkosten()),
            gesuchsperiode,
            anzahlPersonenImHaushalt
        );
        final int kantonsGemeindesteuern = BernCalculatorUtil.intOrZero(steuerdaten.getSteuernKantonGemeinde());
        final int bundessteuern = BernCalculatorUtil.intOrZero(steuerdaten.getSteuernBund());

        final Integer integrationszulage = gesuchsperiode.getIntegrationszulage();
        final int integrationszulageTotal = Integer.min(
            gesuchsperiode.getIntegrationszulage() * anzahlKinderInAusbildung,
            gesuchsperiode.getLimiteEkFreibetragIntegrationszulage() - gesuchsperiode.getEinkommensfreibetrag()
        );

        final PersonValueList verpflegungskostens = new PersonValueList();
        final PersonValueList fahrkostens = new PersonValueList();

        int medizinischeGrundversorgung = 0;
        for (final Eltern elternTeil : elterns) {
            if (elterns.indexOf(elternTeil) == 0) {
                verpflegungskostens.setPersonValue(elternTeil.getVorname(), steuerdaten.getVerpflegung());
                fahrkostens.setPersonValue(elternTeil.getVorname(), steuerdaten.getFahrkosten());
            } else {
                verpflegungskostens.setPartnerValue(elternTeil.getVorname(), steuerdaten.getVerpflegungPartner());
                fahrkostens.setPartnerValue(elternTeil.getVorname(), steuerdaten.getFahrkostenPartner());
            }

            medizinischeGrundversorgung += BernCalculatorUtil.getMedizinischeGrundversorgung(
                elternTeil.getGeburtsdatum(),
                gesuchsjahr,
                gesuchsperiode
            );

            // If the Elterns are separated there can! only be one eltern in elterns
            if (Objects.requireNonNullElse(elternTeil.getWiederverheiratet(), false)) {
                verpflegungskostens.setPersonValue(
                    BernCalculatorUtil.getElternPartnerName(elternTeil.getElternTyp()),
                    steuerdaten.getVerpflegungPartner()
                );
                fahrkostens.setPersonValue(
                    BernCalculatorUtil.getElternPartnerName(elternTeil.getElternTyp()),
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

        final int fahrkostenTotal = InputUtils.sumValues(fahrkostens.toList());
        final int verpflegungskostenTotal = InputUtils.sumValues(verpflegungskostens.toList());

        final int ausgaben =
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
        return FamilienBudgetresultatKostenDtoBuilder.familienBudgetresultatKostenDto()
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
            .verpflegungTotal(verpflegungskostenTotal)
            .build();
    }
}
