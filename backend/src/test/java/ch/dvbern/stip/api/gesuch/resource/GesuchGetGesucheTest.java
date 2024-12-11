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

package ch.dvbern.stip.api.gesuch.resource;

import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsJurist;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
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
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.GetGesucheSBQueryTypeDtoSpec;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDtoSpec;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    @Inject
    ConfigService configService;

    private static GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void getGsDashboardNoAusbildungTest() {

        final var fall = TestUtil.getOrCreateFall(fallApiSpec);
        final var fallDashboardItems = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);

        assertThat(fallDashboardItems.length, is(1));

        final var fallDashboardItem = fallDashboardItems[0];
        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();

        assertThat(fallDashboardItem.getNotifications().isEmpty(), is(true));

        /*
         * If only a ausbildung is created,
         * but not yet a gesuch (or a tranche),
         * an empty gesuch with a empty gesuchtranche should be returned
         */
        TestUtil.createAusbildung(ausbildungApiSpec, fall.getId());
        final var fallDashboardItems2 = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);
        assertNotNull(
            fallDashboardItems2[0].getAusbildungDashboardItems().get(0).getGesuchs().get(0).getCurrentTrancheId()
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
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
            .statusCode(Status.NO_CONTENT.getStatusCode());
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
    @TestAsGesuchsteller
    @Order(9)
    void getGsDashboardTest() {
        final var fallDashboardItems = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);

        assertThat(fallDashboardItems.length, is(1));

        final var fallDashboardItem = fallDashboardItems[0];
        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();

        assertThat(fallDashboardItem.getNotifications().size(), greaterThanOrEqualTo(1));

        assertThat(ausbildungDashboardItems.size(), greaterThanOrEqualTo(1));

        final var ausbildungDashboardItem = ausbildungDashboardItems.get(0);
        final var gesuchDashboardItems = ausbildungDashboardItem.getGesuchs();

        assertThat(gesuchDashboardItems.size(), greaterThanOrEqualTo(1));

        final var gesuchDashboardItem = gesuchDashboardItems.get(0);

        assertThat(gesuchDashboardItem.getGesuchStatus(), is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG));
    }

    @Test
    @TestAsAdmin
    @Order(10)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    @Order(11)
    void getGsDashboardTestNoAusbildung() {
        final var fallDashboardItems = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);

        assertThat(fallDashboardItems.length, is(1));

        final var fallDashboardItem = fallDashboardItems[0];
        final var ausbildungDashboardItems = fallDashboardItem.getAusbildungDashboardItems();

        assertThat(ausbildungDashboardItems.size(), is(0));
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void prepareForJuristischAbklaeren() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void juristischAbklaeren() {
        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        gesuchNotizApiSpec.createNotiz()
            .body(
                new GesuchNotizCreateDtoSpec()
                    .gesuchId(gesuch.getId())
                    .notizTyp(GesuchNotizTypDtoSpec.JURISTISCHE_NOTIZ)
                    .betreff("Test")
                    .text("Test")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsJurist
    @Order(14)
    void getAlleJurisitischeAbklaerungOneFound() {
        final var found = getWithQueryType(GetGesucheSBQueryTypeDtoSpec.ALLE_JURISTISCHE_ABKLAERUNG_MEINE);
        allAreNotInWrongStatus(
            found,
            GesuchstatusDtoSpec.IN_BEARBEITUNG_GS,
            GesuchstatusDtoSpec.EINGEREICHT,
            GesuchstatusDtoSpec.IN_BEARBEITUNG_SB,
            GesuchstatusDtoSpec.BEREIT_FUER_BEARBEITUNG
        );
        assertEquals(1, found.size());
    }

    @Test
    @TestAsAdmin
    @Order(15)
    @AlwaysRun
    void deleteOtherGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

    private void allAreNotInWrongStatus(
        final List<SbDashboardGesuchDtoSpec> gesuche,
        final GesuchstatusDtoSpec... wrongStatus
    ) {
        for (final var gesuch : gesuche) {
            for (final var status : wrongStatus) {
                assertThat(gesuch.getGesuchStatus(), not(status));
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
