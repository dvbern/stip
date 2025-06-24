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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.KommentarDtoSpec;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;
import static ch.dvbern.stip.generated.dto.DokumentTypDtoSpec.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentHistoryResourceTest {
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());

    private UUID gesuchId;
    private GesuchDtoSpec gesuch;
    private GesuchWithChangesDtoSpec returnedGesuch;
    private UUID gesuchTrancheId;
    private List<GesuchDokumentDtoSpec> initialGesuchDokuments = null;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void test_prepare_gesuch_for_dokument() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
        assertThat(gesuch.getId(), notNullValue());
    }

    // for the next tests, gesuch
    // should be in state IN_BEARBEITUNG_SB
    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
        TestUtil.fillAuszahlung(gesuch.getFallId(), auszahlungApiSpec, TestUtil.getAuszahlungUpdateDtoSpec());

        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(0).setWohnkosten(0);
        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(1).setWohnkosten(0);

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(fullGesuch)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuch.getGesuchTrancheToWorkWith().getId(), dokTyp, file);
        }

        initialGesuchDokuments = Arrays.stream(
            gesuchTrancheApiSpec.getGesuchDokumenteGS()
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDokumentDtoSpec[].class)
        ).toList();
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
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
    @Order(4)
    void gesuchStatusChangeToInBearbeitungSB() {
        returnedGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(returnedGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(5)
    void testMoreDocumentsNeeded() {
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();

        overrideEssentialsForUpdate(fullGesuch, returnedGesuch);

        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(0).setWohnkosten(5);
        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(1).setWohnkosten(5);
        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(0).setSozialhilfebeitraege(false);
        fullGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(1).setSozialhilfebeitraege(false);

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuch.getId())
            .body(fullGesuch)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        final var validationReport = gesuchTrancheApiSpec.validateGesuchTranchePagesSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationWarnings().get(0).getMessageTemplate(),
            equalTo(VALIDATION_DOCUMENTS_REQUIRED_MESSAGE)
        );

        final var dokumenteToUpload = gesuchTrancheApiSpec.getDocumentsToUploadSB()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDtoSpec.class);

        assertThat(dokumenteToUpload.getRequired().get(0), equalTo(ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(6)
    void removeSuperfluousDokuments() {
        final var gesuchDokuments = Arrays.stream(
            gesuchTrancheApiSpec.getGesuchDokumenteSB()
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDokumentDtoSpec[].class)
        ).toList();
        assertThat(gesuchDokuments.size(), is(17));
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void testGSDoesNotSeeMoreNeeded() {
        final var validationReport = gesuchTrancheApiSpec.validateGesuchTranchePagesGS()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationWarnings().size(),
            equalTo(0)
        );

        final var dokumenteToUpload = gesuchTrancheApiSpec.getDocumentsToUploadGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDtoSpec.class);

        assertThat(dokumenteToUpload.getRequired().size(), equalTo(0));

        final var gesuchDokuments = Arrays.stream(
            gesuchTrancheApiSpec.getGesuchDokumenteGS()
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDokumentDtoSpec[].class)
        ).toList();

        assertThat(gesuchDokuments.size(), is(19));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(8)
    void dokument_ablehnen_or_akzeptieren() {
        var allDokTypesExceptOne = Arrays.stream(DokumentTypDtoSpec.values()).toList();
        var modifiableDokTypeList = new ArrayList<>(allDokTypesExceptOne);
        modifiableDokTypeList.remove(ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
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

            if (dokToAccept.getValue() != null) {
                dokumentApiSpec.gesuchDokumentAkzeptieren()
                    .gesuchDokumentIdPath(dokToAccept.getValue().getId())
                    .execute(TestUtil.PEEK_IF_ENV_SET)
                    .then()
                    .assertThat()
                    .statusCode(Status.NO_CONTENT.getStatusCode());
            }
        });
    }

    @TestAsSachbearbeiter
    @Test
    @Order(9)
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
    @Order(10)
    void testGSDoesSeeMoreNeeded() {
        final var validationReport = gesuchTrancheApiSpec.validateGesuchTranchePagesGS()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationWarnings().get(0).getMessageTemplate(),
            equalTo(VALIDATION_DOCUMENTS_REQUIRED_MESSAGE)
        );

        final var dokumenteToUpload = gesuchTrancheApiSpec.getDocumentsToUploadGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DokumenteToUploadDtoSpec.class);

        assertThat(dokumenteToUpload.getRequired().get(0), equalTo(ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE));

        final var gesuchDokuments = Arrays.stream(
            gesuchTrancheApiSpec.getGesuchDokumenteGS()
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDokumentDtoSpec[].class)
        ).toList();

        assertThat(gesuchDokuments.size(), is(17));
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void dokumenteHochladen() {
        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuch.getGesuchTrancheToWorkWith().getId(), dokTyp, file);
        }
        gesuchTrancheApiSpec.getGesuchDokumenteGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void gesuchTrancheFehlendeDokumenteEinreichen() {
        gesuchApiSpec.gesuchTrancheFehlendeDokumenteEinreichen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(13)
    void gesuchStatusChangeToInBearbeitungSB2() {
        returnedGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(returnedGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void zurueckweisen() {
        gesuchApiSpec.gesuchZurueckweisen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(
                new KommentarDtoSpec()
                    .text("DONT_CARE")
            )
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(15)
    void testDokumenteRestored() {
        final var gesuchDokuments = Arrays.stream(
            gesuchTrancheApiSpec.getGesuchDokumenteGS()
                .gesuchTrancheIdPath(gesuchTrancheId)
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(GesuchDokumentDtoSpec[].class)
        ).toList();
        var gesuchDokumentsComp = new ArrayList<>(gesuchDokuments);
        gesuchDokumentsComp.sort(Comparator.comparing(GesuchDokumentDtoSpec::getDokumentTyp));
        var gesuchDokumentsInit = new ArrayList<>(initialGesuchDokuments);
        gesuchDokumentsInit.sort(Comparator.comparing(GesuchDokumentDtoSpec::getDokumentTyp));

        assertThat(gesuchDokumentsComp.size(), is(initialGesuchDokuments.size()));

        // Assert that the before and after of the gesuchDokument is equal
        for (int i = 0; i < gesuchDokumentsComp.size(); i++) {
            assertThat(
                gesuchDokumentsComp.get(i).getDokumentTyp(),
                equalTo(gesuchDokumentsInit.get(i).getDokumentTyp())
            );
            assertThat(
                gesuchDokumentsComp.get(i).getCustomDokumentTyp(),
                equalTo(gesuchDokumentsInit.get(i).getCustomDokumentTyp())
            );
            assertThat(gesuchDokumentsComp.get(i).getStatus(), equalTo(gesuchDokumentsInit.get(i).getStatus()));
            assertThat(
                gesuchDokumentsComp.get(i).getDokumente().size(),
                equalTo(gesuchDokumentsInit.get(i).getDokumente().size())
            );
        }
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private GesuchUpdateDtoSpec overrideEssentialsForUpdate(
        GesuchUpdateDtoSpec toOverride,
        GesuchWithChangesDtoSpec override
    ) {
        toOverride.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getElterns()
            .get(0)
            .setId(
                override.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(0).getId()
            );

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getElterns()
            .get(0)
            .getAdresse()
            .setId(
                override.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(0).getAdresse().getId()
            );

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getElterns()
            .get(1)
            .setId(
                override.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(1).getId()
            );

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getElterns()
            .get(1)
            .getAdresse()
            .setId(
                override.getGesuchTrancheToWorkWith().getGesuchFormular().getElterns().get(1).getAdresse().getId()
            );

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getAdresse()
            .setId(
                override.getGesuchTrancheToWorkWith()
                    .getGesuchFormular()
                    .getPersonInAusbildung()
                    .getAdresse()
                    .getId()
            );

        toOverride.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPartner()
            .getAdresse()
            .setId(
                override.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().getAdresse().getId()
            );

        toOverride.getGesuchTrancheToWorkWith().getGesuchFormular().setKinds(null);
        toOverride.getGesuchTrancheToWorkWith().getGesuchFormular().setGeschwisters(null);
        return toOverride;
    }

}
