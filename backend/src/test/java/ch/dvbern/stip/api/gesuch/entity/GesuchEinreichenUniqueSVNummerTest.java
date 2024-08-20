package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.ResponseBody;
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
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class GesuchEinreichenUniqueSVNummerTest {

    public static final String VALID_IBAN = "CH5604835012345678009";
    private static final String UNIQUE_GUELTIGE_AHV_NUMMER = "756.2222.2222.24";
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void gesuchEinreichtenWithUniqueSvNummerAccepted() {
        RestAssured.filters(new RequestLoggingFilter());
        UUID gesuchId = createFullGesuch();

        final var file = TestUtil.getTestPng();
        for (final var dokType : DokumentTypDtoSpec.values()) {
            TestUtil.uploadFile(dokumentApiSpec, gesuchId, dokType, file);
        }

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(2)
    @TestAsGesuchsteller
    void gesuchEinreichenWithNonUniqueSvNummerError() {
        RestAssured.filters(new RequestLoggingFilter());
        UUID gesuchId = createFullGesuch(); //neues Gesuch mit selber AHV-Nummer wird erstellt

        var response = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
            .extract()
            .as(ValidationReportDtoSpec.class);

        assertThat(
            response.getValidationErrors().get(0).getMessageTemplate(),
            is(VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE));

    }

    private UUID createFullGesuch() {
        var response = gesuchApiSpec.createGesuch()
            .body(TestUtil.initGesuchCreateDto())
            .execute(ResponseBody::prettyPeek).then();

        response.assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());

        var gesuchId = TestUtil.extractIdFromResponse(response);
        var gesuchTrancheId = gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(ResponseBody::prettyPeek).then().extract()
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
        SteuerdatenUpdateDtoSpec steuerdatenUpdateDto = TestUtil.createSteuerdatenUpdateDtoSpec();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setSteuerdaten(new ArrayList<>());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getSteuerdaten().add(steuerdatenUpdateDto);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
        return gesuchId;
    }
}
