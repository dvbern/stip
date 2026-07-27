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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.domain.dto.PersonValueList;
import ch.dvbern.stip.berechnung.domain.util.InputUtils;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDtoBuilder;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDtoBuilder;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDtoBuilder;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.domain.util.MathUtil.roundHalfUp;

@UtilityClass
public class PersoenlichesBudgetCalculator {
    public PersoenlichesBudgetresultatDto calculatePersoenlichesBudget(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final List<Kind> kindsImHaushalt,
        final int anzahlMonateGueltigkeit,
        final DateRange gesuchsDateRange,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr
    ) {
        final PersonInAusbildung pia = gesuchFormular.getPersonInAusbildung();
        final Partner partner = gesuchFormular.getPartner();

        final ArrayList<String> haushaltNames = new ArrayList<String>();
        haushaltNames.add(pia.getFullName());

        int anzahlPersonenImHaushalt = 0;
        final boolean eigenerHaushalt = pia.getWohnsitz().isEigenerHaushalt();

        if (eigenerHaushalt) {
            anzahlPersonenImHaushalt += 1;
            anzahlPersonenImHaushalt += kindsImHaushalt.size();

            kindsImHaushalt.forEach(kind -> haushaltNames.add(kind.getFullName()));

            if (Objects.nonNull(partner)) {
                anzahlPersonenImHaushalt += 1;
                haushaltNames.add(partner.getFullName());
            }
        }

        String vornamePartner = null;
        String nachnamePartner = null;
        if (Objects.nonNull(partner)) {
            vornamePartner = partner.getVorname();
            nachnamePartner = partner.getNachname();
        }

        final PersoenlichesBudgetresultatEinnahmenDto einnahmen = calculateEinnahmen(
            gesuchFormular,
            familienBudgetresultats,
            kindsImHaushalt,
            gesuchsperiode,
            gesuchsDateRange
        );
        final PersoenlichesBudgetresultatKostenDto kosten = calculateKosten(
            gesuchFormular,
            familienBudgetresultats,
            kindsImHaushalt,
            anzahlPersonenImHaushalt,
            gesuchsperiode,
            gesuchsjahr
        );

        final BigDecimal einnahmenMinusKosten = BigDecimal.valueOf(einnahmen.getTotal() - kosten.getTotal());

        BigDecimal total = BigDecimal.ZERO;
        Integer proKopfTeilung = null;
        BigDecimal totalNachProKopfTeilung = null;

        if (einnahmenMinusKosten.signum() < 0) {
            total = einnahmenMinusKosten;
            if (pia.getZivilstand().hasPartnerschaft() && pia.getWohnsitz().isEigenerHaushalt()) {
                proKopfTeilung = anzahlPersonenImHaushalt;
                total = total
                    .divide(BigDecimal.valueOf(proKopfTeilung), RoundingMode.HALF_UP);;
                totalNachProKopfTeilung = total;
            }
        }

        return PersoenlichesBudgetresultatDtoBuilder.persoenlichesBudgetresultatDto()
            .haushaltNames(haushaltNames)
            .vorname(pia.getVorname())
            .nachname(pia.getNachname())
            .sozialversicherungsnummer(pia.getSozialversicherungsnummer())
            .geburtsdatum(pia.getGeburtsdatum())
            // TODO 66: Remove one of these, they were redundant?
            .total(roundHalfUp(total))
            // TODO 67: Remove one of these, they were redundant?
            .einnahmenMinusKosten(roundHalfUp(einnahmenMinusKosten))
            // TODO 67: Remove one of these, they were redundant?
            .fehlbetrag(roundHalfUp(einnahmenMinusKosten))
            .eigenerHaushalt(eigenerHaushalt)
            // TODO 66: Remove one of these, they were redundant?
            .budgetTranche(roundHalfUp(total))
            .anzahlMonate(anzahlMonateGueltigkeit)
            .anzahlPersonenImHaushalt(anzahlPersonenImHaushalt)
            .einnahmen(einnahmen)
            .kosten(kosten)
            .vornamePartner(vornamePartner)
            .nachnamePartner(nachnamePartner)
            .proKopfTeilung(proKopfTeilung)
            .totalNachProKopfTeilung(
                Objects.nonNull(totalNachProKopfTeilung) ? roundHalfUp(totalNachProKopfTeilung) : null
            )
            .build();
    }

    private PersoenlichesBudgetresultatEinnahmenDto calculateEinnahmen(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final List<Kind> kindsImHaushalt,
        final Gesuchsperiode gesuchsperiode,
        final DateRange gesuchsDateRange
    ) {
        final PersonInAusbildung pia = gesuchFormular.getPersonInAusbildung();
        final String piaName = pia.getVorname();
        final EinnahmenKosten einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final Abschluss abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final PersonValueList nettoerwerbseinkommen = new PersonValueList();
        final PersonValueList einnahmenBGSA = new PersonValueList();
        final PersonValueList kinderAusbildungszulagen = new PersonValueList();
        final PersonValueList unterhaltsbeitraege = new PersonValueList();
        final PersonValueList eoLeistungen = new PersonValueList();
        final PersonValueList taggelderAHVIV = new PersonValueList();
        final PersonValueList renten = new PersonValueList();
        final PersonValueList ergaenzungsleistungen = new PersonValueList();
        final PersonValueList andereEinnahmen = new PersonValueList();
        final PersonValueList beitraegeGemeindeInstitutionen = new PersonValueList();

        Integer nettoerwerbseinkommenPia = einnahmenKosten.getNettoerwerbseinkommen();
        if (abschluss.getBildungskategorie().isTertiaerstufe()) {
            nettoerwerbseinkommenPia = Math.max(nettoerwerbseinkommenPia - gesuchsperiode.getEinkommensfreibetrag(), 0);
        }
        nettoerwerbseinkommen.setPersonValue(piaName, nettoerwerbseinkommenPia);
        einnahmenBGSA.setPersonValue(piaName, einnahmenKosten.getEinnahmenBGSA());
        kinderAusbildungszulagen.setPersonValue(piaName, einnahmenKosten.getZulagen());
        unterhaltsbeitraege.setPersonValue(piaName, einnahmenKosten.getUnterhaltsbeitraege());
        eoLeistungen.setPersonValue(piaName, einnahmenKosten.getEoLeistungen());
        taggelderAHVIV.setPersonValue(piaName, einnahmenKosten.getTaggelderAHVIV());
        renten.setPersonValue(piaName, einnahmenKosten.getRenten());
        ergaenzungsleistungen.setPersonValue(piaName, einnahmenKosten.getErgaenzungsleistungen());
        andereEinnahmen.setPersonValue(piaName, einnahmenKosten.getAndereEinnahmen());
        beitraegeGemeindeInstitutionen.setPersonValue(piaName, einnahmenKosten.getBeitraege());

        int steuerbaresVermoegen = BernCalculatorUtil.intOrZero(einnahmenKosten.getVermoegen());

        if (pia.getZivilstand().hasPartnerschaft()) {
            final Partner partner = gesuchFormular.getPartner();
            assert partner != null;

            final String partnerName = partner.getVorname();
            final EinnahmenKosten einnahmenKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
            assert einnahmenKostenPartner != null;

            nettoerwerbseinkommen.setPartnerValue(partnerName, einnahmenKostenPartner.getNettoerwerbseinkommen());
            einnahmenBGSA.setPartnerValue(partnerName, einnahmenKostenPartner.getEinnahmenBGSA());
            kinderAusbildungszulagen.setPartnerValue(partnerName, einnahmenKostenPartner.getZulagen());
            unterhaltsbeitraege
                .setPartnerValue(partnerName, einnahmenKostenPartner.getUnterhaltsbeitraege());
            eoLeistungen.setPartnerValue(partnerName, einnahmenKostenPartner.getEoLeistungen());
            taggelderAHVIV.setPartnerValue(partnerName, einnahmenKostenPartner.getTaggelderAHVIV());
            renten.setPartnerValue(partnerName, einnahmenKostenPartner.getRenten());
            ergaenzungsleistungen.setPartnerValue(partnerName, einnahmenKostenPartner.getErgaenzungsleistungen());
            andereEinnahmen.setPartnerValue(partnerName, einnahmenKostenPartner.getAndereEinnahmen());
            beitraegeGemeindeInstitutionen.setPersonValue(partnerName, einnahmenKostenPartner.getBeitraege());
            steuerbaresVermoegen += BernCalculatorUtil.intOrZero(einnahmenKostenPartner.getVermoegen());
        }

        for (final Kind kind : kindsImHaushalt) {
            kinderAusbildungszulagen.addKindValue(
                kind,
                kind.getKinderUndAusbildungszulagen()
            );
            unterhaltsbeitraege.addKindValue(kind, kind.getUnterhaltsbeitraege());
            renten.addKindValue(kind, kind.getRenten());
            ergaenzungsleistungen.addKindValue(kind, kind.getErgaenzungsleistungen());
            andereEinnahmen.addKindValue(kind, kind.getAndereEinnahmen());
        }

        final int nettoerwerbseinkommenTotal = InputUtils.sumValues(nettoerwerbseinkommen.toList());
        final int einnahmenBGSATotal = InputUtils.sumValues(einnahmenBGSA.toList());
        final int kinderAusbildungszulagenTotal = InputUtils.sumValues(kinderAusbildungszulagen.toList());
        final int unterhaltsbeitraegeTotal = InputUtils.sumValues(unterhaltsbeitraege.toList());
        final int eoLeistungenTotal = InputUtils.sumValues(eoLeistungen.toList());
        final int taggelderAHVIVTotal = InputUtils.sumValues(taggelderAHVIV.toList());
        final int rentenTotal = InputUtils.sumValues(renten.toList());
        final int ergaenzungsleistungenTotal = InputUtils.sumValues(ergaenzungsleistungen.toList());
        final int andereEinnahmenTotal = InputUtils.sumValues(andereEinnahmen.toList());
        final int beitraegeGemeindeInstitutionenTotal = InputUtils.sumValues(beitraegeGemeindeInstitutionen.toList());

        final int anrechenbaresVermoegen = roundHalfUp(
            BigDecimal.valueOf(
                steuerbaresVermoegen,
                0
            )
                .multiply(BigDecimal.valueOf(gesuchsperiode.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );

        final Integer elterlicheLeistung = BernCalculatorUtil.calculateElternbeitragTotal(
            gesuchFormular,
            familienBudgetresultats,
            gesuchsperiode,
            gesuchsDateRange
        );

        final int einnahmen =
            nettoerwerbseinkommenTotal
            + InputUtils.sumNullables(
                anrechenbaresVermoegen,
                unterhaltsbeitraegeTotal,
                rentenTotal,
                kinderAusbildungszulagenTotal,
                ergaenzungsleistungenTotal,
                eoLeistungenTotal,
                beitraegeGemeindeInstitutionenTotal,
                einnahmenBGSATotal,
                taggelderAHVIVTotal,
                andereEinnahmenTotal,
                elterlicheLeistung
            );

        return PersoenlichesBudgetresultatEinnahmenDtoBuilder.persoenlichesBudgetresultatEinnahmenDto()
            .total(einnahmen)
            .nettoerwerbseinkommen(nettoerwerbseinkommen.toList())
            .nettoerwerbseinkommenTotal(nettoerwerbseinkommenTotal)
            .einnahmenBGSA(einnahmenBGSA.toList())
            .einnahmenBGSATotal(einnahmenBGSATotal)
            .kinderAusbildungszulagen(kinderAusbildungszulagen.toList())
            .kinderAusbildungszulagenTotal(kinderAusbildungszulagenTotal)
            .unterhaltsbeitraege(unterhaltsbeitraege.toList())
            .unterhaltsbeitraegeTotal(unterhaltsbeitraegeTotal)
            .eoLeistungen(eoLeistungen.toList())
            .eoLeistungenTotal(eoLeistungenTotal)
            .taggelderAHVIV(taggelderAHVIV.toList())
            .taggelderAHVIVTotal(taggelderAHVIVTotal)
            .renten(renten.toList())
            .rentenTotal(rentenTotal)
            .ergaenzungsleistungen(ergaenzungsleistungen.toList())
            .ergaenzungsleistungenTotal(ergaenzungsleistungenTotal)
            .beitraegeGemeindeInstitutionen(beitraegeGemeindeInstitutionenTotal)
            .andereEinnahmen(andereEinnahmen.toList())
            .andereEinnahmenTotal(andereEinnahmenTotal)
            .anrechenbaresVermoegen(anrechenbaresVermoegen)
            .steuerbaresVermoegen(steuerbaresVermoegen)
            .elterlicheLeistung(elterlicheLeistung)
            .build();
    }

    private PersoenlichesBudgetresultatKostenDto calculateKosten(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final List<Kind> kindsImHaushalt,
        final int anzahlPersonenImHaushalt,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr

    ) {
        final PersonInAusbildung pia = gesuchFormular.getPersonInAusbildung();
        final EinnahmenKosten einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final Abschluss abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final PersonValueList medizinischeGrundversorgung = new PersonValueList();
        int grundbedarf = 0;
        int verpflegungskosten = 0;

        if (pia.getWohnsitz().isEigenerHaushalt()) {
            medizinischeGrundversorgung.setPersonValue(
                pia.getVorname(),
                BernCalculatorUtil.getMedizinischeGrundversorgung(
                    pia.getGeburtsdatum(),
                    gesuchsjahr,
                    gesuchsperiode
                )
            );

            final boolean isWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getWgWohnend());
            final boolean isAlternativeWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getAlternativeWohnformWohnend());
            grundbedarf = BernCalculatorUtil.getGrundbedarf(
                gesuchsperiode,
                isAlternativeWgWohnend ? 1 : anzahlPersonenImHaushalt,
                isWgWohnend || isAlternativeWgWohnend
            );

            kindsImHaushalt.forEach(
                kind -> medizinischeGrundversorgung.addKindValue(
                    (Kind) kind,
                    BernCalculatorUtil.getMedizinischeGrundversorgung(
                        kind.getGeburtsdatum(),
                        gesuchsjahr,
                        gesuchsperiode
                    )
                )
            );
        } else {
            final Integer anzahlWochen = abschluss.getFerien() == FerienTyp.LEHRE
                ? gesuchsperiode.getAnzahlWochenLehre()
                : gesuchsperiode.getAnzahlWochenSchule();
            verpflegungskosten = roundHalfUp(
                BigDecimal.valueOf(BernCalculatorUtil.intOrZero(einnahmenKosten.getAuswaertigeMittagessenProWoche()))
                    .multiply(BigDecimal.valueOf(anzahlWochen))
                    .multiply(BigDecimal.valueOf(gesuchsperiode.getPreisProMahlzeit()))
            );
        }

        final int ausbildungskosten = BernCalculatorUtil.getAusbildungskosten(
            einnahmenKosten.getAusbildungskosten(),
            gesuchsperiode,
            abschluss.getBildungskategorie()
        );
        int ausbildungskostenTotal = ausbildungskosten;

        int fahrkosten = BernCalculatorUtil.intOrZero(einnahmenKosten.getFahrkosten());
        int fahrkostenTotal = fahrkosten;

        int fahrkostenPartner = 0;
        int verpflegungPartner = 0;

        int steuern = BernCalculatorUtil.intOrZero(einnahmenKosten.getSteuern());

        if (pia.getZivilstand().hasPartnerschaft()) {
            ausbildungskostenTotal = roundHalfUp(
                BigDecimal.valueOf(ausbildungskosten)
                    .multiply(BigDecimal.valueOf(anzahlPersonenImHaushalt))
            );

            fahrkostenTotal = roundHalfUp(
                BigDecimal.valueOf(fahrkosten)
                    .multiply(BigDecimal.valueOf(anzahlPersonenImHaushalt))
            );

            final EinnahmenKosten einnahmenKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
            assert einnahmenKostenPartner != null;
            fahrkostenPartner = BernCalculatorUtil.intOrZero(einnahmenKostenPartner.getFahrkosten());
            verpflegungPartner = BernCalculatorUtil.intOrZero(einnahmenKostenPartner.getVerpflegungskosten());
            steuern += BernCalculatorUtil.intOrZero(einnahmenKostenPartner.getSteuern());

            if (pia.getWohnsitz().isEigenerHaushalt()) {
                final Partner partner = gesuchFormular.getPartner();
                assert partner != null;
                medizinischeGrundversorgung.setPartnerValue(
                    partner.getVorname(),
                    BernCalculatorUtil.getMedizinischeGrundversorgung(
                        partner.getGeburtsdatum(),
                        gesuchsjahr,
                        gesuchsperiode
                    )
                );
            }
        }

        final int betreuungskostenKinder = BernCalculatorUtil.intOrZero(einnahmenKosten.getBetreuungskostenKinder());

        int wohnkosten = 0;
        if (einnahmenKosten.getWohnkosten() != null && anzahlPersonenImHaushalt > 0) {
            wohnkosten += BernCalculatorUtil.getEffektiveWohnkostenPersoenlich(
                einnahmenKosten.getWohnkosten(),
                gesuchsperiode,
                anzahlPersonenImHaushalt
            );
        }

        final int medizinischeGrundversorgungTotal = InputUtils.sumValues(medizinischeGrundversorgung.toList());

        final int anteilLebenshaltungskosten = familienBudgetresultats.stream()
            .filter(
                familienBudgetresultatDto -> Objects
                    .nonNull(familienBudgetresultatDto.getUngedeckterAnteilLebenshaltungskosten())
            )
            .mapToInt(
                familienBudgetresultat -> BernCalculatorUtil
                    .intOrZero(familienBudgetresultat.getUngedeckterAnteilLebenshaltungskosten())
            )
            .sum();

        final int kosten =
            grundbedarf
            + InputUtils.sumNullables(
                wohnkosten,
                medizinischeGrundversorgungTotal,
                ausbildungskostenTotal,
                steuern,
                fahrkostenTotal,
                fahrkostenPartner,
                verpflegungskosten,
                verpflegungPartner,
                betreuungskostenKinder,
                anteilLebenshaltungskosten
            );

        return PersoenlichesBudgetresultatKostenDtoBuilder.persoenlichesBudgetresultatKostenDto()
            .total(kosten)
            .ausbildungskosten(ausbildungskosten)
            .ausbildungskostenTotal(ausbildungskostenTotal)
            .fahrkosten(fahrkosten)
            .fahrkostenTotal(fahrkostenTotal)
            .verpflegungskosten(verpflegungskosten)
            .grundbedarf(grundbedarf)
            .wohnkosten(wohnkosten)
            .medizinischeGrundversorgung(medizinischeGrundversorgung.toList())
            .medizinischeGrundversorgungTotal(medizinischeGrundversorgungTotal)
            .betreuungskostenKinder(betreuungskostenKinder)
            .steuern(steuern)
            .anteilLebenshaltungskosten(anteilLebenshaltungskosten)
            .fahrkostenPartner(fahrkostenPartner)
            .verpflegungPartner(verpflegungPartner)
            .build();
    }
}
