package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsgangCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungsgangUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.AusbildungsgangApiSpec;
import ch.dvbern.stip.generated.api.AusbildungsstaetteApiSpec;
import ch.dvbern.stip.generated.dto.*;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungsgangResourceTest {
	private final AusbildungsgangApiSpec ausbildungsgangApi =
			AusbildungsgangApiSpec.ausbildungsgang(RequestSpecUtil.quarkusSpec());
	private final AusbildungsstaetteApiSpec ausbildungsstaetteApiSpec =
			AusbildungsstaetteApiSpec.ausbildungsstaette(RequestSpecUtil.quarkusSpec());
	private UUID ausbildungsgangId;

	private UUID ausbildungsstaetteId;

	@Test
	@TestAsGesuchsteller
	@Order(1)
	void createAusbildungsgangAsGesuchstellerForbidden() {
		ausbildungsgangApi.createAusbildungsgang()
				.body(AusbildungsgangCreateDtoSpecModel.ausbildungsgangCreateDtoSpec)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.FORBIDDEN.getStatusCode());
	}

	@Test
	@TestAsSachbearbeiter
	@Order(2)
	void createAusbildungsgangAsSachbearbeiter() {
		var response = ausbildungsgangApi.createAusbildungsgang()
				.body(AusbildungsgangCreateDtoSpecModel.ausbildungsgangCreateDtoSpec)
				.execute(ResponseBody::prettyPeek)
				.then();

		response.assertThat()
				.statusCode(Status.OK.getStatusCode());

		ausbildungsgangId = extractFromBody(response).getId();
	}

	@Test
	@TestAsSachbearbeiter
	@Order(3)
	void getAusbildungsgang() {
		var ausbildunggang = getAusbildungsgangeFromAPI(ausbildungsgangId);
		ausbildungsstaetteId = ausbildunggang.getAusbildungsstaetteId();

		assertThat(ausbildunggang.getId(), is(ausbildungsgangId));
		assertThat(ausbildunggang.getAusbildungsstaetteId(), notNullValue());
	}

	@Test
	@TestAsSachbearbeiter
	@Order(4)
	void createNewAusbildungsgangWithExistingAusbildungsstaette() {
		var ausbildungsstaettes = getAusbildungsstaettenFromApi();
		var ausbildungsgang = AusbildungsgangCreateDtoSpecModel.ausbildungsgangCreateDtoSpec;

		ausbildungsgang.setAusbildungsstaetteId(ausbildungsstaettes[0].getId());

		ausbildungsgangApi.createAusbildungsgang()
				.body(ausbildungsgang)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.OK.getStatusCode());

		assertThat(getAusbildungsstaettenFromApi().length, is(ausbildungsstaettes.length));
	}

	@Test
	@TestAsSachbearbeiter
	@Order(5)
	void updateAusbildungsgangNotFound() {
		var ausbildunggang = AusbildungsgangUpdateDtoSpecModel.ausbildungsgangUpdateDtoSpec;
		ausbildunggang.setAusbildungsstaetteId(ausbildungsstaetteId);

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
		var ausbildunggang = AusbildungsgangUpdateDtoSpecModel.ausbildungsgangUpdateDtoSpec;

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

		var ausbildunggang = AusbildungsgangUpdateDtoSpecModel.ausbildungsgangUpdateDtoSpec;
		final var aarau = "AARAU";
		ausbildunggang.setAusbildungsort(aarau);
		ausbildunggang.setAusbildungsstaetteId(ausbildungsstaettes[0].getId());

		ausbildungsgangApi.updateAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
				.body(ausbildunggang)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.OK.getStatusCode());

		var updatedAussibldungsgang = getAusbildungsgangeFromAPI(ausbildungsgangId);
		var updatedAusbildungsstaette = getAusbildungsstaetteFromApi(ausbildungsstaettes[0].getId());

		assertThat(updatedAussibldungsgang.getAusbildungsort(), is(aarau));
		assertThat(getAusbildungsstaettenFromApi().length, is(ausbildungsstaettes.length));
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
	@Order(9)
	void deleteAusbildungsgangNotFound() {
		ausbildungsgangApi.deleteAusbildungsgang().ausbildungsgangIdPath(UUID.randomUUID())
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.NOT_FOUND.getStatusCode());
	}

	@Test
	@TestAsSachbearbeiter
	@Order(10)
	void deleteAusbildungsgang() {
		var numAusbildungsstaettenBevoreDelete = getAusbildungsstaettenFromApi().length;

		ausbildungsgangApi.deleteAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.NO_CONTENT.getStatusCode());

		ausbildungsgangApi.getAusbildungsgang().ausbildungsgangIdPath(ausbildungsgangId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.NOT_FOUND.getStatusCode());
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

	private AusbildungsgangDto extractFromBody(ValidatableResponse response) {
		return response
				.extract()
				.body()
				.as(AusbildungsgangDto.class);
	}

}
