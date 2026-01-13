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

package ch.dvbern.stip.api.demo.service;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.adresse.entity.AdresseBuilder;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungBuilder;
import ch.dvbern.stip.api.auszahlung.entity.AuszahlungBuilder;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityBuilder;
import ch.dvbern.stip.api.common.entity.AbstractPersonBuilder;
import ch.dvbern.stip.api.demo.entity.DemoPerson;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKostenBuilder;
import ch.dvbern.stip.api.eltern.entity.ElternBuilder;
import ch.dvbern.stip.api.fall.entity.FallBuilder;
import ch.dvbern.stip.api.familiensituation.entity.FamiliensituationBuilder;
import ch.dvbern.stip.api.geschwister.entity.GeschwisterBuilder;
import ch.dvbern.stip.api.gesuch.entity.GesuchBuilder;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormularBuilder;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTrancheBuilder;
import ch.dvbern.stip.api.kind.entity.KindBuilder;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemBuilder;
import ch.dvbern.stip.api.partner.entity.PartnerBuilder;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungBuilder;
import ch.dvbern.stip.api.steuererklaerung.entity.SteuererklaerungBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class GenerateDemoDataService {
    private final BenutzerService benutzerService;

    public void createEingereicht() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var auszahlung = AuszahlungBuilder.auszahlung()
            .zahlungsverbindung(null)
            .auszahlungAnSozialdienst(false)
            .sapBusinessPartnerId(null)
            .buchhaltung(null)
            .build();
        final var fall = FallBuilder.fall()
            .fallNummer(null)
            .gesuchsteller(benutzer)
            .ausbildungs(null)
            .buchhaltungs(null)
            .darlehens(null)
            .delegierung(null)
            .auszahlung(auszahlung)
            .build();
        final var ausbildung = AusbildungBuilder.ausbildung()
            .fall(fall)
            .gesuchs(null)
            .ausbildungsgang(null)
            .besuchtBMS(false)
            .alternativeAusbildungsgang(null)
            .alternativeAusbildungsstaette(null)
            .fachrichtungBerufsbezeichnung(null)
            .ausbildungNichtGefunden(false)
            .ausbildungBegin(null)
            .ausbildungEnd(null)
            .pensum(null)
            .ausbildungsort(null)
            .ausbildungsortPLZ(null)
            .isAusbildungAusland(null)
            .land(null)
            .status(null)
            .build();
        final var pia = DemoPerson.createPersonInAusbildung(
            PersonInAusbildungBuilder.personInAusbildung()
                .adresse(
                    AdresseBuilder.adresse()
                        .land(null)
                        .coAdresse(null)
                        .strasse(null)
                        .hausnummer(null)
                        .plz(null)
                        .ort(null)
                        .build()
                )
                .sozialversicherungsnummer(null)
                .anrede(null)
                .identischerZivilrechtlicherWohnsitz(true)
                .identischerZivilrechtlicherWohnsitzOrt(null)
                .identischerZivilrechtlicherWohnsitzPLZ(null)
                .email(null)
                .telefonnummer(null)
                .nationalitaet(null)
                .heimatort(null)
                .heimatortPLZ(null)
                .niederlassungsstatus(null)
                .einreisedatum(null)
                .zivilstand(null)
                .sozialhilfebeitraege(false)
                .vormundschaft(false)
                .korrespondenzSprache(null)
                .zustaendigeKESB(null),
            AbstractFamilieEntityBuilder.abstractFamilieEntity()
                .wohnsitz(null)
                .wohnsitzAnteilMutter(null)
                .wohnsitzAnteilVater(null),
            AbstractPersonBuilder.abstractPerson()
                .nachname(null)
                .vorname(null)
                .geburtsdatum(null)
        );
        final var lebenslauf = LebenslaufItemBuilder.lebenslaufItem()
            .von(null)
            .bis(null)
            .taetigkeitsart(null)
            .taetigkeitsBeschreibung(null)
            .fachrichtungBerufsbezeichnung(null)
            .ausbildungAbgeschlossen(false)
            .wohnsitz(null)
            .build();
        final var partner = DemoPerson.createPartner(
            PartnerBuilder.partner()
                .adresse(
                    AdresseBuilder.adresse()
                        .land(null)
                        .coAdresse(null)
                        .strasse(null)
                        .hausnummer(null)
                        .plz(null)
                        .ort(null)
                        .build()
                )
                .sozialversicherungsnummer(null)
                .inAusbildung(false)
                .ausbildungspensum(null),
            AbstractPersonBuilder.abstractPerson()
                .nachname(null)
                .vorname(null)
                .geburtsdatum(null)
        );
        final var kind = DemoPerson.createKind(
            KindBuilder.kind()
                .ausbildungssituation(null)
                .unterhaltsbeitraege(null)
                .wohnsitzAnteilPia(null)
                .kinderUndAusbildungszulagen(null)
                .renten(null)
                .andereEinnahmen(null),
            AbstractPersonBuilder.abstractPerson()
                .nachname(null)
                .vorname(null)
                .geburtsdatum(null)
        );
        final var einnahmenKosten = EinnahmenKostenBuilder.einnahmenKosten()
            .nettoerwerbseinkommen(null)
            .fahrkosten(null)
            .wohnkosten(null)
            .wgWohnend(null)
            .wgAnzahlPersonen(null)
            .alternativeWohnformWohnend(null)
            .unterhaltsbeitraege(null)
            .zulagen(null)
            .renten(null)
            .eoLeistungen(null)
            .ergaenzungsleistungen(null)
            .beitraege(null)
            .ausbildungskosten(null)
            .auswaertigeMittagessenProWoche(null)
            .verpflegungskosten(null)
            .betreuungskostenKinder(null)
            .veranlagungsStatus(null)
            .steuerjahr(null)
            .vermoegen(null)
            .einnahmenBGSA(null)
            .taggelderAHVIV(null)
            .andereEinnahmen(null)
            .arbeitspensumProzent(null)
            .build();
        final var einnahmenKostenPartner = EinnahmenKostenBuilder.einnahmenKosten()
            .nettoerwerbseinkommen(null)
            .fahrkosten(null)
            .wohnkosten(null)
            .wgWohnend(null)
            .wgAnzahlPersonen(null)
            .alternativeWohnformWohnend(null)
            .unterhaltsbeitraege(null)
            .zulagen(null)
            .renten(null)
            .eoLeistungen(null)
            .ergaenzungsleistungen(null)
            .beitraege(null)
            .ausbildungskosten(null)
            .auswaertigeMittagessenProWoche(null)
            .verpflegungskosten(null)
            .betreuungskostenKinder(null)
            .veranlagungsStatus(null)
            .steuerjahr(null)
            .vermoegen(null)
            .einnahmenBGSA(null)
            .taggelderAHVIV(null)
            .andereEinnahmen(null)
            .arbeitspensumProzent(null)
            .build();
        final var familiensituation = FamiliensituationBuilder.familiensituation()
            .elternVerheiratetZusammen(null)
            .elternteilUnbekanntVerstorben(null)
            .gerichtlicheAlimentenregelung(null)
            .mutterUnbekanntVerstorben(null)
            .mutterUnbekanntGrund(null)
            .mutterWiederverheiratet(null)
            .vaterUnbekanntVerstorben(null)
            .vaterUnbekanntGrund(null)
            .vaterWiederverheiratet(null)
            .werZahltAlimente(null)
            .build();
        final var eltern = DemoPerson.createEltern(
            ElternBuilder.eltern()
                .adresse(
                    AdresseBuilder.adresse()
                        .land(null)
                        .coAdresse(null)
                        .strasse(null)
                        .hausnummer(null)
                        .plz(null)
                        .ort(null)
                        .build()
                )
                .sozialversicherungsnummer(null)
                .elternTyp(null)
                .telefonnummer(null)
                .ausweisbFluechtling(null)
                .identischerZivilrechtlicherWohnsitz(false)
                .identischerZivilrechtlicherWohnsitzOrt(null)
                .identischerZivilrechtlicherWohnsitzPLZ(null)
                .sozialhilfebeitraege(false)
                .wohnkosten(null),
            AbstractPersonBuilder.abstractPerson()
                .nachname(null)
                .vorname(null)
                .geburtsdatum(null)
        );
        final var steuererklaerung = SteuererklaerungBuilder.steuererklaerung()
            .steuerdatenTyp(null)
            .steuererklaerungInBern(false)
            .ergaenzungsleistungen(null)
            .unterhaltsbeitraege(null)
            .renten(null)
            .einnahmenBGSA(null)
            .andereEinnahmen(null)
            .build();
        final var geschwister = DemoPerson.createGeschwister(
            GeschwisterBuilder.geschwister()
                .ausbildungssituation(null),
            AbstractFamilieEntityBuilder.abstractFamilieEntity()
                .wohnsitz(null)
                .wohnsitzAnteilMutter(null)
                .wohnsitzAnteilVater(null),
            AbstractPersonBuilder.abstractPerson()
                .nachname(null)
                .vorname(null)
                .geburtsdatum(null)
        );
        final var gesuchFormular = GesuchFormularBuilder.gesuchFormular()
            .personInAusbildung(pia)
            .familiensituation(familiensituation)
            .partner(partner)
            .einnahmenKosten(einnahmenKosten)
            .einnahmenKostenPartner(einnahmenKostenPartner)
            .lebenslaufItems(Set.of(lebenslauf))
            .geschwisters(Set.of(geschwister))
            .elterns(Set.of(eltern))
            .kinds(Set.of(kind))
            .tranche(null)
            .steuerdaten(null)
            .steuererklaerung(Set.of(steuererklaerung))
            .versteckteEltern(null)
            .build();
        final var gesuchTranche = GesuchTrancheBuilder.gesuchTranche()
            .gueltigkeit(null)
            .gesuchFormular(gesuchFormular)
            .gesuch(null)
            .status(null)
            .gesuchDokuments(null)
            .typ(null)
            .build();
        final var gesuch = GesuchBuilder.gesuch()
            .ausbildung(ausbildung)
            .gesuchsperiode(null)
            .gesuchStatus(null)
            .gesuchTranchen(List.of(gesuchTranche))
            .unterschriftenblaetter(null)
            .datenschutzbriefs(null)
            .beschwerdeVerlauf(null)
            .beschwerdeHaengig(false)
            .beschwerdeEntscheids(null)
            .verfuegt(false)
            .verfuegungs(null)
            .build();
    }
}
