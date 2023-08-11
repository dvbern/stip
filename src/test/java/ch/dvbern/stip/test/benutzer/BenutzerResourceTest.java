package ch.dvbern.stip.test.benutzer;

import ch.dvbern.oss.stip.contract.test.api.BenutzerApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.BenutzerDtoSpec;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestConstants;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BenutzerResourceTest {

	public final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());

	@Test
	@Order(1)
	void testFindBenutzendeEndpoint() {
		var benutzende = benutzerApiSpec.getBenutzende().execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(BenutzerDtoSpec[].class);

		assertThat(benutzende.length, is(2));
		assertThat(benutzende[0].getId(), is(UUID.fromString(TestConstants.GESUCHSTELLER_TEST_ID)));

	}

	@Test
	@Order(2)
	void testGetBenutzer() {
		var benutzer = benutzerApiSpec.getBenutzer()
				.benutzerIdPath(TestConstants.GESUCHSTELLER_TEST_ID)
				.execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(BenutzerDtoSpec.class);
		assertThat(benutzer.getId(), is(UUID.fromString(TestConstants.GESUCHSTELLER_TEST_ID)));
	}

	@Test
	@Order(3)
	void testGetBenutzerNotFound() {
		benutzerApiSpec.getBenutzer()
				.benutzerIdPath(UUID.randomUUID())
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.NOT_FOUND.getStatusCode());
	}
}
