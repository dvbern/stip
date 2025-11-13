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

package ch.dvbern.stip.api.notiz.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.notiz.type.GesuchNotizTyp;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchNotizResourceImplTest {
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
    private GesuchNotizDto juristischeAbklaerungNotizDto;
    private GesuchNotizDto gesuchNotizDto;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
    }

    // create a notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void notizErstellen() {
        // TODO KSTIP-2005: Create a second Gesuch
        // create notiz for gesuch1
        var gesuchNotizCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchNotizCreateDto.setGesuchId(gesuch.getId());
        gesuchNotizCreateDto.setText("test");
        gesuchNotizCreateDto.setBetreff("test");
        gesuchNotizCreateDto.setNotizTyp(GesuchNotizTypDtoSpec.GESUCH_NOTIZ);
        gesuchNotizDto = gesuchNotizApiSpec.createNotiz()
            .body(gesuchNotizCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);
    }

    // get all notizen as SB
    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void getAll() {
        final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDtoSpec[].class);
        // the response should contain all notizen of all gesuche related to the current fall
        assertEquals(1, notizen.length);
        final var notiz = Arrays.stream(notizen).toList().get(0);
        assertEquals(GesuchNotizTypDtoSpec.GESUCH_NOTIZ, notiz.getNotizTyp());

        gesuchNotizApiSpec.getNotiz()
            .notizIdPath(notiz.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDtoSpec.class);
    }

    // update notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void update() {
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
        gesuchCreateDto.setNotizTyp(GesuchNotizTypDtoSpec.GESUCH_NOTIZ);
        final var notiz = gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);

        var gesuchUpdateDto = new GesuchNotizUpdateDtoSpec();
        gesuchUpdateDto.setId(notiz.getId());
        gesuchUpdateDto.setText("update");
        gesuchUpdateDto.setBetreff("update");
        gesuchNotizApiSpec.updateNotiz()
            .body(gesuchUpdateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        gesuchNotizApiSpec.deleteNotiz()
            .notizIdPath(gesuchUpdateDto.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    // create a notiz as SB
    // delete gesuch as SB, check if all notizen are deleted too
    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void createAdditionalNotiz() {
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
        gesuchCreateDto.setNotizTyp(GesuchNotizTypDtoSpec.GESUCH_NOTIZ);
        gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void setToInBearbeitung() {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @Order(8)
    void delete() {
        // delete gesuch
        gesuchApiSpec.deleteGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void checkNoNotizRemaining() {
        gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    // Test Resources for Juristische Notiz
    // create a notiz as SB
    @Test
    @TestAsGesuchsteller
    @Order(10)
    void neuesGesuchErstellenUndEinreichen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(11)
    void neuesGesuchBearbeiten() {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void juristischeNotizErstellen() {
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test juristische notiz");
        gesuchCreateDto.setBetreff("test juristische notiz");
        gesuchCreateDto.setNotizTyp(GesuchNotizTypDtoSpec.JURISTISCHE_NOTIZ);
        gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(GesuchNotizDto.class);

        // Gesuchstatus should be set to JURISTISCHE_ABKLAERUNG after the creation
        final var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getGesuchStatus(), is(GesuchstatusDtoSpec.JURISTISCHE_ABKLAERUNG));
    }

    // get all notizen as SB
    @Test
    @TestAsJurist
    @Order(13)
    void getJuristischeNotizen() {
        final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto[].class);
        // several notizes possible, since the endpoint returns all notizen of a fall
        assertTrue(notizen.length > 0);
        final var createdJuristischeNotizen = Arrays.stream(notizen)
            .filter(notiz -> notiz.getNotizTyp().equals(GesuchNotizTyp.JURISTISCHE_NOTIZ))
            .toList();
        assertTrue(createdJuristischeNotizen.size() > 0);
        juristischeAbklaerungNotizDto = createdJuristischeNotizen.get(0);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void updateJuristischeNotizAsSBShouldFail() {
        var update = new GesuchNotizUpdateDtoSpec();
        update.setId(juristischeAbklaerungNotizDto.getId());
        update.setText("update");
        update.setBetreff("update");

        gesuchNotizApiSpec.updateNotiz()
            .body(update)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(15)
    void updateJuristischeNotizAsJuristShouldFail() {
        var update = new GesuchNotizUpdateDtoSpec();
        update.setId(juristischeAbklaerungNotizDto.getId());
        update.setBetreff("update");
        update.setText("update");

        gesuchNotizApiSpec.updateNotiz()
            .body(update)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(16)
    void answerJuristischeNotiz() {
        var antwort = new JuristischeAbklaerungNotizAntwortDtoSpec();
        antwort.setAntwort("Test antwort");

        final var abklaerungNotizDto = gesuchNotizApiSpec.answerJuristischeAbklaerungNotiz()
            .notizIdPath(juristischeAbklaerungNotizDto.getId())
            .body(antwort)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);
        gesuchNotizApiSpec.answerJuristischeAbklaerungNotiz()
            .notizIdPath(juristischeAbklaerungNotizDto.getId())
            .body(antwort)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        assertEquals(abklaerungNotizDto.getAntwort(), antwort.getAntwort());

        // Gesuchstatus should be set to BEREIT_FUER_BEARBEITUNG after the answer
        final var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getGesuchStatus(), is(GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG));
    }

    // get all notizen as SB
    @Test
    @TestAsSachbearbeiter
    @Order(17)
    void getJuristischeNotizenWithAnswer() {
        final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto[].class);
        juristischeAbklaerungNotizDto = Arrays.stream(notizen).toList().get(notizen.length - 1);
        assertEquals(GesuchNotizTyp.JURISTISCHE_NOTIZ, juristischeAbklaerungNotizDto.getNotizTyp());
        assertNotNull(juristischeAbklaerungNotizDto.getAntwort());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(18)
    void anotherNotizErstellen() {
        var gesuchNotizCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchNotizCreateDto.setGesuchId(gesuch.getId());
        gesuchNotizCreateDto.setText("test");
        gesuchNotizCreateDto.setBetreff("test");
        gesuchNotizCreateDto.setNotizTyp(GesuchNotizTypDtoSpec.GESUCH_NOTIZ);
        gesuchNotizDto = gesuchNotizApiSpec.createNotiz()
            .body(gesuchNotizCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);
    }

    /**
     * Only juristische notizen are allowed to be answered
     */
    @Test
    @TestAsJurist
    @Order(19)
    void answerJuristischeNotizShouldfail() {
        var antwort = new JuristischeAbklaerungNotizAntwortDtoSpec();
        antwort.setAntwort("Test antwort");

        gesuchNotizApiSpec.answerJuristischeAbklaerungNotiz()
            .notizIdPath(gesuchNotizDto.getId())
            .body(antwort)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    /**
     * Note: the possibility to delete a juristische notiz
     * is not forseen yet or not part of KSTIP-1130
     */
    @Test
    @TestAsSachbearbeiter
    @Order(20)
    void juristischeNotizDeleteShouldFail() {
        gesuchNotizApiSpec.deleteNotiz()
            .notizIdPath(juristischeAbklaerungNotizDto.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(21)
    void juristischeNotizDeleteAsJuristShouldFail() {
        gesuchNotizApiSpec.deleteNotiz()
            .notizIdPath(juristischeAbklaerungNotizDto.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(99)
    @TestAsSuperUser
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
