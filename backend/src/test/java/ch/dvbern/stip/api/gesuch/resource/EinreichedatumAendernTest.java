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

import java.time.LocalDate;
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
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchNotizApiSpec;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EinreichedatumAendernTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchNotizApiSpec gesuchNotizApiSpec = GesuchNotizApiSpec.gesuchNotiz(RequestSpecUtil.quarkusSpec());

    private LocalDate oldEinreichedatum;
    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void fillGesuch() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);

        gesuchApiSpec.gesuchEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void einreichedatumIsSet() {
        gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(gesuch.getEinreichedatum(), is(notNullValue()));
        oldEinreichedatum = gesuch.getEinreichedatum();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void sbCannotUpdateInWrongState() {
        gesuchApiSpec.einreichedatumManuellAendern()
            .gesuchIdPath(gesuch.getId())
            .body(
                new EinreichedatumAendernRequestDtoSpec()
                    .newEinreichedatum(LocalDate.of(2025, 1, 31))
                    .text("Test notizen Text")
                    .betreff("Test betreff Text")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void sbBearbeiten() {
        BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec())
            .prepareCurrentBenutzer()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void einreichedatumAusserhalbGesuchsperiode() {
        gesuchApiSpec.einreichedatumManuellAendern()
            .gesuchIdPath(gesuch.getId())
            .body(
                new EinreichedatumAendernRequestDtoSpec()
                    .newEinreichedatum(LocalDate.of(2000, 1, 31))
                    .text("Test notizen Text")
                    .betreff("Test betreff Text")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void einreichedatumAendern() {
        gesuch = gesuchApiSpec.einreichedatumManuellAendern()
            .gesuchIdPath(gesuch.getId())
            .body(
                new EinreichedatumAendernRequestDtoSpec()
                    .newEinreichedatum(LocalDate.of(2025, 1, 31))
                    .text("Test notizen Text")
                    .betreff("Test betreff Text")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(gesuch.getEinreichedatum(), is(not(oldEinreichedatum)));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void einreichedatumAendernCreatedNotiz() {
        final var notizen = gesuchNotizApiSpec.getNotizen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchNotizDtoSpec[].class);

        assertThat(Arrays.asList(notizen), is(not(empty())));
    }

    @Test
    @TestAsAdmin
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
