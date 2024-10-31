package ch.dvbern.stip.api.sozialdienst.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.util.*;
import ch.dvbern.stip.generated.api.SozialdienstApiSpec;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

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
    public final SozialdienstApiSpec apiSpec = SozialdienstApiSpec.sozialdienst(RequestSpecUtil.quarkusSpec());
    public SozialdienstDtoSpec dtoSpec;
    public SozialdienstDto dto;

    private final static String VALID_IBAN_1 = "CH5089144653587876648";
    private final static String VALID_IBAN_2 = "CH5089144653587876648";

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

        final var sozialdienstAdminCreateDto = new SozialdienstAdminCreateDtoSpec();
        sozialdienstAdminCreateDto.setKeycloakId(UUID.randomUUID().toString());
        sozialdienstAdminCreateDto.setNachname("Muster");
        sozialdienstAdminCreateDto.setVorname("Max");
        sozialdienstAdminCreateDto.seteMail("test@test.com");

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
    }

    @Order(2)
    @TestAsAdmin
    @Test
    void getSozialdienste(){
        dtoSpec = Arrays.stream(apiSpec.getAllSozialdienste()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec[].class)).toList().get(0);
    }

    @Order(3)
    @TestAsAdmin
    @Test
    void getSozialdienstById(){
        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
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
        assertTrue(updated.getName().contains("updated"));
        assertTrue(updated.getAdresse().getStrasse().contains("updated"));
    }

    @Order(5)
    @TestAsAdmin
    @Test
    void updateSozizialdienstAdminTest(){
        final var updateSozialdienstDto = new SozialdienstAdminUpdateDtoSpec();
        updateSozialdienstDto.setVorname("updated");
        updateSozialdienstDto.setNachname("updated");
        updateSozialdienstDto.seteMail("test@test.com");
        final var updated = apiSpec.updateSozialdienstAdmin()
            .sozialdienstIdPath(dtoSpec.getId())
            .body(updateSozialdienstDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstAdminDtoSpec.class);
        assertTrue(updated.getNachname().contains("updated"));
        assertTrue(updated.getVorname().contains("updated"));
    }

    @Order(5)
    @TestAsAdmin
    @Test
    void replaceSozizialdienstAdminTest(){
        final var keykloakId = UUID.randomUUID().toString();
        final var createSozialdienstDto = new SozialdienstAdminCreateDtoSpec();
        createSozialdienstDto.setVorname("replaced");
        createSozialdienstDto.setNachname("replaced");
        createSozialdienstDto.seteMail("test@test.com");
        createSozialdienstDto.setKeycloakId(keykloakId);
        final var replaced = apiSpec.replaceSozialdienstAdmin()
            .sozialdienstIdPath(dtoSpec.getId())
            .body(createSozialdienstDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstAdminDtoSpec.class);;
        assertTrue(replaced.getNachname().contains("replaced"));
        assertTrue(replaced.getVorname().contains("replaced"));
        assertTrue(replaced.getKeycloakId().equals(keykloakId));
    }

    @Order(7)
    @TestAsAdmin
    @Test
    void deleteSozialdienst(){
        apiSpec.deleteSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek).then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
