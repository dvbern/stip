/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.benutzer.resource;

import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.benutzer.SachbearbeiterZuordnungStammdatenDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_2_TEST_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_TEST_AHV_NUMMER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
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
        final var benutzerDto = api.prepareCurrentBenutzer()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(BenutzerDtoSpec.class);

        me = benutzerDto;

        assertThat(benutzerDto.getVorname()).isEqualTo("Frédéric");
        assertThat(benutzerDto.getNachname()).isEqualTo("Nell");
        assertThat(benutzerDto.getSozialversicherungsnummer()).isEqualTo(GESUCHSTELLER_TEST_AHV_NUMMER);
    }

    @Test
    @TestAsGesuchsteller2
    @Order(2)
    void test_get_me2() {
        final var benutzerDto = api.prepareCurrentBenutzer()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(BenutzerDtoSpec.class);

        assertThat(benutzerDto.getVorname()).isEqualTo("Hans");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller 2");
        assertThat(benutzerDto.getSozialversicherungsnummer()).isEqualTo(GESUCHSTELLER_2_TEST_AHV_NUMMER);
    }

    @Test
    @Order(4)
    @TestAsSachbearbeiter
    void findSachbearbeitende() {
        var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(BenutzerDtoSpec[].class);
        sachbearbeiterUUID = sachbearbeiterListe[0].getId();
    }

    @Test
    @Order(5)
    @TestAsAdmin
    void createSachbearbeiterZuordnungStammdaten() {
        final var updateDto = SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenDtoSpec();
        api.createOrUpdateSachbearbeiterStammdaten()
            .benutzerIdPath(sachbearbeiterUUID)
            .body(updateDto)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(BenutzerDtoSpec[].class);
        MatcherAssert.assertThat(sachbearbeiterListe.length, is(greaterThan(0)));
        MatcherAssert.assertThat(
            Arrays.stream(sachbearbeiterListe).allMatch(x -> x.getSachbearbeiterZuordnungStammdaten() != null),
            is(true)
        );
    }

    @Test
    @Order(6)
    @TestAsAdmin
    void createSachbearbeiterZuordnungStammdatenList() throws InterruptedException {
        // This is bad, but currently the only way to avoid a 500 response
        // As the server may still be (background) processing the request from the
        // Previous test.
        Thread.sleep(5000);
        final var updateDtos =
            SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenListDtoSpecs(2);
        updateDtos.get(0).setSachbearbeiter(me.getId());
        updateDtos.get(1).setSachbearbeiter(sachbearbeiterUUID);

        api.createOrUpdateSachbearbeiterStammdatenList()
            .body(updateDtos)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        final var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(BenutzerDtoSpec[].class);

        MatcherAssert.assertThat(sachbearbeiterListe.length, is(greaterThan(0)));
        MatcherAssert.assertThat(
            Arrays.stream(sachbearbeiterListe).allMatch(x -> x.getSachbearbeiterZuordnungStammdaten() != null),
            is(true)
        );
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
