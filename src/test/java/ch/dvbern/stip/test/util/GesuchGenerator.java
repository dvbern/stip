package ch.dvbern.stip.test.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static ch.dvbern.stip.api.common.type.Anrede.HERR;
import static ch.dvbern.stip.api.common.type.Wohnsitz.FAMILIE;
import static ch.dvbern.stip.api.personinausbildung.type.Sprache.DEUTSCH;
import static ch.dvbern.stip.api.personinausbildung.type.Zivilstand.LEDIG;
import static ch.dvbern.stip.api.stammdaten.type.Land.CH;
import static ch.dvbern.stip.test.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;

public final class GesuchGenerator {

    private GesuchGenerator() {
    }

    public static Gesuch createGesuch() {
        GesuchFormular gesuchFormularToWorkWith = new GesuchFormular()
                .setPersonInAusbildung(createPersonInAusbildung())
                .setElterns(createElterns())
                .setFamiliensituation(createFamiliensituation())
                .setEinnahmenKosten(createEinnahmeKosten());

        return new Gesuch()
                .setGesuchFormularToWorkWith(gesuchFormularToWorkWith)
                .setFall(new Fall())
                .setGesuchsperiode(new Gesuchsperiode().setGueltigkeit(GUELTIGKEIT_PERIODE_23_24));
    }

    private static EinnahmenKosten createEinnahmeKosten() {
        return new EinnahmenKosten()
                .setNettoerwerbseinkommen(BigDecimal.valueOf(10000))
                .setFahrkosten(BigDecimal.valueOf(150))
                .setWohnkosten(BigDecimal.valueOf(1500))
                .setPersonenImHaushalt(BigDecimal.ONE)
                .setVerdienstRealisiert(false);
    }

    private static Familiensituation createFamiliensituation() {
        return new Familiensituation()
                .setElternVerheiratetZusammen(true)
                .setGerichtlicheAlimentenregelung(false);
    }

    private static Set<Eltern> createElterns() {
        Set<Eltern> elterns = new HashSet<>();
        elterns.add(createElten(ElternTyp.MUTTER));
        elterns.add(createElten(ElternTyp.VATER));
        return elterns;
    }

    private static Eltern createElten(ElternTyp elternTyp) {
        var eltern =  new Eltern()
                .setElternTyp(elternTyp)
                .setAdresse(createAdresse())
                .setSozialversicherungsnummer(getAHVNummerForElternTyp(elternTyp))
                .setTelefonnummer("0791111111")
                .setSozialhilfebeitraegeAusbezahlt(false)
                .setAusweisbFluechtling(false)
                .setErgaenzungsleistungAusbezahlt(false);
        eltern.setId(UUID.randomUUID());
        eltern.setNachname("nachname");
        eltern.setVorname("vorname");
        eltern.setGeburtsdatum(LocalDate.of(1980,1,1));
        return eltern;
    }

    private static String getAHVNummerForElternTyp(ElternTyp elternTyp) {
        return elternTyp == ElternTyp.MUTTER ? TestConstants.AHV_NUMMER_VALID_MUTTER : TestConstants.AHV_NUMMER_VALID_VATTER;
    }

    public static Partner createPartner() {
        Partner partner =  new Partner()
                .setFahrkosten(BigDecimal.valueOf(1000))
                .setAdresse(createAdresse())
                .setJahreseinkommen(BigDecimal.valueOf(50000))
                .setVerpflegungskosten(BigDecimal.valueOf(700))
                .setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_PARTNER);
        partner.setGeburtsdatum(LocalDate.of(1999,1,1));
        partner.setNachname("Nachname");
        partner.setVorname("vorname");
        return partner;
    }
    private static PersonInAusbildung createPersonInAusbildung() {
        final PersonInAusbildung personInAusbildung = new PersonInAusbildung()
                .setZivilstand(LEDIG)
                .setAdresse(createAdresse())
                .setEmail("test@test.ch")
                .setAnrede(HERR)
                .setNationalitaet(CH)
                .setTelefonnummer("078 888 88 88")
                .setDigitaleKommunikation(true)
                .setIdentischerZivilrechtlicherWohnsitz(true)
                .setKorrespondenzSprache(DEUTSCH)
                .setSozialhilfebeitraege(false)
                .setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG)
                .setQuellenbesteuert(false)
                .setHeimatort("Bern");

        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 10, 10));
        personInAusbildung.setNachname("Tester");
        personInAusbildung.setVorname("Prosper");
        personInAusbildung.setWohnsitz(FAMILIE);

        return personInAusbildung;
    }

    private static Adresse createAdresse() {
        return new Adresse()
            .setLand(CH)
            .setPlz("3000")
            .setOrt("Bern")
            .setStrasse("Strasse");
    }
}
