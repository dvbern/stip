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

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller3;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiterAdmin;
import ch.dvbern.stip.api.generator.api.model.benutzer.SachbearbeiterZuordnungStammdatenDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.dto.BenutzerDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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
        final var benutzerDto = api.prepareCurrentBenutzer()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(BenutzerDtoSpec.class);

        me = benutzerDto;

        assertThat(benutzerDto.getVorname()).isEqualTo("");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller");
    }

    @Test
    @TestAsGesuchsteller2
    @Order(2)
    void test_get_me2() {
        final var benutzerDto = api.prepareCurrentBenutzer()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(BenutzerDtoSpec.class);

        assertThat(benutzerDto.getVorname()).isEqualTo("");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller");
    }

    @Test
    @TestAsGesuchsteller3
    @Order(3)
    void test_get_me3() {
        final var benutzerDto = api.prepareCurrentBenutzer()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(BenutzerDtoSpec.class);

        assertThat(benutzerDto.getVorname()).isEqualTo("");
        assertThat(benutzerDto.getNachname()).isEqualTo("Gesuchsteller");
    }

    @Test
    @Order(4)
    @TestAsSachbearbeiter
    void findSachbearbeitende() {
        var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
    @TestAsSachbearbeiterAdmin
    void createSachbearbeiterZuordnungStammdaten() {
        final var updateDto = SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenDtoSpec();
        api.createOrUpdateSachbearbeiterStammdaten()
            .benutzerIdPath(sachbearbeiterUUID)
            .body(updateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
    @TestAsSachbearbeiterAdmin
    void createSachbearbeiterZuordnungStammdatenList() throws InterruptedException {
        // This is bad, but currently the only way to avoid a 500 response
        // As the server may still be (background) processing the request from the
        // Previous test.
        Thread.sleep(5000);
        final var updateDtos =
            SachbearbeiterZuordnungStammdatenDtoSpecModel.sachbearbeiterZuordnungStammdatenListDtoSpecs(1);
        updateDtos.get(0).setSachbearbeiter(sachbearbeiterUUID);

        api.createOrUpdateSachbearbeiterStammdatenList()
            .body(updateDtos)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        final var sachbearbeiterListe = api.getSachbearbeitende()
            .execute(TestUtil.PEEK_IF_ENV_SET)
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
            .benutzerIdPath(sachbearbeiterUUID)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(SachbearbeiterZuordnungStammdatenDtoSpec.class);

        MatcherAssert.assertThat(myZuordnung, notNullValue());
    }
}
