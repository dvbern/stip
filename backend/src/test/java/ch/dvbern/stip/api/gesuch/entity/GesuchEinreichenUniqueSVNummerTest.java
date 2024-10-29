package ch.dvbern.stip.api.gesuch.entity;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class GesuchEinreichenUniqueSVNummerTest {

    public static final String VALID_IBAN = "CH5604835012345678009";
    private static final String UNIQUE_GUELTIGE_AHV_NUMMER = "756.2222.2222.24";
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());

    UUID gesuchTrancheId;

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void gesuchEinreichtenWithUniqueSvNummerAccepted() {
        RestAssured.filters(new RequestLoggingFilter());
        UUID gesuchId = createFullGesuch();

        final var file = TestUtil.getTestPng();
        for (final var dokType : DokumentTypDtoSpec.values()) {
            TestUtil.uploadFile(dokumentApiSpec, gesuchTrancheId, dokType, file);
        }

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(3)
    @TestAsGesuchsteller
    void gesuchEinreichenWithNonUniqueSvNummerError() {
        RestAssured.filters(new RequestLoggingFilter());
        UUID gesuchId = createFullGesuch(); //neues Gesuch mit selber AHV-Nummer wird erstellt

        var response = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
            .extract()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            response.getValidationErrors().get(0).getMessageTemplate(),
            is(VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE)
        );

    }

    private UUID createFullGesuch() {
        final var ausbildung = TestUtil.createAusbildung(ausbildungApiSpec, UUID.fromString(TestConstants.FALL_TEST_ID));
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setAusbildungId(ausbildung.getId());

        var response = gesuchApiSpec.createGesuch()
            .body(gesuchDTO)
            .execute(TestUtil.PEEK_IF_ENV_SET).then();

        response.assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());

        var gesuchId = TestUtil.extractIdFromResponse(response);
        gesuchTrancheId = gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET).then().extract()
            .body()
            .as(GesuchDtoSpec.class)
            .getGesuchTrancheToWorkWith().getId();
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuchTrancheId);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .setSozialversicherungsnummer(UNIQUE_GUELTIGE_AHV_NUMMER);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getAuszahlung().setIban(VALID_IBAN);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
        return gesuchId;
    }
}
