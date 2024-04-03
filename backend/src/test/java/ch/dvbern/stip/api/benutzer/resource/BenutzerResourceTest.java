package ch.dvbern.stip.api.benutzer.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.benutzer.BenutzerUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.benutzer.SachbearbeiterZuordnungStammdatenDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.dto.BenutzerDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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

        final var updateDto = BenutzerUpdateDtoSpecModel.benutzerUpdateDtoSpec;
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
        final var updateDto = BenutzerUpdateDtoSpecModel.benutzerUpdateDtoSpec;
        updateDto.setNachname(nachname);
        updateDto.setBenutzereinstellungen(me.getBenutzereinstellungen());

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
        final var updateDto = SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenDtoSpec;
        api.createOrUpdateSachbearbeiterStammdaten()
            .benutzerIdPath(sachbearbeiterUUID)
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
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

    @Test
    @Order(6)
    @TestAsSachbearbeiter
    void createSachbearbeiterZuordnungStammdatenList() {
        final var updateDtos = SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenListDtoSpecs(2);
        updateDtos.get(0).setSachbearbeiter(me.getId());
        updateDtos.get(1).setSachbearbeiter(sachbearbeiterUUID);

        api.createOrUpdateSachbearbeiterStammdatenList()
            .body(updateDtos)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        final var sachbearbeiterListe = api.getSachbearbeitende().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(BenutzerDtoSpec[].class);

        MatcherAssert.assertThat(sachbearbeiterListe.length, is(1));
        MatcherAssert.assertThat(sachbearbeiterListe[0].getSachbearbeiterZuordnungStammdaten(), notNullValue());
        final var myZuordnung = api.getSachbearbeiterStammdaten()
            .benutzerIdPath(me.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(SachbearbeiterZuordnungStammdatenDtoSpec.class);

        MatcherAssert.assertThat(myZuordnung, notNullValue());
    }
}
