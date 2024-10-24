package ch.dvbern.stip.api.notiz.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    //create a gesuch
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAndFall(fallApiSpec, gesuchApiSpec);
    }

    // create a notiz as SB
    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void notizErstellen() {
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
        final var notiz = Arrays.stream(notizen).toList().get(0);

        gesuchNotizApiSpec.getNotiz()
            .notizIdPath(notiz.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

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
    void allNotizenShouldBeCascadeDeleted() {
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
        assertEquals(0, notizen.length);
    }
}
