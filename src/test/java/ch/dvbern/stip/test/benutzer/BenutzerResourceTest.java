package ch.dvbern.stip.test.benutzer;

import ch.dvbern.oss.stip.contract.test.api.BenutzerApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.BenutzerDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.BenutzerUpdateDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.test.util.TestConstants.AHV_NUMMER_VALID;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BenutzerResourceTest {

    private final BenutzerApiSpec api = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_get_me() {
        final var benutzerDto = api.getCurrentBenutzer()
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BenutzerDtoSpec.class);

        assertThat(benutzerDto.getVorname()).isEqualTo("Hans");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller");
        assertThat(benutzerDto.getSozialversicherungsnummer()).isEqualTo(AHV_NUMMER_VALID);
    }

    @Test
    @TestAsGesuchsteller2
    @Order(2)
    void test_get_me2() {
        final var benutzerDto = api.getCurrentBenutzer()
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BenutzerDtoSpec.class);

        assertThat(benutzerDto.getVorname()).isEqualTo("Hans");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller 2");
        assertThat(benutzerDto.getSozialversicherungsnummer()).isEqualTo(AHV_NUMMER_VALID);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void test_update_me() {

        final var updateDto = new BenutzerUpdateDtoSpec();
        updateDto.setVorname("Fritz");
        updateDto.setNachname("Tester");
        updateDto.setSozialversicherungsnummer("756.1234.5678.97");

        api.updateCurrentBenutzer()
                .body(updateDto)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());


        final var updatedBenutzer = api.getCurrentBenutzer()
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BenutzerDtoSpec.class);

        assertThat(updatedBenutzer.getVorname()).isEqualTo(updateDto.getVorname());
        assertThat(updatedBenutzer.getNachname()).isEqualTo(updateDto.getNachname());
        assertThat(updatedBenutzer.getSozialversicherungsnummer()).isEqualTo(updateDto.getSozialversicherungsnummer());
    }
}
