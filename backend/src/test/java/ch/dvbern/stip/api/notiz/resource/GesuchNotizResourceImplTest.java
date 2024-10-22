package ch.dvbern.stip.api.notiz.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.notiz.service.GesuchNotizMapper;
import ch.dvbern.stip.api.util.*;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchNotizResourceImplTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());

    private GesuchNotizDtoSpec gesuchNotizDtoSpec;
    private GesuchNotizCreateDtoSpec gesuchNotizCreateDtoSpec;
    private GesuchNotizUpdateDtoSpec gesuchNotizUpdateDtoSpec;

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    //create a gesuch
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAndFall(fallApiSpec, gesuchApiSpec);
    }

    //create a notiz as GS
    @Test
    @TestAsGesuchsteller
    @Order(2)
    void notizErstellenNotAllowed(){
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
        gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    // create a notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void notizErstellen(){
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
        gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    // get all notizen as GS
    @Test
    @TestAsGesuchsteller
    @Order(4)
    void getAllNotAllowed(){
        gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());

        gesuchNotizApiSpec.getNotiz()
            .notizIdPath(UUID.randomUUID().toString())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    // get all notizen as SB
    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void getAll(){
       final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDtoSpec[].class);
       final var notiz = Arrays.stream(notizen).toList().get(0);

        gesuchNotizApiSpec.getNotiz()
            .notizIdPath(notiz.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

    }

    // update notiz as GS
    @Test
    @TestAsGesuchsteller
    @Order(6)
    void updateAndDeleteNotAllowed(){
        var gesuchUpdateDto = new GesuchNotizUpdateDtoSpec();
        gesuchUpdateDto.setId(UUID.randomUUID());
        gesuchUpdateDto.setText("test");
        gesuchUpdateDto.setBetreff("test");
        gesuchNotizApiSpec.updateNotiz()
            .body(gesuchUpdateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        gesuchNotizApiSpec.deleteNotiz()
            .notizIdPath(gesuchUpdateDto.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    // update notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void update(){
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
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
    @Order(7)
    void allNotizenShouldBeCascadeDeleted(){
        var gesuchCreateDto = new GesuchNotizCreateDtoSpec();
        gesuchCreateDto.setGesuchId(gesuch.getId());
        gesuchCreateDto.setText("test");
        gesuchCreateDto.setBetreff("test");
        final var notiz = gesuchNotizApiSpec.createNotiz()
            .body(gesuchCreateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto.class);

        //delete gesuch
        gesuchApiSpec.deleteGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDto[].class);
        assertEquals(0,notizen.length);
    }
}
