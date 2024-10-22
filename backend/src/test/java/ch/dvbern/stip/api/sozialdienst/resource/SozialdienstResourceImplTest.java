package ch.dvbern.stip.api.sozialdienst.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.util.*;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
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

    @Order(1)
    @Test
    @TestAsGesuchsteller
    void createSozialdienstShouldFail() {
        var adresseDto = new AdresseDtoSpec();
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setPlz("12345");
        adresseDto.setOrt("Musterort");
        adresseDto.setHausnummer("1");
        adresseDto.setLand(LandDtoSpec.CH);

        final var createDto = new SozialdienstCreateDtoSpec()
            .adresse(adresseDto)
            .name("Muster Sozialdienst")
            .iban("CH5089144653587876648");
        apiSpec.createSozialdienst()
            .body(createDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Order(2)
    @Test
    @TestAsSachbearbeiter
    void createSozialdienst() {
        var adresseDto = new AdresseDtoSpec();
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setPlz("12345");
        adresseDto.setOrt("Musterort");
        adresseDto.setHausnummer("1");
        adresseDto.setLand(LandDtoSpec.CH);

        final var createDto = new SozialdienstCreateDtoSpec()
            .adresse(adresseDto)
            .name("Muster Sozialdienst")
            .iban("CH5089144653587876648");

        dtoSpec = apiSpec.createSozialdienst()
            .body(createDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(SozialdienstDtoSpec.class);
    }

    @Order(3)
    @TestAsGesuchsteller
    @Test
    void getSozialdiensteShouldFail(){
        apiSpec.getAllSozialdienste()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Order(4)
    @TestAsSachbearbeiter
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

    @Order(5)
    @TestAsGesuchsteller
    @Test
    void getSozialdienstByIdShouldFail(){
        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Order(5)
    @TestAsSachbearbeiter
    @Test
    void getSozialdienstById(){
        apiSpec.getSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Order(6)
    @TestAsGesuchsteller
    @Test
    void updateSozialdienstShouldFail() {
        var updateDto = new SozialdienstUpdateDtoSpec();
        updateDto.setId(dtoSpec.getId());
        updateDto.setAdresse(dtoSpec.getAdresse());
        updateDto.setName(dtoSpec.getName());
        updateDto.setIban(dtoSpec.getIban());

        final var newStreetname = "updated street";
        final var newName = "updated sozialdienst";
        updateDto.getAdresse().setStrasse(newStreetname);
        updateDto.setName(newName);

        apiSpec.updateSozialdienst()
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Order(7)
    @TestAsSachbearbeiter
    @Test
    void updateSozialdienst() {
        var updateDto = new SozialdienstUpdateDtoSpec();
        updateDto.setId(dtoSpec.getId());
        updateDto.setAdresse(dtoSpec.getAdresse());
        updateDto.setName(dtoSpec.getName());
        updateDto.setIban(dtoSpec.getIban());

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

    @Order(8)
    @TestAsGesuchsteller
    @Test
    void deleteSozialdienstShouldFail(){
        apiSpec.deleteSozialdienst()
            .sozialdienstIdPath(dtoSpec.getId())
            .execute(ResponseBody::prettyPeek).then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Order(9)
    @TestAsSachbearbeiter
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
