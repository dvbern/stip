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
        ausbildung.setAusbildungstaetteId(UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"));
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
}
