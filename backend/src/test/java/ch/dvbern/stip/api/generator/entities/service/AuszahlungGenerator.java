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

package ch.dvbern.stip.api.generator.entities.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;

// todo: remove
public final class AuszahlungGenerator {
    public static Auszahlung initAuszahlung() {
        Auszahlung auszahlung = new Auszahlung();
        // auszahlung.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        // auszahlung.setVorname("Brigitta1111111");
        // auszahlung.setNachname("Flückke11111111");
        // auszahlung.setIban("CH2089144635452242312");
        // Adresse adresse = new Adresse();
        // adresse.setStrasse("Bundesstadtstrasse");
        // adresse.setOrt("Hauptstadtort");
        // adresse.setPlz("9299");
        // adresse.setLand(LandGenerator.initSwitzerland());
        // adresse.setHausnummer("9298");
        // auszahlung.setAdresse(adresse);
        // auszahlung.setSapBusinessPartnerId(1427);
        return auszahlung;
    }
}
