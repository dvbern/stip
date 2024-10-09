package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsstaetteCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsstaetteUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.AusbildungsstaetteApiSpec;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungsstaetteResourceTest {

	private final AusbildungsstaetteApiSpec api =
			AusbildungsstaetteApiSpec.ausbildungsstaette(RequestSpecUtil.quarkusSpec());

	private UUID ausbildungsstaetteId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createAusbildungsstaetteAsGesuchstellerForbidden() {
        api.createAusbildungsstaette()
                .body(AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec())
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(2)
    void createAusbildungsstaetteAsJurist() {
        var response = api.createAusbildungsstaette()
                .body(AusbildungsstaetteCreateDtoSpecModel.ausbildungsstaetteCreateDtoSpec())
                .execute(ResponseBody::prettyPeek)
                .then();

        response.assertThat()
                .statusCode(Status.OK.getStatusCode());

        ausbildungsstaetteId = extractFromBody(response).getId();
    }

	@Test
	@TestAsGesuchsteller
    @Order(3)
	void getAusbildungsstaetten() {
		var res = api.getAusbildungsstaetten().execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.OK.getStatusCode())
				.extract()
				.as(AusbildungsstaetteDtoSpec[].class);

		assertThat(res.length, greaterThanOrEqualTo(1));
	}

	@Test
	@TestAsGesuchsteller
    @Order(4)
	void getAusbildungsstaette() {
		api.getAusbildungsstaette().ausbildungsstaetteIdPath(TestConstants.TEST_AUSBILDUNGSSTAETTE_ID)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.OK.getStatusCode());
	}

    @Test
    @TestAsJurist
    @Order(5)
    void updateAusbildungsstaetteNotFound() {
        var ausbildungsstaette = AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpec();

        api.updateAusbildungsstaette().ausbildungsstaetteIdPath(UUID.randomUUID())
			.body(ausbildungsstaette)
			.execute(ResponseBody::prettyPeek)
			.then()
			.assertThat()
			.statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void updateAusbildungsstaetteAsGesuchstellerForbidden() {
        var ausbildungsstaette = AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpec();

        api.updateAusbildungsstaette().ausbildungsstaetteIdPath(ausbildungsstaetteId)
                .body(ausbildungsstaette)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(7)
    void updateAusbildungsstaette() {
        var ausbildungsstaette = AusbildungsstaetteUpdateDtoSpecModel.ausbildungsstaetteUpdateDtoSpec();

        var uniAarau = "Uni Aarau";
        ausbildungsstaette.setNameDe(uniAarau);
        ausbildungsstaette.setNameFr(uniAarau);

        api.updateAusbildungsstaette().ausbildungsstaetteIdPath(ausbildungsstaetteId)
                .body(ausbildungsstaette)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode());

        var updatedAusbildungsstaette = getAusbildungsstaetteFromApi(ausbildungsstaetteId);
        assertThat(updatedAusbildungsstaette.getNameDe(), is(uniAarau));
    	assertThat(updatedAusbildungsstaette.getNameFr(), is(uniAarau));
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void deleteAusbildungsstaetteAsGesuchstellerForbidden() {
        api.deleteAusbildungsstaette().ausbildungsstaetteIdPath(ausbildungsstaetteId)
    			.execute(ResponseBody::prettyPeek)
    			.then()
    			.assertThat()
    			.statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(9)
    void deleteAusbildungsstaetteNotFound() {
        api.deleteAusbildungsstaette().ausbildungsstaetteIdPath(UUID.randomUUID())
    			.execute(ResponseBody::prettyPeek)
    			.then()
    			.assertThat()
    			.statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(10)
    void deleteAusbildungsstaette() {
        api.deleteAusbildungsstaette().ausbildungsstaetteIdPath(ausbildungsstaetteId)
    			.execute(ResponseBody::prettyPeek)
    			.then()
    			.assertThat()
    			.statusCode(Status.NO_CONTENT.getStatusCode());

		api.getAusbildungsstaette().ausbildungsstaetteIdPath(ausbildungsstaetteId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.NOT_FOUND.getStatusCode());
    }

    private AusbildungsstaetteDtoSpec getAusbildungsstaetteFromApi(UUID id) {
        return api.getAusbildungsstaette().ausbildungsstaetteIdPath(id)
                .execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .as(AusbildungsstaetteDtoSpec.class);
    }

	private AusbildungsstaetteDto extractFromBody(ValidatableResponse response) {
		return response
				.extract()
				.body()
				.as(AusbildungsstaetteDto.class);
	}
}
