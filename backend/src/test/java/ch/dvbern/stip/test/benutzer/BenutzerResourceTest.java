package ch.dvbern.stip.test.benutzer;

import ch.dvbern.stip.generated.test.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.test.dto.BenutzerDtoSpec;
import ch.dvbern.stip.generated.test.dto.BenutzerUpdateDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.test.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.MatcherAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static ch.dvbern.stip.test.generator.api.model.benutzer.BenutzerUpdateDtoSpecModel.benutzerUpdateDtoSpecModel;
import static ch.dvbern.stip.test.generator.api.model.benutzer.SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenDtoSpecModel;
import static ch.dvbern.stip.test.util.TestConstants.AHV_NUMMER_VALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.instancio.Select.field;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BenutzerResourceTest {

    private final BenutzerApiSpec api = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());

    private UUID sachbearbeiterUUID;

    private BenutzerDtoSpec me;

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

        me = benutzerDto;

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

        final var updateDto = Instancio.of(benutzerUpdateDtoSpecModel).create();
        updateDto.setBenutzereinstellungen(me.getBenutzereinstellungen());

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

    @Test
    @Order(4)
    @TestAsSachbearbeiter
    void createAndFindSachbearbeitenden() {
        String nachname = UUID.randomUUID().toString();
        final var updateDto = Instancio.of(benutzerUpdateDtoSpecModel)
            .set(field(BenutzerUpdateDtoSpec::getNachname), nachname)
            .set(field(BenutzerUpdateDtoSpec::getBenutzereinstellungen), me.getBenutzereinstellungen())
            .create();
        api.updateCurrentBenutzer().body(updateDto).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.ACCEPTED.getStatusCode());
        var sachbearbeiterListe = api.getSachbearbeitende().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(BenutzerDtoSpec[].class);
        sachbearbeiterUUID = sachbearbeiterListe[0].getId();
        assertThat(sachbearbeiterListe).extracting(BenutzerDtoSpec::getNachname).contains(nachname);
    }

    @Test
    @Order(5)
    @TestAsSachbearbeiter
    void createSachbearbeiterZuordnungStammdaten() {
        final var updateDto = Instancio.of(sachbearbeiterZuordnungStammdatenDtoSpecModel).create();
        api.createOrUpdateSachbearbeiterStammdaten().benutzerIdPath(sachbearbeiterUUID).body(updateDto).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Status.ACCEPTED.getStatusCode());
        var sachbearbeiterListe = api.getSachbearbeitende().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(BenutzerDtoSpec[].class);
        MatcherAssert.assertThat(sachbearbeiterListe.length, is(1));
        MatcherAssert.assertThat(sachbearbeiterListe[0].getSachbearbeiterZuordnungStammdaten(), notNullValue());
    }
}
