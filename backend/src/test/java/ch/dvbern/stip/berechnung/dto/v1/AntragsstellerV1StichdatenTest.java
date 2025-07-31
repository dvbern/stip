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

package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AntragsstellerV1StichdatenTest {
    AntragsstellerV1 antragsstellerV1;
    Gesuch gesuch;
    GesuchFormular gesuchFormular;

    @BeforeEach
    void setUp() {
        gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());
        gesuch.getGesuchsperiode().setLimiteAlterAntragsstellerHalbierungElternbeitrag(25);
        gesuchFormular = gesuch.getGesuchTranchen().get(0).getGesuchFormular();
    }

    @Test
    void stichdatum_pia_alter_halbierungElternbeitragTest() {
        var ausbildung = gesuch.getAusbildung();
        var endOfAusbildungsjahr = gesuch.getLatestGesuchTranche().getGueltigkeit().getGueltigBis();

        // pia has at least 1 abgeschlossene Ausbildung in Lebenslauf
        var lebenslaufitem = new LebenslaufItem();
        lebenslaufitem.setAbschluss(new Abschluss().setZusatzfrage(AbschlussZusatzfrage.BERUFSBEZEICHNUNG).setAskForBerufsmaturitaet(true).setBerufsbefaehigenderAbschluss(true));
        lebenslaufitem.setBis(ausbildung.getAusbildungBegin().minusMonths(1));
        lebenslaufitem.setVon(ausbildung.getAusbildungBegin().minusYears(1));
        lebenslaufitem.setAusbildungAbgeschlossen(true);
        gesuchFormular.getLebenslaufItems().add(lebenslaufitem);

        // pia < 25 years old at end of current ausbildungsjahr
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(endOfAusbildungsjahr.minusYears(24));

        // assert
        antragsstellerV1 =
            AntragsstellerV1.buildFromDependants(gesuchFormular, 0);
        assertFalse(antragsstellerV1.isHalbierungElternbeitrag());

        // pia = 25 years old at end of current ausbildungsjahr
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(endOfAusbildungsjahr.minusYears(25));

        // act & assert
        antragsstellerV1 =
            AntragsstellerV1.buildFromDependants(gesuchFormular, 0);
        assertTrue(antragsstellerV1.isHalbierungElternbeitrag());
    }
}
