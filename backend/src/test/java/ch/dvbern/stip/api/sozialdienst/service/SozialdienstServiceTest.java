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

package ch.dvbern.stip.api.sozialdienst.service;

import java.util.List;
import java.util.Optional;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SozialdienstServiceTest {

    private Sozialdienst sozialdienstA;
    private Sozialdienst sozialdienstB;
    private SozialdienstBenutzer benutzerOfSozialdienstA;
    private SozialdienstBenutzer benutzerOfSozialdienstB;

    private SozialdienstBenutzer sozialdienstAdminOfSozialdienstA;
    private SozialdienstBenutzer sozialdienstAdminOfSozialdienstB;

    @Inject
    private SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    @Inject
    private SozialdienstRepository sozialdienstRepository;

    @Inject
    private SozialdienstService sozialdienstService;
    @InjectMock
    private SozialdienstBenutzerService sozialdienstBenutzerService;

    @Transactional
    @BeforeEach
    void setUp() {

        benutzerOfSozialdienstA = new SozialdienstBenutzer();
        benutzerOfSozialdienstA.setVorname("Vorname");
        benutzerOfSozialdienstA.setNachname("Nachname");
        benutzerOfSozialdienstA.setBenutzereinstellungen(new Benutzereinstellungen());
        benutzerOfSozialdienstA.setBenutzerStatus(BenutzerStatus.AKTIV);

        benutzerOfSozialdienstB = new SozialdienstBenutzer();
        benutzerOfSozialdienstB.setVorname("Vorname");
        benutzerOfSozialdienstB.setNachname("Nachname");
        benutzerOfSozialdienstB.setBenutzereinstellungen(new Benutzereinstellungen());
        benutzerOfSozialdienstB.setBenutzerStatus(BenutzerStatus.AKTIV);

        sozialdienstAdminOfSozialdienstA = new SozialdienstBenutzer();
        sozialdienstAdminOfSozialdienstA.setVorname("Vorname");
        sozialdienstAdminOfSozialdienstA.setNachname("Nachname");
        sozialdienstAdminOfSozialdienstA.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdminOfSozialdienstA.setBenutzerStatus(BenutzerStatus.AKTIV);

        sozialdienstAdminOfSozialdienstB = new SozialdienstBenutzer();
        sozialdienstAdminOfSozialdienstB.setVorname("Vorname");
        sozialdienstAdminOfSozialdienstB.setNachname("Nachname");
        sozialdienstAdminOfSozialdienstB.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdminOfSozialdienstB.setBenutzerStatus(BenutzerStatus.AKTIV);

        Adresse adresse1 = new Adresse();
        adresse1.setPlz("3000");
        adresse1.setLand(Land.CH);
        adresse1.setHausnummer("1");
        adresse1.setStrasse("Musterstrasse");
        adresse1.setOrt("Ort");

        Adresse adresse2 = new Adresse();
        adresse2.setPlz("3000");
        adresse2.setLand(Land.CH);
        adresse2.setHausnummer("1");
        adresse2.setStrasse("Musterstrasse");
        adresse2.setOrt("Ort");

        sozialdienstA = new Sozialdienst();
        sozialdienstA.setName("Sozialdienst");
        sozialdienstA.setAdresse(adresse1);
        sozialdienstA.setSozialdienstBenutzers(List.of(benutzerOfSozialdienstA));
        sozialdienstA.setSozialdienstAdmin(sozialdienstAdminOfSozialdienstA);
        sozialdienstA.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        sozialdienstRepository.persistAndFlush(sozialdienstA);

        sozialdienstB = new Sozialdienst();
        sozialdienstB.setName("Sozialdienst");
        sozialdienstB.setAdresse(adresse2);
        sozialdienstB.setSozialdienstBenutzers(List.of(benutzerOfSozialdienstB));
        sozialdienstB.setSozialdienstAdmin(sozialdienstAdminOfSozialdienstB);
        sozialdienstB.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        sozialdienstRepository.persistAndFlush(sozialdienstB);

        sozialdienstBenutzerRepository.persistAndFlush(benutzerOfSozialdienstA);
        sozialdienstBenutzerRepository.persistAndFlush(benutzerOfSozialdienstB);
        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstAdminOfSozialdienstA);
        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstAdminOfSozialdienstB);
    }

    @Test
    void getSozialdienstOfCurrentSozialdienstBenutzer_ShouldReturn_SozialdienstA_ForSozialdienstMitarbeiterOfSozialdienstA() {
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(benutzerOfSozialdienstA));
        assertThat(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer(), is(sozialdienstA));
    }

    @Test
    void getSozialdienstOfCurrentSozialdienstBenutzer_ShouldReturn_SozialdienstA_ForSozialdienstAdminOfSozialdienstA() {
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(sozialdienstAdminOfSozialdienstA));
        assertThat(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer(), is(sozialdienstA));
    }

    @Test
    void getSozialdienstOfCurrentSozialdienstBenutzer_ShouldReturn_SozialdienstB_ForSozialdienstMitarbeiterOfSozialdienstB() {
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(benutzerOfSozialdienstB));
        assertThat(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer(), is(sozialdienstB));
    }

    @Test
    void getSozialdienstOfCurrentSozialdienstBenutzer_ShouldReturn_SozialdienstB_ForSozialdienstAdminOfSozialdienstB() {
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(sozialdienstAdminOfSozialdienstB));
        assertThat(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer(), is(sozialdienstB));
    }

    @Test
    void isCurrentBenutzerMitarbeiterOfSozialdienst_shouldReturnTrue_whenCurrentBenutzerIsOfSameSozialdienst(){
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(benutzerOfSozialdienstA));
        assertThat(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(sozialdienstA.getId()), is(true));
        assertThat(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(sozialdienstB.getId()), is(false));

        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer()).thenReturn(Optional.ofNullable(sozialdienstAdminOfSozialdienstA));
        assertThat(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(sozialdienstA.getId()), is(true));
        assertThat(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(sozialdienstB.getId()), is(false));

    }

}
