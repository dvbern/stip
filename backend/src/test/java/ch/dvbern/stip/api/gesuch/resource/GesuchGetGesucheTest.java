package ch.dvbern.stip.api.gesuch.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
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
import static org.hamcrest.Matchers.not;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
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
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void getAlleGesucheNoneWithoutPiaFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE);
        final var withoutPia = Arrays.stream(found)
            .filter(gesuch -> gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung() == null)
            .toList();
        assertThat(withoutPia.size(), is(0));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void getMeineBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void getAlleBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
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
            .statusCode(Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void getMeineBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void getAlleBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsAdmin
    @Order(9)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void allAreNotInWrongStatus(final GesuchDtoSpec[] gesuche, final GesuchstatusDtoSpec... wrongStatus) {
        for (final var gesuch : gesuche) {
            for (final var status : wrongStatus) {
                assertThat(gesuch.getGesuchStatus(), not(status));
            }
        }
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
