package ch.dvbern.stip.api.gesuch.resource;

import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.GetGesucheSBQueryTypeDtoSpec;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDtoSpec;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    @Inject
    ConfigService configService;

    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
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
    void getMeineBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void getAlleBearbeitbarenNoneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
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
    @Order(6)
    void getMeineBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR_MEINE);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsSachbearbeiter
    @Order(7)
    void getAlleBearbeitbarenOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_BEARBEITBAR);
        allAreNotInWrongStatus(found, GesuchstatusDtoSpec.IN_BEARBEITUNG_GS, GesuchstatusDtoSpec.EINGEREICHT);
    }

    @Test
    @TestAsAdmin
    @Order(8)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void allAreNotInWrongStatus(final List<SbDashboardGesuchDtoSpec> gesuche, final GesuchstatusDtoSpec... wrongStatus) {
        for (final var gesuch : gesuche) {
            for (final var status : wrongStatus) {
                assertThat(gesuch.getStatus(), not(status));
            }
        }
    }

    private List<SbDashboardGesuchDtoSpec> getWithQueryType(final GetGesucheSBQueryTypeDtoSpec queryType) {
        return gesuchApiSpec.getGesucheSb()
            .getGesucheSBQueryTypePath(queryType)
            .pageQuery(0)
            .pageSizeQuery(configService.getMaxAllowedPageSize())
            .typQuery(GesuchTrancheTypDtoSpec.TRANCHE)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(PaginatedSbDashboardDtoSpec.class)
            .getEntries();
    }
}
