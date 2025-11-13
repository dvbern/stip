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

package ch.dvbern.stip.api.dokument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.TEST_FILE_LOCATION;
import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesFehlendeCustomDokumenteUebermittelnTest {
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());

    public GesuchApiSpec getGesuchApiSpec() {
        return gesuchApiSpec;
    }

    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;
    private UUID gesuchTrancheId;
    private UUID dokumentId;
    private UUID customDokumentId;

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
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
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
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
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

    GesuchDokumentDto createdGesuchDokumentWithCustomType;

    @Test
    @TestAsSachbearbeiter
    @Order(18)
    void create_custom_gesuchdokument() {
        CustomDokumentTypCreateDtoSpec customDokumentTypCreateDtoSpec = new CustomDokumentTypCreateDtoSpec();
        customDokumentTypCreateDtoSpec.setType("test");
        customDokumentTypCreateDtoSpec.setDescription("test description");
        customDokumentTypCreateDtoSpec.setTrancheId(gesuchTrancheId);
        createdGesuchDokumentWithCustomType = dokumentApiSpec.createCustomDokumentTyp()
            .body(customDokumentTypCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDto.class);
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

    @Test
    @TestAsGesuchsteller
    @Order(20)
    void test_get_required_custom_gesuchdokuments_should_not_be_empty() {
        /*
         * Both the denied & the newly added custom document should:
         * appear in requiredDocuments
         */
        final var requiredDocuments = gesuchTrancheApiSpec.getDocumentsToUploadGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDto.class);
        assertThat(requiredDocuments.getCustomDokumentTyps().size(), is(1));
        assertThat(requiredDocuments.getRequired().size(), is(1));
    }

    @Test
    @TestAsGesuchsteller
    @Order(21)
    void test_get_abgelehnte_and_created_custom_dokument_should_be_in_correct_state() {
        /*
         * Both the denied & the newly added custom document should:
         * be in state "AUSSTEHEND"
         * not contain any attached files
         */
        var dok = dokumentApiSpec.getGesuchDokumentForTypGS()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(dok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(dok.getValue().getDokumente(), is(empty()));

        var customDok = dokumentApiSpec.getCustomGesuchDokumentForTypGS()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(customDok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(customDok.getValue().getDokumente(), is(empty()));
    }

    /*
     * GS re-uploads required/denied files
     * GS executes "fehlende Dokumente einreichen"
     * both files should still be in state "AUSSTHEND", but should contain files.
     * Also, it should be made sure that all GesuchDokuments contain at least 1 file
     *
     */
    @Test
    @TestAsGesuchsteller
    @Order(22)
    void reupload_abgelehnte_dokumente() {
        var customDok = dokumentApiSpec.getCustomGesuchDokumentForTypGS()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);

        File file = new File(TEST_PNG_FILE_LOCATION);
        TestUtil.uploadFile(
            dokumentApiSpec,
            gesuch.getGesuchTrancheToWorkWith().getId(),
            DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE,
            file
        );

        // a file in custom dokument is still missing - 403 (Forbidden) should occur
        gesuchApiSpec.gesuchTrancheFehlendeDokumenteEinreichen()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        TestUtil.uploadCustomDokumentFile(
            dokumentApiSpec,
            customDok.getValue().getCustomDokumentTyp().getId(),
            file
        );
        gesuchApiSpec.gesuchTrancheFehlendeDokumenteEinreichen()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        var dok = dokumentApiSpec.getGesuchDokumentForTypGS()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(dok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(dok.getValue().getDokumente().size(), is(greaterThan(0)));

        customDok = dokumentApiSpec.getCustomGesuchDokumentForTypGS()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(customDok.getValue(), Matchers.nullValue());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(23)
    void setToInBearbeitungSB() {
        final var customDok = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(customDok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(customDok.getValue().getDokumente().size(), is(greaterThan(0)));

        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    /*
     * SB denies both documents
     * SB executes "fehlende Dokumente übermitteln"
     * operation should not fail
     * both files should be in state "AUSSTHEND" and not contain any files
     */
    @Test
    @TestAsSachbearbeiter
    @Order(24)
    void deny_reuploaded_dokumente_of_both_categories() {
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
        var customDok = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
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

        // deny a "normal" gesuchdokument
        dokumentApiSpec.gesuchDokumentAblehnen()
            .gesuchDokumentIdPath(dok.getValue().getId())
            .body(gesuchDokumentAblehnenRequest)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        var customGesuchDokumentAblehnenRequest = new GesuchDokumentAblehnenRequestDtoSpec();
        var customKommentar = new GesuchDokumentKommentarDtoSpec();
        customKommentar.setKommentar("test");
        customKommentar.setGesuchDokumentId(customDok.getValue().getId());
        customKommentar.setGesuchTrancheId(gesuchTrancheId);
        customGesuchDokumentAblehnenRequest.setKommentar(kommentar);
        // deny a custom gesuchdokument
        dokumentApiSpec.gesuchDokumentAblehnen()
            .gesuchDokumentIdPath(customDok.getValue().getId())
            .body(customGesuchDokumentAblehnenRequest)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
        // send missing files to GS
        gesuchApiSpec.gesuchFehlendeDokumenteUebermitteln()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        // check that both gesuchdokuments are in correct state & do not contain files
        dok = dokumentApiSpec.getGesuchDokumentForTypSB()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);

        assertThat(dok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(dok.getValue().getDokumente(), is(empty()));

        customDok = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(customDok.getValue().getStatus(), is(GesuchDokumentStatus.AUSSTEHEND));
        assertThat(customDok.getValue().getDokumente(), is(empty()));
    }

    /*
     * GS re-uploads required/denied files
     */
    @Test
    @TestAsGesuchsteller
    @Order(25)
    void reupload_dokumente() {
        gesuchApiSpec.gesuchTrancheFehlendeDokumenteEinreichen()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.FORBIDDEN.getStatusCode());
        reupload_abgelehnte_dokumente();
    }

    @Test
    @TestAsSachbearbeiter
    @Order(26)
    void reset_to_in_bearbeitung_sb() {
        setToInBearbeitungSB();
    }

    /*
     * SB accepts all files
     * Operation should not fail
     */
    @Test
    @TestAsSachbearbeiter
    @Order(27)
    void accept_reuploaded_dokumente_of_both_categories() {
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
        var customDok = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
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

        // accept a "normal" gesuchdokument
        dokumentApiSpec.gesuchDokumentAkzeptieren()
            .gesuchDokumentIdPath(dok.getValue().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        // accept a custom gesuchdokument
        dokumentApiSpec.gesuchDokumentAkzeptieren()
            .gesuchDokumentIdPath(customDok.getValue().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        // check that both gesuchdokuments are in correct state & do contain files
        dok = dokumentApiSpec.getGesuchDokumentForTypSB()
            .dokumentTypPath(DokumentTypDtoSpec.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE)
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);

        assertThat(dok.getValue().getStatus(), is(GesuchDokumentStatus.AKZEPTIERT));
        assertThat(dok.getValue().getDokumente().size(), is(greaterThan(0)));

        customDok = dokumentApiSpec.getCustomGesuchDokumentForTypSB()
            .customDokumentTypIdPath(createdGesuchDokumentWithCustomType.getCustomDokumentTyp().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(NullableGesuchDokumentDto.class);
        assertThat(customDok.getValue().getStatus(), is(GesuchDokumentStatus.AKZEPTIERT));
        assertThat(customDok.getValue().getDokumente().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private String readFileData() throws IOException {
        return Files.readString(new File(TEST_FILE_LOCATION).toPath());
    }
}
