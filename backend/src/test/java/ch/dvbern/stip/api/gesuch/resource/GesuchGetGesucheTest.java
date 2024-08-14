package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GetGesucheSBQueryTypeDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
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

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchGetGesucheTest {
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
    @TestAsSachbearbeiter
    @Order(2)
    void getAlleGesuchenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE);
        assertThat(found.length, is(0));
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void getAlleGesucheOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE);
        assertThat(found.length, is(1));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void getMeineBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        assertThat(found.length, is(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void getAlleBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        assertThat(found.length, is(0));
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void getMeineBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        assertThat(found.length, is(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void getAlleBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        assertThat(found.length, is(1));
    }

    @Test
    @TestAsAdmin
    @Order(10)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private GesuchDtoSpec[] getWithQueryType(final GetGesucheSBQueryTypeDtoSpec queryType) {
        return gesuchApiSpec.getGesucheSb()
            .getGesucheSBQueryTypePath(queryType)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);
    }
}
