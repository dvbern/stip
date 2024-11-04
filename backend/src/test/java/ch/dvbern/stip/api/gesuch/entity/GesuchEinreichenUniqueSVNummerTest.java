package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
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
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private static UUID gesuchTrancheId;
    private static UUID ausbildungId;
    private static final List<UUID> gesucheToDelete = new ArrayList<>();

    @Test
    @Order(1)
    @TestAsGesuchsteller
    void gesuchEinreichtenWithUniqueSvNummerAccepted() {
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
    @Order(2)
    @TestAsGesuchsteller
    void gesuchEinreichenWithNonUniqueSvNummerError() {
        final var gesuchCreateDtoSpec = new GesuchCreateDtoSpec();
        gesuchCreateDtoSpec.setAusbildungId(ausbildungId);
        UUID gesuchId = TestUtil.extractIdFromResponse(gesuchApiSpec.createGesuch() //neues Gesuch mit selber AHV-Nummer wird erstellt
            .body(gesuchCreateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode()));

        gesucheToDelete.add(gesuchId);
        final var gesuch = gesuchApiSpec.getCurrentGesuch()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);

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

    @Test
    @Order(3)
    @TestAsAdmin
    void deleteGesuch() {
        final var gesuche = gesuchApiSpec.getGesucheGs()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDto[].class);

        for (final var gesuchToDelete : gesucheToDelete) {
            TestUtil.deleteGesuch(gesuchApiSpec, gesuchToDelete);
        }
    }

    private UUID createFullGesuch() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        ausbildungId = gesuch.getAusbildungId();
        gesucheToDelete.add(gesuch.getId());
        gesuchTrancheId = gesuch.getGesuchTrancheToWorkWith().getId();
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);

        return gesuch.getId();
    }
}
