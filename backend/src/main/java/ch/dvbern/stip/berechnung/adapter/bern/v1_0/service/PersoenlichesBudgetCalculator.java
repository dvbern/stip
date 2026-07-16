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

import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.berechnung.adapter.bern.util.BernCalculatorUtil;
import ch.dvbern.stip.berechnung.domain.dto.PersonValueList;
import ch.dvbern.stip.berechnung.domain.util.InputUtils;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDto;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.berechnung.domain.util.InputUtils.toJahresWert;
import static ch.dvbern.stip.berechnung.domain.util.MathUtil.divideByTranchen;
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
        final var pia = gesuchFormular.getPersonInAusbildung();
        final var partner = gesuchFormular.getPartner();

        final var haushaltNames = new ArrayList<String>();
        haushaltNames.add(pia.getFullName());

        var anzahlPersonenImHaushalt = 0;
        final var eigenerHaushalt = pia.getWohnsitz().isEigenerHaushalt();

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

        final var einnahmen = calculateEinnahmen(
            gesuchFormular,
            familienBudgetresultats,
            kindsImHaushalt,
            gesuchsperiode,
            gesuchsDateRange
        );
        final var kosten = calculateKosten(
            gesuchFormular,
            familienBudgetresultats,
            kindsImHaushalt,
            anzahlPersonenImHaushalt,
            gesuchsperiode,
            gesuchsjahr
        );

        final var einnahmenMinusKosten = BigDecimal.valueOf(einnahmen.getTotal() - kosten.getTotal());

        var total = BigDecimal.ZERO;
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
            if (anzahlMonateGueltigkeit != 12) {
                total = divideByTranchen(total, anzahlMonateGueltigkeit);
            }
        }

        return new PersoenlichesBudgetresultatDto()
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
            );
    }

    private PersoenlichesBudgetresultatEinnahmenDto calculateEinnahmen(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final List<Kind> kindsImHaushalt,
        final Gesuchsperiode gesuchsperiode,
        final DateRange gesuchsDateRange
    ) {
        final var pia = gesuchFormular.getPersonInAusbildung();
        final var piaName = pia.getVorname();
        final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final var abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final var nettoerwerbseinkommen = new PersonValueList();
        final var einnahmenBGSA = new PersonValueList();
        final var kinderAusbildungszulagen = new PersonValueList();
        final var unterhaltsbeitraege = new PersonValueList();
        final var eoLeistungen = new PersonValueList();
        final var taggelderAHVIV = new PersonValueList();
        final var renten = new PersonValueList();
        final var ergaenzungsleistungen = new PersonValueList();
        final var andereEinnahmen = new PersonValueList();
        final var beitraegeGemeindeInstitutionen = new PersonValueList();

        var nettoerwerbseinkommenPia = einnahmenKosten.getNettoerwerbseinkommen();
        if (abschluss.getBildungskategorie().isTertiaerstufe()) {
            nettoerwerbseinkommenPia = Math.max(nettoerwerbseinkommenPia - gesuchsperiode.getEinkommensfreibetrag(), 0);
        }
        nettoerwerbseinkommen.setPersonValue(piaName, nettoerwerbseinkommenPia);
        einnahmenBGSA.setPersonValue(piaName, einnahmenKosten.getEinnahmenBGSA());
        kinderAusbildungszulagen.setPersonValue(piaName, einnahmenKosten.getZulagen());
        unterhaltsbeitraege.setPersonValue(piaName, toJahresWert(einnahmenKosten.getUnterhaltsbeitraege()));
        eoLeistungen.setPersonValue(piaName, einnahmenKosten.getEoLeistungen());
        taggelderAHVIV.setPersonValue(piaName, einnahmenKosten.getTaggelderAHVIV());
        renten.setPersonValue(piaName, einnahmenKosten.getRenten());
        ergaenzungsleistungen.setPersonValue(piaName, einnahmenKosten.getErgaenzungsleistungen());
        andereEinnahmen.setPersonValue(piaName, einnahmenKosten.getAndereEinnahmen());
        beitraegeGemeindeInstitutionen.setPersonValue(piaName, einnahmenKosten.getBeitraege());

        var steuerbaresVermoegen = Objects.requireNonNullElse(einnahmenKosten.getVermoegen(), 0);

        if (pia.getZivilstand().hasPartnerschaft()) {
            final var partner = gesuchFormular.getPartner();
            assert partner != null;

            final var partnerName = partner.getVorname();
            final var einnahmenKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
            assert einnahmenKostenPartner != null;

            nettoerwerbseinkommen.setPartnerValue(partnerName, einnahmenKostenPartner.getNettoerwerbseinkommen());
            einnahmenBGSA.setPartnerValue(partnerName, einnahmenKostenPartner.getEinnahmenBGSA());
            kinderAusbildungszulagen.setPartnerValue(partnerName, einnahmenKostenPartner.getZulagen());
            unterhaltsbeitraege
                .setPartnerValue(partnerName, toJahresWert(einnahmenKostenPartner.getUnterhaltsbeitraege()));
            eoLeistungen.setPartnerValue(partnerName, einnahmenKostenPartner.getEoLeistungen());
            taggelderAHVIV.setPartnerValue(partnerName, einnahmenKostenPartner.getTaggelderAHVIV());
            renten.setPartnerValue(partnerName, einnahmenKostenPartner.getRenten());
            ergaenzungsleistungen.setPartnerValue(partnerName, einnahmenKostenPartner.getErgaenzungsleistungen());
            andereEinnahmen.setPartnerValue(partnerName, einnahmenKostenPartner.getAndereEinnahmen());
            beitraegeGemeindeInstitutionen.setPersonValue(partnerName, einnahmenKostenPartner.getBeitraege());
            steuerbaresVermoegen += Objects.requireNonNullElse(einnahmenKostenPartner.getVermoegen(), 0);
        }

        for (final var kind : kindsImHaushalt) {
            kinderAusbildungszulagen.addKindValue(
                kind,
                toJahresWert(kind.getKinderUndAusbildungszulagen())
            );
            unterhaltsbeitraege.addKindValue(kind, toJahresWert(kind.getUnterhaltsbeitraege()));
            renten.addKindValue(kind, kind.getRenten());
            ergaenzungsleistungen.addKindValue(kind, kind.getErgaenzungsleistungen());
            andereEinnahmen.addKindValue(kind, kind.getAndereEinnahmen());
        }

        final var nettoerwerbseinkommenTotal = InputUtils.sumValues(nettoerwerbseinkommen.toList());
        final var einnahmenBGSATotal = InputUtils.sumValues(einnahmenBGSA.toList());
        final var kinderAusbildungszulagenTotal = InputUtils.sumValues(kinderAusbildungszulagen.toList());
        final var unterhaltsbeitraegeTotal = InputUtils.sumValues(unterhaltsbeitraege.toList());
        final var eoLeistungenTotal = InputUtils.sumValues(eoLeistungen.toList());
        final var taggelderAHVIVTotal = InputUtils.sumValues(taggelderAHVIV.toList());
        final var rentenTotal = InputUtils.sumValues(renten.toList());
        final var ergaenzungsleistungenTotal = InputUtils.sumValues(ergaenzungsleistungen.toList());
        final var andereEinnahmenTotal = InputUtils.sumValues(andereEinnahmen.toList());
        final var beitraegeGemeindeInstitutionenTotal = InputUtils.sumValues(beitraegeGemeindeInstitutionen.toList());

        final var anrechenbaresVermoegen = roundHalfUp(
            BigDecimal.valueOf(
                steuerbaresVermoegen,
                0
            )
                .multiply(BigDecimal.valueOf(gesuchsperiode.getVermoegensanteilInProzent()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
        );

        final var elterlicheLeistung = BernCalculatorUtil.calculateElternbeitragTotal(
            gesuchFormular,
            familienBudgetresultats,
            gesuchsperiode,
            gesuchsDateRange
        );

        final var einnahmen =
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

        return new PersoenlichesBudgetresultatEinnahmenDto()
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
            .elterlicheLeistung(elterlicheLeistung);
    }

    private PersoenlichesBudgetresultatKostenDto calculateKosten(
        final GesuchFormular gesuchFormular,
        final List<FamilienBudgetresultatDto> familienBudgetresultats,
        final List<Kind> kindsImHaushalt,
        final int anzahlPersonenImHaushalt,
        final Gesuchsperiode gesuchsperiode,
        final int gesuchsjahr

    ) {
        final var pia = gesuchFormular.getPersonInAusbildung();
        final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final var abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final var medizinischeGrundversorgung = new PersonValueList();
        var grundbedarf = 0;
        var verpflegungskosten = 0;

        if (pia.getWohnsitz().isEigenerHaushalt()) {
            medizinischeGrundversorgung.setPersonValue(
                pia.getVorname(),
                BernCalculatorUtil.getMedizinischeGrundversorgung(
                    pia.getGeburtsdatum(),
                    gesuchsjahr,
                    gesuchsperiode
                )
            );

            final var isWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getWgWohnend());
            final var isAlternativeWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getAlternativeWohnformWohnend());
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
            var anzahlWochen = abschluss.getFerien() == FerienTyp.LEHRE
                ? gesuchsperiode.getAnzahlWochenLehre()
                : gesuchsperiode.getAnzahlWochenSchule();
            verpflegungskosten = roundHalfUp(
                BigDecimal.valueOf(Objects.requireNonNullElse(einnahmenKosten.getAuswaertigeMittagessenProWoche(), 0))
                    .multiply(BigDecimal.valueOf(anzahlWochen))
                    .multiply(BigDecimal.valueOf(gesuchsperiode.getPreisProMahlzeit()))
            );
        }

        final var ausbildungskosten = BernCalculatorUtil.getAusbildungskosten(
            einnahmenKosten.getAusbildungskosten(),
            gesuchsperiode,
            abschluss.getBildungskategorie()
        );
        var ausbildungskostenTotal = ausbildungskosten;

        var fahrkosten = Objects.requireNonNullElse(einnahmenKosten.getFahrkosten(), 0);
        var fahrkostenTotal = fahrkosten;

        var fahrkostenPartner = 0;
        var verpflegungPartner = 0;

        var steuern = Objects.requireNonNullElse(einnahmenKosten.getSteuern(), 0);

        if (pia.getZivilstand().hasPartnerschaft()) {
            ausbildungskostenTotal = roundHalfUp(
                BigDecimal.valueOf(ausbildungskosten)
                    .multiply(BigDecimal.valueOf(anzahlPersonenImHaushalt))
            );

            fahrkostenTotal = roundHalfUp(
                BigDecimal.valueOf(fahrkosten)
                    .multiply(BigDecimal.valueOf(anzahlPersonenImHaushalt))
            );

            final var einnahmenKostenPartner = gesuchFormular.getEinnahmenKostenPartner();
            assert einnahmenKostenPartner != null;
            fahrkostenPartner = Objects.requireNonNullElse(einnahmenKostenPartner.getFahrkosten(), 0);
            verpflegungPartner = Objects.requireNonNullElse(einnahmenKostenPartner.getVerpflegungskosten(), 0);
            steuern += Objects.requireNonNullElse(einnahmenKostenPartner.getSteuern(), 0);

            if (pia.getWohnsitz().isEigenerHaushalt()) {
                final var partner = gesuchFormular.getPartner();
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

        final var betreuungskostenKinder = Objects.requireNonNullElse(einnahmenKosten.getBetreuungskostenKinder(), 0);

        var wohnkosten = 0;
        if (einnahmenKosten.getWohnkosten() != null && anzahlPersonenImHaushalt > 0) {
            wohnkosten += BernCalculatorUtil.getEffektiveWohnkostenPersoenlich(
                toJahresWert(einnahmenKosten.getWohnkosten()),
                gesuchsperiode,
                anzahlPersonenImHaushalt
            );
        }

        final var medizinischeGrundversorgungTotal = InputUtils.sumValues(medizinischeGrundversorgung.toList());

        final var anteilLebenshaltungskosten = familienBudgetresultats.stream()
            .filter(
                familienBudgetresultatDto -> Objects
                    .nonNull(familienBudgetresultatDto.getUngedeckterAnteilLebenshaltungskosten())
            )
            .mapToInt(
                familienBudgetresultat -> Objects
                    .requireNonNullElse(familienBudgetresultat.getUngedeckterAnteilLebenshaltungskosten(), 0)
            )
            .sum();

        final var kosten =
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

        return new PersoenlichesBudgetresultatKostenDto()
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
            .fahrkostenPartner(fahrkostenPartner)
            .verpflegungPartner(verpflegungPartner)
            .betreuungskostenKinder(betreuungskostenKinder)
            .steuern(steuern)
            .anteilLebenshaltungskosten(anteilLebenshaltungskosten);
    }
}
