package ch.dvbern.stip.test.gesuch;

import ch.dvbern.oss.stip.contract.test.api.GesuchApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.ValidationReportDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import ch.dvbern.stip.test.util.TestUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE;
import static ch.dvbern.stip.test.generator.api.GesuchTestSpecGenerator.gesuchUpdateDtoSpecFullModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class GesuchEinreichenUniqueSVNummerTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

    private static final String UNIQUE_GUELTIGE_AHV_NUMMER = "756.2222.2222.24";

    public static final String VALID_IBAN = "CH5604835012345678009";

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void gesuchEinreichtenWithUniqueSvNummerAccepted() {
        UUID gesuchId = createFullGesuch();

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
        UUID gesuchId = createFullGesuch(); //neues Gesuch mit selber AHV-Nummer wird erstellt

        var response = gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId)
                .execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract()
                .as(ValidationReportDtoSpec.class);

        assertThat(response.getValidationErrors().get(0).getMessageTemplate(), is(VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE));

    }

    private UUID createFullGesuch() {
        var response = gesuchApiSpec.createGesuch()
                .body(TestUtil.initGesuchCreateDto())
                .execute(ResponseBody::prettyPeek).then();

        response.assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());

        var gesuchId = TestUtil.extractIdFromResponse(response);
        var gesuchTrancheId = gesuchApiSpec.getGesuch()
                .gesuchIdPath(gesuchId)
                .execute(ResponseBody::prettyPeek).then().extract()
                .body()
                .as(GesuchDtoSpec.class)
                .getGesuchTrancheToWorkWith().getId();
        var gesuchUpdatDTO = Instancio.of(gesuchUpdateDtoSpecFullModel).create();
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().setId(gesuchTrancheId);
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setSozialversicherungsnummer(UNIQUE_GUELTIGE_AHV_NUMMER);
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setSozialversicherungsnummer(UNIQUE_GUELTIGE_AHV_NUMMER);
        gesuchUpdatDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getAuszahlung().setIban(VALID_IBAN);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
        return gesuchId;
    }
}
