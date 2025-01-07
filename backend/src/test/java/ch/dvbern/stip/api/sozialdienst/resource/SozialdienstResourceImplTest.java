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

package ch.dvbern.stip.api.sozialdienst.resource;

import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.SozialdienstApiSpec;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SozialdienstResourceImplTest {
    private static final String ADMIN_EMAIL = "test@test.com";
    public final SozialdienstApiSpec apiSpec = SozialdienstApiSpec.sozialdienst(RequestSpecUtil.quarkusSpec());
    public SozialdienstDtoSpec dtoSpec;
    public SozialdienstDto dto;

    private static final String VALID_IBAN_1 = "CH5089144653587876648";
    private static final String VALID_IBAN_2 = "CH5089144653587876648";

    @Order(1)
    @Test
    @TestAsAdmin
    void createSozialdienst() {
        var adresseDto = new AdresseDtoSpec();
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setPlz("12345");
        adresseDto.setOrt("Musterort");
        adresseDto.setHausnummer("1");
        adresseDto.setLand(LandDtoSpec.CH);

        final var sozialdienstAdminCreateDto = new SozialdienstAdminDtoSpec();
        sozialdienstAdminCreateDto.setKeycloakId(UUID.randomUUID().toString());
        sozialdienstAdminCreateDto.setNachname("Muster");
        sozialdienstAdminCreateDto.setVorname("Max");
        sozialdienstAdminCreateDto.setEmail(ADMIN_EMAIL);

        final var createDto = new SozialdienstCreateDtoSpec()
            .adresse(adresseDto)
            .name("Muster Sozialdienst")
            .iban(VALID_IBAN_1)
            .sozialdienstAdmin(sozialdienstAdminCreateDto);

        dtoSpec = apiSpec.createSozialdienst()
            .body(createDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);

        assertNotNull(dtoSpec.getSozialdienstAdmin());
        assertTrue(dtoSpec.getSozialdienstAdmin().getEmail().contains(ADMIN_EMAIL));
        checkSozialdienstResponse(dtoSpec);
        checkSozialdienstAdminResponse(dtoSpec.getSozialdienstAdmin());
    }

    @Order(2)
    @TestAsAdmin
    @Test
    void getSozialdienstById() {
        dtoSpec = apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);
        assertNotNull(dtoSpec.getSozialdienstAdmin());
        assertTrue(dtoSpec.getSozialdienstAdmin().getEmail().contains(ADMIN_EMAIL));
        checkSozialdienstResponse(dtoSpec);
        checkSozialdienstAdminResponse(dtoSpec.getSozialdienstAdmin());

    }

    @Order(3)
    @TestAsAdmin
    @Test
    void getSozialdienste() {
        dtoSpec = Arrays.stream(
            apiSpec.getAllSozialdienste()
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(SozialdienstDtoSpec[].class)
        ).toList().get(0);
        assertNotNull(dtoSpec.getSozialdienstAdmin());
        assertTrue(dtoSpec.getSozialdienstAdmin().getEmail().contains(ADMIN_EMAIL));
        checkSozialdienstResponse(dtoSpec);
        checkSozialdienstAdminResponse(dtoSpec.getSozialdienstAdmin());
    }

    @Order(4)
    @TestAsAdmin
    @Test
    void updateSozialdienst() {
        var updateDto = new SozialdienstUpdateDtoSpec();
        updateDto.setId(dtoSpec.getId());
        updateDto.setAdresse(dtoSpec.getAdresse());
        updateDto.setName(dtoSpec.getName());
        updateDto.setIban(VALID_IBAN_2);

        final var newStreetname = "updated street";
        final var newName = "updated sozialdienst";
        updateDto.getAdresse().setStrasse(newStreetname);
        updateDto.setName(newName);

        apiSpec.updateSozialdienst()
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        final var updated = apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);
        assertNotNull(updated.getSozialdienstAdmin());
        assertTrue(updated.getName().contains("updated"));
        assertTrue(updated.getAdresse().getStrasse().contains("updated"));
        checkSozialdienstResponse(updated);
        checkSozialdienstAdminResponse(updated.getSozialdienstAdmin());
    }

    @Order(5)
    @TestAsAdmin
    @Test
    void updateSozizialdienstAdminTest() {
        final var updateSozialdienstDto = new SozialdienstAdminUpdateDtoSpec();
        updateSozialdienstDto.setVorname("updated");
        updateSozialdienstDto.setNachname("updated");
        final var updated = apiSpec.updateSozialdienstAdmin()
            .sozialdienstIdPath(dtoSpec.getId())
            .body(updateSozialdienstDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstBenutzerDtoSpec.class);
        assertTrue(updated.getNachname().contains("updated"));
        assertTrue(updated.getVorname().contains("updated"));
        checkSozialdienstAdminResponse(updated);
    }

    @Order(5)
    @TestAsAdmin
    @Test
    void replaceSozizialdienstAdminTest() {
        final var keykloakId = UUID.randomUUID().toString();
        final var createSozialdienstDto = new SozialdienstAdminDtoSpec();
        createSozialdienstDto.setVorname("replaced");
        createSozialdienstDto.setNachname("replaced");
        createSozialdienstDto.setEmail("replaced@test.com");
        createSozialdienstDto.setKeycloakId(keykloakId);
        final var replaced = apiSpec.replaceSozialdienstAdmin()
            .sozialdienstIdPath(dtoSpec.getId())
            .body(createSozialdienstDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstBenutzerDtoSpec.class);;
        assertTrue(replaced.getNachname().contains("replaced"));
        assertTrue(replaced.getVorname().contains("replaced"));
        assertTrue(replaced.getEmail().contains("replaced"));
        // assertEquals(replaced.getKeycloakId(), keykloakId);
        checkSozialdienstAdminResponse(replaced);

    }

    @Order(7)
    @TestAsAdmin
    @Test
    void deleteSozialdienst() {
        apiSpec.deleteSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    private void checkSozialdienstResponse(SozialdienstDtoSpec dtoSpec) {
        assertNotNull(dtoSpec.getId());
        assertNotNull(dtoSpec.getAdresse());
        assertNotNull(dtoSpec.getName());
        assertNotNull(dtoSpec.getIban());
    }

    private void checkSozialdienstAdminResponse(SozialdienstBenutzerDtoSpec dtoSpec) {
        assertNotNull(dtoSpec.getVorname());
        assertNotNull(dtoSpec.getNachname());
        assertNotNull(dtoSpec.getEmail());
    }
}
