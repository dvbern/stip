package ch.dvbern.stip.api.generator.entities.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.stammdaten.type.Land;

public final class AuszahlungGenerator {
    public static Auszahlung initAuszahlung(){
        Auszahlung auszahlung = new Auszahlung();
        auszahlung.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlung.setVorname("Brigitta1111111");
        auszahlung.setNachname("Flückke11111111");
        auszahlung.setIban("CH2089144635452242312");
        Adresse adresse = new Adresse();
        adresse.setStrasse("Bundesstadtstrasse");
        adresse.setOrt("Hauptstadtort");
        adresse.setPlz("9299");
        adresse.setLand(Land.CH);
        adresse.setHausnummer("9298");
        auszahlung.setAdresse(adresse);
        auszahlung.setSapBusinessPartnerId(1427);
        return auszahlung;
    }
}
