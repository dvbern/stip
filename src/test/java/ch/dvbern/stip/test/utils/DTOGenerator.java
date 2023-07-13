package ch.dvbern.stip.test.utils;

import ch.dvbern.stip.generated.test.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DTOGenerator {

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForLebenslauf() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var lebenslaufItem = new LebenslaufItemUpdateDtoSpec();
        lebenslaufItem.setBeschreibung("Test");
        lebenslaufItem.setBis("02.2022");
        lebenslaufItem.setVon("01.2022");
        lebenslaufItem.setTaetigskeitsart(TaetigskeitsartDtoSpec.ERWERBSTAETIGKEIT);
        lebenslaufItem.setBildungsart(BildungsartDtoSpec.FACHHOCHSCHULEN);
        lebenslaufItem.setWohnsitz(WohnsitzKantonDtoSpec.BE);

        gesuchformularToWorkWith.getLebenslaufItems().add(lebenslaufItem);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForPersonInAusbildung() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var personInAusbildung = new PersonInAusbildungUpdateDtoSpec();
        var adresseDTO = prepareAdresseUpdate();
        personInAusbildung.setAdresse(adresseDTO);
        personInAusbildung.setAnrede(AnredeDtoSpec.HERR);
        personInAusbildung.setEmail("test@test.ch");
        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 10, 10));
        personInAusbildung.setNachname("Tester");
        personInAusbildung.setVorname("Prosper");
        personInAusbildung.setNationalitaet(LandDtoSpec.CH);
        personInAusbildung.setTelefonnummer("078 888 88 88");
        personInAusbildung.setDigitaleKommunikation(true);
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
        personInAusbildung.setKorrespondenzSprache(SpracheDtoSpec.DEUTSCH);
        personInAusbildung.setSozialhilfebeitraege(false);
        personInAusbildung.setZivilstand(ZivilstandDtoSpec.LEDIG);
        personInAusbildung.setSozialversicherungsnummer("756.0000.0000.05");
        personInAusbildung.setQuellenbesteuert(false);
        personInAusbildung.setWohnsitz(WohnsitzDtoSpec.ELTERN);

        gesuchformularToWorkWith.setPersonInAusbildung(personInAusbildung);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static AdresseDtoSpec prepareAdresseUpdate() {
        var adresseDTO = new AdresseDtoSpec();
        adresseDTO.setLand(LandDtoSpec.CH);
        adresseDTO.setPlz("3000");
        adresseDTO.setOrt("Bern");
        adresseDTO.setStrasse("Strasse");
        return adresseDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForAusbildung() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var ausbildung = new AusbildungUpdateDtoSpec();
        ausbildung.setAusbildungBegin("01.2022");
        ausbildung.setAusbildungEnd("02.2022");
        ausbildung.setAusbildungsland(AusbildungslandDtoSpec.SCHWEIZ);
        ausbildung.setAusbildungNichtGefunden(false);
        ausbildung.setPensum(AusbildungsPensumDtoSpec.VOLLZEIT);
        ausbildung.setAusbildungsgangId(UUID.fromString("3a8c2023-f29e-4466-a2d7-411a7d032f42"));
        ausbildung.setAusbildungsstaetteId(UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"));
        ausbildung.setFachrichtung("test");
        gesuchformularToWorkWith.setAusbildung(ausbildung);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForFamiliensituation() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var familiensituation = new FamiliensituationUpdateDtoSpec();
        familiensituation.setObhut(ElternschaftsteilungDtoSpec.GEMEINSAM);
        familiensituation.setObhutMutter(new BigDecimal(50));
        familiensituation.setObhutVater(new BigDecimal(50));
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setGerichtlicheAlimentenregelung(false);
        familiensituation.setMutterWiederverheiratet(false);
        familiensituation.setVaterWiederverheiratet(false);
        familiensituation.setSorgerecht(ElternschaftsteilungDtoSpec.GEMEINSAM);
        gesuchformularToWorkWith.setFamiliensituation(familiensituation);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForPartner() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var partner = new PartnerUpdateDtoSpec();
        partner.setAdresse(prepareAdresseUpdate());
        partner.setGeburtsdatum(LocalDate.of(2002, 12, 1));
        partner.setNachname("Testname");
        partner.setVorname("Testvorname");
        partner.setSozialversicherungsnummer("756.0000.0000.05");
        partner.setJahreseinkommen(new BigDecimal(100000));
        gesuchformularToWorkWith.setPartner(partner);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForAuszahlung() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var auszahlung = new AuszahlungUpdateDtoSpec();
        auszahlung.setAdresse(prepareAdresseUpdate());
        auszahlung.setNachname("Testname");
        auszahlung.setVorname("Testvorname");
        auszahlung.setIban("IBAN TODO");
        auszahlung.setKontoinhaber(KontoinhaberDtoSpec.GESUCHSTELLER);
        gesuchformularToWorkWith.setAuszahlung(auszahlung);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForGeschwister() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var geschwister = new GeschwisterUpdateDtoSpec();
        geschwister.setNachname("Geschwisternachname");
        geschwister.setVorname("Geschwistervorname");
        geschwister.setGeburtsdatum(LocalDate.of(2001, 1, 1));
        geschwister.setAusbildungssituation(AusbildungssituationDtoSpec.IN_AUSBILDUNG);
        geschwister.setWohnsitz(WohnsitzDtoSpec.ELTERN);
        geschwister.setWohnsitzAnteilMutter(new BigDecimal(40));
        geschwister.setWohnsitzAnteilVater(new BigDecimal(60));

        gesuchformularToWorkWith.getGeschwisters().add(geschwister);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    public static GesuchUpdateDtoSpec prepareGesuchUpdateForEltern() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var eltern = new ElternUpdateDtoSpec();
        eltern.setNachname("Elternnachname");
        eltern.setVorname("Elternvorname");
        eltern.setGeburtsdatum(LocalDate.of(2001, 1, 1));
        eltern.setTelefonnummer("");
        eltern.setAusweisbFluechtling(false);
        eltern.setAdresse(prepareAdresseUpdate());
        eltern.setElternTyp(ElternTypDtoSpec.VATER);
        eltern.setErgaenzungsleistungAusbezahlt(false);
        eltern.setIdentischerZivilrechtlicherWohnsitz(false);
        eltern.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
        eltern.setIdentischerZivilrechtlicherWohnsitzPLZ("1234");
        eltern.setSozialhilfebeitraegeAusbezahlt(false);
        eltern.setSozialversicherungsnummer("756.0000.0000.05");
        gesuchformularToWorkWith.getElterns().add(eltern);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }
}
