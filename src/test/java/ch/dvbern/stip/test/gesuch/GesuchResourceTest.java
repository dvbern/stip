package ch.dvbern.stip.test.gesuch;

import ch.dvbern.oss.stip.contract.test.api.GesuchApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchCreateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ValidationReportDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestConstants;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE;
import static ch.dvbern.stip.test.util.DTOGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceTest {

	public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

	private UUID gesuchId;

	private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";

	@Test
	@TestAsGesuchsteller
	@Order(1)
	void testCreateEndpoint() {
		var gesuchDTO = new GesuchCreateDtoSpec();
		gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
		gesuchDTO.setGesuchsperiodeId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

		gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.CREATED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(2)
	void testFindGesuchEndpoint() {
		var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(GesuchDtoSpec[].class);

		gesuchId = gesuche[0].getId();

		assertThat(gesuche.length, is(1));
		assertThat(gesuche[0].getFall().getId(), is(UUID.fromString(TestConstants.FALL_TEST_ID)));
		assertThat(gesuche[0].getGesuchsperiode().getId(),
				is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
	}

	@Test
	@TestAsGesuchsteller
	@Order(3)
	void testUpdateGesuchEndpointPersonInAusbildung() {
		var gesuchUpdatDTO = prepareGesuchUpdateForPersonInAusbildung();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(4)
	void testUpdateGesuchEndpointAusbildung() {
		var gesuchUpdatDTO = prepareGesuchUpdateForAusbildung();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(5)
	void testUpdateGesuchEndpointFamiliensituation() {
		var gesuchUpdatDTO = prepareGesuchUpdateForFamiliensituation();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(6)
	void testUpdateGesuchEndpointPartner() {
		var gesuchUpdatDTO = prepareGesuchUpdateForPartner();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(7)
	void testUpdateGesuchEndpointAuszahlung() {
		var gesuchUpdatDTO = prepareGesuchUpdateForAuszahlung();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(8)
	void testUpdateGesuchEndpointAddGeschwister() {
		var gesuchUpdatDTO = prepareGesuchUpdateForGeschwister();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(9)
	void testUpdateGesuchEndpointUpdateGeschwister() {
		var gesuch =
				gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
				.body()
				.as(GesuchDtoSpec.class);
		var gesuchUpdatDTO = prepareGesuchUpdateForGeschwister();
		gesuchUpdatDTO.getGesuchFormularToWorkWith()
				.getGeschwisters()
				.get(0)
				.setId(gesuch.getGesuchFormularToWorkWith().getGeschwisters().get(0).getId());
		gesuchUpdatDTO.getGesuchFormularToWorkWith().getGeschwisters().get(0).setNachname(geschwisterNameUpdateTest);
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(10)
	void testUpdateGesuchAddLebenslaufEndpoint() {
		var gesuchUpdatDTO = prepareGesuchUpdateForLebenslaufBildungsart();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(11)
	void testUpdateGesuchAddElternEndpoint() {
		var gesuchUpdatDTO = prepareGesuchUpdateForEltern();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(12)
	void testUpdateGesuchEndpointAddKind() {
		var gesuchUpdatDTO = prepareGesuchUpdateForKind();
		gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.ACCEPTED.getStatusCode());
	}

	@Test
	@TestAsGesuchsteller
	@Order(13)
	void testAllFormularPresent() {
		var gesuch =
				gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek).then().extract()
				.body()
				.as(GesuchDtoSpec.class);
		assertThat(gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung(), is(notNullValue()));
		assertThat(gesuch.getGesuchFormularToWorkWith().getAusbildung(), is(notNullValue()));
		assertThat(gesuch.getGesuchFormularToWorkWith().getFamiliensituation(), is(notNullValue()));
		assertThat(gesuch.getGesuchFormularToWorkWith().getPartner(), is(notNullValue()));
		assertThat(gesuch.getGesuchFormularToWorkWith().getAuszahlung(), is(notNullValue()));
		assertThat(gesuch.getGesuchFormularToWorkWith().getGeschwisters().size(), is(1));
		assertThat(
				gesuch.getGesuchFormularToWorkWith().getGeschwisters().get(0).getNachname(),
				is(geschwisterNameUpdateTest));
		assertThat(gesuch.getGesuchFormularToWorkWith().getLebenslaufItems().size(), is(1));
		assertThat(gesuch.getGesuchFormularToWorkWith().getElterns().size(), is(1));
		assertThat(gesuch.getGesuchFormularToWorkWith().getKinds().size(), is(1));

	}

	@Test
	@TestAsGesuchsteller
	@Order(14)
	void testGetGesucheForBenutzende() {
		var gesuche = gesuchApiSpec.getGesucheForBenutzer()
				.benutzerIdPath(TestConstants.GESUCHSTELLER_TEST_ID)
				.execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(GesuchDtoSpec[].class);
		assertThat(gesuche.length, is(1));
	}

	@Test
	@TestAsGesuchsteller
	@Order(15)
	void test_get_gesuch_for_fall() {
		var gesuche = gesuchApiSpec.getGesucheForFall()
				.fallIdPath(TestConstants.FALL_TEST_ID)
				.execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(GesuchDtoSpec[].class);
		assertThat(gesuche.length, is(1));
	}

	@Test
	@TestAsGesuchsteller
	@Order(16)
	void testGesuchEinreichenValidationError(){
		var validationReport = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.extract()
				.body()
				.as(ValidationReportDtoSpec.class);
		assertThat(validationReport.getValidationErrors().stream()
				.anyMatch(validationError -> validationError.getMessageTemplate()
						.equals(VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE)), is(true));
		assertThat(validationReport.getValidationErrors().stream()
				.anyMatch(validationError -> validationError.getMessageTemplate()
						.equals(VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE)), is(true));
	}

	@Test
    @TestAsGesuchsteller
	@Order(17)
	void testDeleteGesuch() {
		gesuchApiSpec.deleteGesuch()
				.gesuchIdPath(gesuchId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}
}
