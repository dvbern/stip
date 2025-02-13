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

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstAdminCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstCreateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.SozialdienstApiSpec;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
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

    private static final String VALID_IBAN_2 = "CH5089144653587876648";

    @Order(1)
    @Test
    @TestAsAdmin
    void createSozialdienst() {
        final var adresse = AdresseSpecModel.adresseDtoSpec();
        final var admin = SozialdienstAdminCreateDtoSpecModel.sozialdienstAdminCreateDtoSpec();
        final var sozialdienst = SozialdienstCreateDtoSpecModel.sozialdienstCreateDtoSpec(admin, adresse);

        dtoSpec = TestUtil.executeAndExtract(
            SozialdienstDtoSpec.class,
            apiSpec.createSozialdienst().body(sozialdienst)
        );

        assertThat(dtoSpec.getSozialdienstAdmin(), notNullValue());
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
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);
        assertThat(dtoSpec.getSozialdienstAdmin(), notNullValue());
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
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(SozialdienstDtoSpec[].class)
        ).filter(sozialdienst -> sozialdienst.getId().equals(dtoSpec.getId())).findFirst().get();
        assertThat(dtoSpec.getSozialdienstAdmin(), notNullValue());
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
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        final var updated = apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);
        assertThat(updated.getSozialdienstAdmin(), notNullValue());
        assertTrue(updated.getName().contains("updated"));
        assertTrue(updated.getAdresse().getStrasse().contains("updated"));
        checkSozialdienstResponse(updated);
        checkSozialdienstAdminResponse(updated.getSozialdienstAdmin());
    }

    // @Order(5)
    // @TestAsAdmin
    // @Test
    // void replaceSozialdienstAdminTest() {
    // final var createSozialdienstDto = new SozialdienstAdminDtoSpec();
    // createSozialdienstDto.setVorname("replaced");
    // createSozialdienstDto.setNachname("replaced");
    // createSozialdienstDto.setEmail("replaced@test.com");
    // createSozialdienstDto.setKeycloakId(SOZIALDIENST_ADMIN_ID);
    // final var replaced = apiSpec.replaceSozialdienstAdmin()
    // .sozialdienstIdPath(dtoSpec.getId())
    // .body(createSozialdienstDto)
    // .execute(TestUtil.PEEK_IF_ENV_SET)
    // .then()
    // .assertThat()
    // .statusCode(Response.Status.OK.getStatusCode())
    // .extract()
    // .as(SozialdienstBenutzerDtoSpec.class);
    // assertTrue(replaced.getNachname().contains("replaced"));
    // assertTrue(replaced.getVorname().contains("replaced"));
    // assertTrue(replaced.getEmail().contains("replaced"));
    // checkSozialdienstAdminResponse(replaced);
    // }

    @Order(99)
    @TestAsAdmin
    @Test
    void deleteSozialdienst() {
        apiSpec.deleteSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
