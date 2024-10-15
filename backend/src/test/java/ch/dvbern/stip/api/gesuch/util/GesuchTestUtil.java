package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public class GesuchTestUtil {
    public Gesuch setupValidGesuch(){
        Gesuch gesuch = GesuchGenerator.initGesuch();
        gesuch.setId(UUID.randomUUID());
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        gesuch.getNewestGesuchTranche().get().setGesuchFormular(new GesuchFormular());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setId(UUID.randomUUID());
        gesuch.setGesuchNummer(UUID.randomUUID().toString());
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setPersonInAusbildung(setupValidPersonInAusbildung());
        gesuch.getNewestGesuchTranche().get().setTyp(GesuchTrancheTyp.TRANCHE);

        return gesuch;
    }

    public Gesuch setupValidGesuchInState(Gesuchstatus status){
        Gesuch gesuch = setupValidGesuch();
        return gesuch.setGesuchStatus(status);
    }

    public PersonInAusbildung setupValidPersonInAusbildung() {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setSozialversicherungsnummer("756.1119.8398.42");
        personInAusbildung.setVorname("A");
        personInAusbildung.setNachname("B");
        Adresse adresse = new Adresse();
        adresse.setStrasse("a");
        adresse.setHausnummer("1");
        adresse.setId(UUID.randomUUID());
        adresse.setPlz("3333");
        adresse.setLand(Land.CH);
        adresse.setOrt("b");
        personInAusbildung.setHeimatort("B");
        personInAusbildung.setAdresse(adresse);
        personInAusbildung.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        personInAusbildung.setTelefonnummer(UUID.randomUUID().toString());
        personInAusbildung.setGeburtsdatum(LocalDate.now());
        personInAusbildung.setKorrespondenzSprache(Sprache.DEUTSCH);
        personInAusbildung.setAnrede(Anrede.FRAU);
        personInAusbildung.setEmail("test@test.com");
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);

        return personInAusbildung;
    }
}
