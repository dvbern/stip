package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.*;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class DeleteGesuchAsAdminTest {

    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
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
    @TestAsAdmin
    @Order(3)
    void deleteGesuchAsAdmin() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    /**
     * Test setup for next delete operation
     */

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void gesuchErstellen2() {
        gesuch = TestUtil.createGesuchAndFall(fallApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void fillGesuch2() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsAdmin
    @Order(7)
    void deleteGesuchAsAdminInStatusEingereicht() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

}
