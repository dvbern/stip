package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.*;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchTrancheResourceImplTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAndFall(fallApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void createFirstAenderungsantrag() {
        createAenderungsanstrag()
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }


    @Test
    @TestAsGesuchsteller
    @Order(5)
    @Description("Only one (open: NOT in State ABGELEHNT|AKZEPTIIERT) Aenderungsantrag should be allowed")
    void createSecondAenderungsantragFails() {
        createAenderungsanstrag()
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    io.restassured.response.Response createAenderungsanstrag() {
        return gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(new CreateAenderungsantragRequestDtoSpec().comment("aenderung1")
                .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb())
                .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis()))
            .execute(TestUtil.PEEK_IF_ENV_SET);
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    @Description("The GS should be able do delete a Aenderung, if it is in State 'In Bearbeitung GS'")
    void deleteAenderungTest() {
        GesuchTrancheSlimDtoSpec[] gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchTrancheSlimDtoSpec[].class);
        int count = gesuchtranchen.length;
        final var aenderung = Arrays.stream(gesuchtranchen).filter(tranche -> tranche.getTyp() == GesuchTrancheTypDtoSpec.AENDERUNG).findFirst().get();
        //delete aenderung
        gesuchTrancheApiSpec.aenderungLoeschen().aenderungIdPath(aenderung.getId()).execute(TestUtil.PEEK_IF_ENV_SET).then().assertThat().statusCode(Response.Status.OK.getStatusCode());

        // assert that list size is -1 to previous
        gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchTrancheSlimDtoSpec[].class);
        assertThat(gesuchtranchen.length).isLessThan(count);
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    @Description("Only one (open: NOT in State ABGELEHNT|AKZEPTIIERT) Aenderungsantrag should be allowed")
    void deleteAenderungShouldFailTest() {
        GesuchTrancheSlimDtoSpec[] gesuchtranchen = gesuchTrancheApiSpec.getAllTranchenForGesuch()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchTrancheSlimDtoSpec[].class);
        final var tranche = Arrays.stream(gesuchtranchen).filter(t -> t.getTyp() == GesuchTrancheTypDtoSpec.TRANCHE).findFirst().get();
        gesuchTrancheApiSpec.aenderungLoeschen().aenderungIdPath(tranche.getId()).execute(TestUtil.PEEK_IF_ENV_SET).then().assertThat().statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }


    // todo KSTIP-KSTIP-1158: a Aenderung should be accepted/denied by an SB
}
