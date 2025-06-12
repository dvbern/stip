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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.NotificationApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NachfristAendernRequestDtoSpec;
import ch.dvbern.stip.generated.dto.NotificationDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchUpdateNachristDokumenteTest {
    public final NotificationApiSpec notificationApiSpec =
        NotificationApiSpec.notification(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());

    private final LocalDate nachreichefrist = LocalDate.now().plusDays(55);

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private GesuchDtoSpec gesuch;
    private UUID gesuchTrancheId;
    private UUID dokumentId;
    private UUID customDokumentId;
    private GesuchTrancheUpdateDtoSpec trancheUpdateDtoSpec;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB
    @Test
    @TestAsGesuchsteller
    @Order(14)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
        gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(16)
    void gesuchStatusChangeToInBearbeitungSB() {
        final var gesuchWithChanges = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(this.gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(gesuchWithChanges.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(17)
    void dokument_ablehnen_or_akzeptieren() {
        var allDokTypesExceptOne = Arrays.stream(DokumentTypDtoSpec.values()).toList();
        var modifiableDokTypeList = new ArrayList<>(allDokTypesExceptOne);
        modifiableDokTypeList.remove(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
        modifiableDokTypeList.forEach(dokType -> {
            var dokToAccept = dokumentApiSpec.getGesuchDokumentForTypSB()
                .dokumentTypPath(dokType)
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .body()
                .as(NullableGesuchDokumentDto.class);

            dokumentApiSpec.gesuchDokumentAkzeptieren()
                .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        });
        var dok = dokumentApiSpec.getGesuchDokumentForTypSB()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);

        var gesuchDokumentAblehnenRequest = new GesuchDokumentAblehnenRequestDtoSpec();
        var kommentar = new GesuchDokumentKommentarDtoSpec();
        kommentar.setKommentar("test");
        kommentar.setGesuchDokumentId(dok.getValue().getId());
        kommentar.setGesuchTrancheId(gesuchTrancheId);
        gesuchDokumentAblehnenRequest.setKommentar(kommentar);

        dokumentApiSpec.gesuchDokumentAblehnen()
            .gesuchDokumentIdPath(dok.getValue().getId())
            .body(gesuchDokumentAblehnenRequest)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Test
    @Order(19)
    void updateEinreichefrist() {
        var nachfristAendern = new NachfristAendernRequestDtoSpec();
        nachfristAendern.setNewNachfrist(nachreichefrist);
        gesuchApiSpec.updateNachfristDokumente()
            .gesuchIdPath(gesuch.getId())
            .body(nachfristAendern)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    // verify that notification is present
    @Test
    @TestAsGesuchsteller
    @Order(20)
    void verifyNotification() {
        final var notifications = notificationApiSpec.getNotificationsForCurrentUser()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(NotificationDto[].class);

        assertThat(notifications.length, greaterThan(0));
        assertThat(
            Arrays.stream(notifications)
                .toList()
                .stream()
                .anyMatch(
                    notification -> notification.getNotificationType() == NotificationType.NACHFRIST_DOKUMENTE_CHANGED
                ),
            is(true)
        );
    }

    // check that einreichefrist doesnt get changed by updateGesuch operation
    @TestAsSachbearbeiter
    @Test
    @Order(21)
    void updateGesuchShouldNotOverrideEingabefristTest() {
        TestUtil.updateGesuch(gesuchApiSpec, gesuch);

        final var updatedGesuch = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertEquals(nachreichefrist, updatedGesuch.getNachfristDokumente());
    }

    @TestAsSachbearbeiter
    @Test
    @Order(22)
    void fehlendeDokumenteUebermitteln() {
        gesuchApiSpec.gesuchFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    // check that einreichefrist wont be overwritten by fehlende dokumente übermitteln
    @TestAsSachbearbeiter
    @Test
    @Order(23)
    void gesuchEinreichefristDokumenteShouldNotBeOverwrittenTest() {
        final var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getNachfristDokumente(), is(nachreichefrist));
    }

    @TestAsGesuchsteller
    @Test
    @Order(24)
    void gesuchEinreichefristDokumenteShouldBeSetToDefaultAsGS() {
        final var item = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        assertThat(
            item.getAusbildungDashboardItems().get(0).getGesuchs().get(0).getNachfristDokumente(),
            is(nachreichefrist)
        );
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
