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

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.generator.api.model.gesuch.AuszahlungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PartnerUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuererklaerungUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.NotificationApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.PartnerUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import ch.dvbern.stip.generated.dto.ZivilstandDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchFormularNullableFieldsByGesuchstatusTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final NotificationApiSpec notificationApiSpec =
        NotificationApiSpec.notification(RequestSpecUtil.quarkusSpec());
    private UUID fallId;
    private UUID gesuchId;
    private UUID gesuchTrancheId;
    private UUID ausbildungId;
    private final GesuchFormularUpdateDtoSpec currentFormular = new GesuchFormularUpdateDtoSpec();
    private GesuchTrancheUpdateDtoSpec trancheUpdateDtoSpec;
    private PartnerUpdateDtoSpec partnerUpdateDtoSpec;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
        ausbildungId = gesuch.getAusbildungId();
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();

        assertThat(
            "Newly created Gesuch is not IN_BEARBEITUNG_GS",
            gesuch.getGesuchStatus(),
            is(GesuchstatusDtoSpec.IN_BEARBEITUNG_GS)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchTrancheCreated() {
        final var gesuch = gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        trancheUpdateDtoSpec = new GesuchTrancheUpdateDtoSpec();
        trancheUpdateDtoSpec.setId(gesuch.getGesuchTrancheToWorkWith().getId());
        trancheUpdateDtoSpec.setGesuchFormular(currentFormular);

        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGueltigAb(),
            is(GUELTIGKEIT_PERIODE_23_24.getGueltigAb())
        );
        assertThat(
            gesuch.getGesuchTrancheToWorkWith().getGueltigBis(),
            is(GUELTIGKEIT_PERIODE_23_24.getGueltigBis())
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void addPersonInAusbildung() {
        var pia = PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec();
        pia.setZivilstand(ZivilstandDtoSpec.KONKUBINAT);
        currentFormular.setPersonInAusbildung(pia);
        final var returnedGesuch = patchGesuch();

        assertThat(
            returnedGesuch.getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getAdresse()
                .getId(),
            is(notNullValue())
        );

        // Set the Adresse ID from the returned Gesuch, so follow-up calls won't want to change it
        currentFormular.getPersonInAusbildung()
            .getAdresse()
            .setId(
                returnedGesuch.getGesuchTrancheToWorkWith()
                    .getGesuchFormular()
                    .getPersonInAusbildung()
                    .getAdresse()
                    .getId()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void addLebenslauf() {
        final var lebenslaufItems = LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs();
        currentFormular.setLebenslaufItems(lebenslaufItems);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void addFamiliensituation() {
        final var famsit = FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec();
        currentFormular.setFamiliensituation(famsit);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void addElternWithNullableFields() {
        var eltern = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2);
        eltern.get(0).setElternTyp(ElternTypDtoSpec.VATER);
        eltern.get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATER);
        // set nullable fields to null
        eltern.get(0).setWohnkosten(null);
        // todo: enable eltern.get(0).setErgaenzungsleistungen(null);

        eltern.get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
        eltern.get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);
        // set nullable fields to null
        eltern.get(1).setWohnkosten(null);
        // todo: enable eltern.get(1).setErgaenzungsleistungen(null);

        currentFormular.setElterns(eltern);
        // patch should still be possible
        patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(9)
    void addSteuererklaerung() {
        final var steuererklaerung =
            SteuererklaerungUpdateTabsDtoSpecModel.steuererklaerungDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        currentFormular.setSteuererklaerung(List.of(steuererklaerung));
        patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(10)
    void addGeschwister() {
        final var geschwister = GeschwisterUpdateDtoSpecModel.geschwisterUpdateDtoSpecs();
        currentFormular.setGeschwisters(geschwister);
        patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void addAuszahlung() {
        final var auszahlung = AuszahlungUpdateDtoSpecModel.auszahlungUpdateDtoSpec();
        patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(12)
    void addEinnahmenKosten() {
        final var einnahmenKosten = EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec();
        currentFormular.setEinnahmenKosten(einnahmenKosten);
        patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
    }

    @Test
    @TestAsGesuchsteller
    @Order(13)
    void addDokumente() {
        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuchTrancheId, dokTyp, file);
        }
    }

    @Test
    @TestAsGesuchsteller
    @Order(14)
    void addPartnerWithNullableFields() {
        partnerUpdateDtoSpec = (PartnerUpdateDtoSpec) PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec();
        var partner = partnerUpdateDtoSpec;
        partner.setVerpflegungskosten(null);
        partner.setFahrkosten(null);
        partner.setJahreseinkommen(null);

        currentFormular.setPartner(partner);
        // patch should still be possible
        final var updatedGesuch = patchGesuch();
        assertThat(getValidationReport().getValidationErrors().size(), is(greaterThan(0)));
        final var adresse = updatedGesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().getAdresse();
        partnerUpdateDtoSpec.setAdresse(adresse);
    }

    @Test
    @TestAsGesuchsteller
    @Order(20)
    void gesuchEinreichenValidationShouldFail() {
        final var validationReport = gesuchTrancheApiSpec.gesuchTrancheEinreichenValidierenGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationErrors().toString(),
            validationReport.getValidationErrors().size(),
            greaterThan(0)
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(21)
    void gesuchEinreichenShouldFail() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(22)
    void updateNullableValues() {
        var eltern = ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2);
        eltern.get(0).setElternTyp(ElternTypDtoSpec.VATER);
        eltern.get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATER);
        // set nullable fields to null
        eltern.get(0).setWohnkosten(0);
        eltern.get(0).setErgaenzungsleistungen(0);

        eltern.get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
        eltern.get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);
        // set nullable fields to null
        eltern.get(1).setWohnkosten(0);
        eltern.get(1).setErgaenzungsleistungen(0);
        currentFormular.setElterns(eltern);

        // validation should still fail
        final var validationReport = gesuchTrancheApiSpec.gesuchTrancheEinreichenValidierenGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationErrors().toString(),
            validationReport.getValidationErrors().size(),
            greaterThan(0)
        );

        var partner = partnerUpdateDtoSpec;
        partner.setVerpflegungskosten(0);
        partner.setFahrkosten(0);
        partner.setJahreseinkommen(0);

        currentFormular.setPartner(partner);
        patchAndValidate();
    }

    @Test
    @TestAsGesuchsteller
    @Order(23)
    void gesuchEinreichenValidationShouldSuccess() {
        final var validationReport = gesuchTrancheApiSpec.gesuchTrancheEinreichenValidierenGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            validationReport.getValidationErrors().toString(),
            validationReport.getValidationErrors().size(),
            not(greaterThan(0))
        );
    }

    @Test
    @TestAsGesuchsteller
    @Order(24)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @StepwiseExtension.AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }

    private GesuchWithChangesDtoSpec patchAndValidate() {
        final var returnedGesuch = patchGesuch();
        validatePage();
        return returnedGesuch;
    }

    private GesuchWithChangesDtoSpec patchGesuch() {
        final var gesuchUpdateDtoSpec = new GesuchUpdateDtoSpec();
        gesuchUpdateDtoSpec.setGesuchTrancheToWorkWith(trancheUpdateDtoSpec);

        gesuchApiSpec.updateGesuch()
            .gesuchIdPath(gesuchId)
            .body(gesuchUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        return gesuchApiSpec.getGesuchGS()
            .gesuchTrancheIdPath(trancheUpdateDtoSpec.getId())
            .execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
    }

    private void validatePage() {
        validatePage(true);
    }

    private ValidationReportDto getValidationReport() {
        return gesuchTrancheApiSpec
            .validateGesuchTranchePagesGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(ValidationReportDto.class);
    }

    private void validatePage(final boolean allowWarnings) {
        final var report = gesuchTrancheApiSpec
            .validateGesuchTranchePagesGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .as(ValidationReportDto.class);

        assertThat(report.getValidationErrors(), is(empty()));
        if (!allowWarnings) {
            assertThat(report.getValidationWarnings(), is(empty()));
        }
    }
}
