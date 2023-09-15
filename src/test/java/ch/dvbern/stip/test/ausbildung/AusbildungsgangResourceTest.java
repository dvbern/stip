package ch.dvbern.stip.test.ausbildung;

import ch.dvbern.oss.stip.contract.test.api.AusbildungsgangApiSpec;
import ch.dvbern.oss.stip.contract.test.api.AusbildungsstaetteApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.AusbildungsgangDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.AusbildungsortDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.AusbildungsstaetteDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import ch.dvbern.stip.test.util.TestUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static ch.dvbern.stip.test.util.DTOGenerator.prepareAusbildungsgangUpdate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungsgangResourceTest {

    private final AusbildungsgangApiSpec ausbildungsgangApi = AusbildungsgangApiSpec.ausbildungsgang(RequestSpecUtil.quarkusSpec());
    private final AusbildungsstaetteApiSpec ausbildungsstaetteApiSpec = AusbildungsstaetteApiSpec.ausbildungsstaette(RequestSpecUtil.quarkusSpec());
    private UUID ausbildungsgangId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createAusbildungsgangAsGesuchstellerForbidden() {
        ausbildungsgangApi.createAusbildungsgang().body(prepareAusbildungsgangUpdate()).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void createAusbildungsgangAsSachbearbeiter() {
        var response = ausbildungsgangApi.createAusbildungsgang().body(prepareAusbildungsgangUpdate()).execute(ResponseBody::prettyPeek)
                .then();

        response.assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());

        ausbildungsgangId = TestUtil.extractIdFromResponse(response);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void getAusbildungsgang() {
        var ausbildunggang = getAusbildungsgangeFromAPI(ausbildungsgangId);

        assertThat(ausbildunggang.getId(), is(ausbildungsgangId));
        assertThat(ausbildunggang.getAusbildungsstaetteId(), notNullValue());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void createNewAusbildungsgangWithExistingAusbildungsstaette() {
        var ausbildungsstaettes = getAusbildungsstaettenFromApi();
        var ausbildungsgang = prepareAusbildungsgangUpdate();

        ausbildungsgang.getAusbildungsstaette().setId(ausbildungsstaettes[0].getId());

        ausbildungsgangApi.createAusbildungsgang()
                .body(ausbildungsgang)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());

        assertThat(getAusbildungsstaettenFromApi().length, is(ausbildungsstaettes.length));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void updateAusbildungsgangNotFound() {
        var ausbildunggang = prepareAusbildungsgangUpdate();

        ausbildungsgangApi.updateAusbildungsgang().ausbildungsgangIdPath(UUID.randomUUID())
                .body(ausbildunggang)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void updateAusbildungsgangAsGesuchstellerForbidden() {
        var ausbildunggang = prepareAusbildungsgangUpdate();

        ausbildungsgangApi.updateAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
                .body(ausbildunggang)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void updateAusbildungsgang() {
        var ausbildungsstaettes = getAusbildungsstaettenFromApi();

        var ausbildunggang = prepareAusbildungsgangUpdate();
        var uniArrau = "Uni Aarau";
        ausbildunggang.setAusbildungsort(AusbildungsortDtoSpec.AARAU);
        ausbildunggang.getAusbildungsstaette().setNameDe(uniArrau);
        ausbildunggang.getAusbildungsstaette().setNameFr(uniArrau);
        ausbildunggang.getAusbildungsstaette().setId(ausbildungsstaettes[0].getId());

        ausbildungsgangApi.updateAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
                .body(ausbildunggang)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());

        var updatedAussibldungsgang = getAusbildungsgangeFromAPI(ausbildungsgangId);
        var updatedAusbildungsstaette = getAusbildungsstaetteFromApi(ausbildungsstaettes[0].getId());

        assertThat(updatedAussibldungsgang.getAusbildungsort(), is(AusbildungsortDtoSpec.AARAU));
        assertThat(getAusbildungsstaettenFromApi().length, is(ausbildungsstaettes.length));
        assertThat(updatedAusbildungsstaette.getNameDe(), is(uniArrau));
        assertThat(updatedAusbildungsstaette.getNameFr(), is(uniArrau));
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void deleteAusbildungsgangAsGesuchstellerForbidden() {
        ausbildungsgangApi.deleteAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void deleteAusbildungsgangNotFound() {
        ausbildungsgangApi.deleteAusbildungsgang().ausbildungsgangIdPath(UUID.randomUUID())
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void deleteAusbildungsgang() {
        var numAusbildungsstaettenBevoreDelete = getAusbildungsstaettenFromApi().length;

        ausbildungsgangApi.deleteAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        assertThat(getAusbildungsstaettenFromApi().length, is(numAusbildungsstaettenBevoreDelete));
    }

    private AusbildungsstaetteDtoSpec[] getAusbildungsstaettenFromApi() {
        return ausbildungsstaetteApiSpec.getAusbildungsstaetten()
                .execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .as(AusbildungsstaetteDtoSpec[].class);
    }

    private AusbildungsstaetteDtoSpec getAusbildungsstaetteFromApi(UUID id) {
        return ausbildungsstaetteApiSpec.getAusbildungsstaette().ausbildungsstaetteIdPath(id)
                .execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .as(AusbildungsstaetteDtoSpec.class);
    }

    private AusbildungsgangDtoSpec getAusbildungsgangeFromAPI(UUID id) {
        return ausbildungsgangApi.getAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .as(AusbildungsgangDtoSpec.class);
    }

}
