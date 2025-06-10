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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchNachfristDokumenteSetDefaultValueTest {
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());

    @Getter
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;
    private UUID gesuchTrancheId;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB
    @Test
    @TestAsGesuchsteller
    @Order(14)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
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
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(16)
    void gesuchStatusChangeToInBearbeitungSB() {
        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
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
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(NullableGesuchDokumentDto.class);

            dokumentApiSpec.gesuchDokumentAkzeptieren()
                .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.NO_CONTENT.getStatusCode());
        });
        var dok = dokumentApiSpec.getGesuchDokumentForTypSB()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
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
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Test
    @Order(19)
    void fehlendeDokumenteUebermitteln() {
        gesuchApiSpec.gesuchFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @TestAsSachbearbeiter
    @Test
    @Order(20)
    void gesuchEinreichefristDokumenteShouldBeSetToDefaultAsSB() {
        final var gesuchWithChanges = gesuchApiSpec.getGesuchSB()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(gesuchWithChanges.getNachfristDokumente(), is(LocalDate.now().plusDays(30)));
    }

    @TestAsGesuchsteller
    @Test
    @Order(21)
    void gesuchEinreichefristDokumenteShouldBeSetToDefaultAsGS() {
        final var fallDashboardItemDto = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        var ausbildungDashBoardItemDtos =
            fallDashboardItemDto.getAusbildungDashboardItems();
        var gesuchDashBoardItemDtos =
            ausbildungDashBoardItemDtos.get(ausbildungDashBoardItemDtos.size() - 1).getGesuchs();
        fallDashboardItemDto.getAusbildungDashboardItems()
            .forEach(
                ausbildungDashboardItemDto -> ausbildungDashboardItemDto.getGesuchs()
                    .forEach(
                        gesuchDashboardItemDto -> {
                            if (
                                Objects
                                    .equals(gesuchDashboardItemDto.getCurrentTrancheId(), gesuchTrancheId)
                            ) {
                                assertThat(
                                    gesuchDashboardItemDto.getNachfristDokumente(),
                                    is(notNullValue())
                                );
                            }
                        }
                    )
            );

        assertThat(
            gesuchDashBoardItemDtos.get(0).getNachfristDokumente(),
            is(notNullValue())
        );
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

}
