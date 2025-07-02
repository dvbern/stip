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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.auszahlung.service.ZahlungsverbindungService;
import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsSozialdienstAdmin;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SozialdienstBenutzerServiceTest {
    @Inject
    SozialdienstService sozialdienstService;

    @Inject
    SozialdienstRepository sozialdienstRepository;

    @Inject
    LandService landService;

    @Inject
    SozialdienstBenutzerService sozialdienstBenutzerService;

    @InjectMock
    ZahlungsverbindungService zahlungsverbindungServiceMock;

    SozialdienstDto sozialdienstDto;
    Zahlungsverbindung zahlungsverbindung;

    private static final String VALID_IBAN_1 = "CH5089144653587876648";

    @Order(1)
    @TestAsSozialdienstAdmin
    @Test
    void init() {

        zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setIban(VALID_IBAN_1);
        zahlungsverbindung.setNachname("Test");
        zahlungsverbindung.setVorname("Test");
        var adresse = new Adresse();
        adresse.setLand(landService.requireLandById(TestConstants.TEST_LAND_SCHWEIZ_ID));
        adresse.setStrasse("Musterstrasse");
        adresse.setHausnummer("1");
        adresse.setPlz("3000");
        adresse.setOrt("Bern");
        zahlungsverbindung.setAdresse(adresse);

        zahlungsverbindungServiceMock = Mockito.mock(ZahlungsverbindungService.class);
        when(zahlungsverbindungServiceMock.createZahlungsverbindung(Mockito.any())).thenReturn(zahlungsverbindung);
    }

    @Order(2)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void createSozialdienst() {
        var sozialdienstCreateDto = new SozialdienstCreateDto();

        sozialdienstCreateDto.setName("a");
        // sozialdienstCreateDto.setIban(VALID_IBAN_1);
        var sdAdresse = new AdresseDto();
        sdAdresse.setStrasse("Musterstrasse");
        sdAdresse.setPlz("12345");
        sdAdresse.setOrt("Musterort");
        sdAdresse.setHausnummer("1");
        sdAdresse.setLandId(TestConstants.TEST_LAND_SCHWEIZ_ID);

        var zahlungsverbindungDto = new ZahlungsverbindungDto();
        zahlungsverbindungDto.setVorname("Test");
        zahlungsverbindungDto.setNachname("Test");
        zahlungsverbindungDto.setAdresse(sdAdresse);
        zahlungsverbindungDto.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        sozialdienstCreateDto.setZahlungsverbindung(zahlungsverbindungDto);

        var sozialdienstAdminDto = new SozialdienstAdminDto();
        sozialdienstAdminDto.setVorname("a");
        sozialdienstAdminDto.setNachname("b");
        sozialdienstAdminDto.setEmail("a@b.ch");
        sozialdienstCreateDto.setSozialdienstAdmin(sozialdienstAdminDto);

        sozialdienstDto = sozialdienstService.createSozialdienst(sozialdienstCreateDto);

        assertThat(sozialdienstDto.getId(), notNullValue());
    }

    @Order(3)
    @Transactional
    @TestAsAdmin
    @Test
    void replaceSozialdienstAdmin() {
        SozialdienstAdminDto sozialdienstAdminDto = new SozialdienstAdminDto();
        sozialdienstAdminDto.setVorname("c");
        sozialdienstAdminDto.setNachname("d");
        sozialdienstAdminDto.setEmail("c@d.ch");
        sozialdienstService.replaceSozialdienstAdmin(sozialdienstDto.getId(), sozialdienstAdminDto);

        sozialdienstDto = sozialdienstService.getSozialdienstById(sozialdienstDto.getId());
        assertThat(sozialdienstDto.getSozialdienstAdmin().getVorname(), equalTo("c"));
        assertThat(sozialdienstDto.getSozialdienstAdmin().getNachname(), equalTo("d"));
        assertThat(sozialdienstDto.getSozialdienstAdmin().getEmail(), equalTo("c@d.ch"));
    }

    @Order(4)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void getSozialdienstBenutzerEmptyTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(1));
    }

    @Order(5)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void createSozialdienstBenutzerTest() {
        SozialdienstBenutzerCreateDto createDto = new SozialdienstBenutzerCreateDto();
        var name = "replaced";
        var email = "fabrice.jakob@dvbern.ch";

        createDto.setVorname(name);
        createDto.setNachname(name);
        createDto.setEmail(email);
        createDto.setRedirectUri("");

        var sozialdienstbenutzer = sozialdienstBenutzerService
            .createSozialdienstMitarbeiterBenutzer(
                sozialdienstRepository.requireById(sozialdienstDto.getId()),
                createDto
            );

        assertThat(sozialdienstbenutzer.getVorname(), equalTo(name));
        assertThat(sozialdienstbenutzer.getNachname(), equalTo(name));
        assertThat(sozialdienstbenutzer.getEmail(), equalTo(email));
    }

    @Order(6)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void getSozialdienstBenutzerTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(2));
    }

    @Order(7)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void updateSozialdienstBenutzerTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        SozialdienstBenutzerUpdateDto updateDto = new SozialdienstBenutzerUpdateDto();
        var newname = "replaced2";

        updateDto.setId(sozialdienstbenutzers.get(0).getId());
        updateDto.setVorname(newname);
        updateDto.setNachname(newname);

        final var sozialdienstbenutzer = sozialdienstBenutzerService.updateSozialdienstBenutzer(updateDto);

        assertThat(sozialdienstbenutzer.getVorname(), equalTo(newname));
        assertThat(sozialdienstbenutzer.getNachname(), equalTo(newname));
    }

    @Order(8)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void deleteSozialdienstBenutzerTest() {
        var sozialdienstbenutzers = sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        sozialdienstBenutzerService.deleteSozialdienstBenutzer(sozialdienstbenutzers.get(0).getId());

        sozialdienstbenutzers = sozialdienstBenutzerService
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(1));
    }
}
