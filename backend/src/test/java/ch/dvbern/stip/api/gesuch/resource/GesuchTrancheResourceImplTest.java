package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import javax.management.DescriptorKey;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class GesuchTrancheResourceImplTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec = GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch= TestUtil.createGesuchAndFall(fallApiSpec, gesuchApiSpec);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void fillGesuch() {
        TestUtil.fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichen()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    @Description("Only one (open: NOT in State ABGELEHNT|AKZEPTIIERT) Aenderungsantrag should be allowed")
    void testGesuchAenderungsAntrag(){
        gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(new CreateAenderungsantragRequestDtoSpec().comment("aenderung1")
                .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb())
                .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis()))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());

        gesuchTrancheApiSpec.createAenderungsantrag()
            .gesuchIdPath(gesuch.getId())
            .body(new CreateAenderungsantragRequestDtoSpec().comment("aenderung2")
                .start(gesuch.getGesuchTrancheToWorkWith().getGueltigAb())
                .end(gesuch.getGesuchTrancheToWorkWith().getGueltigBis()))
            . execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    // todo KSTIP-XXXX: a Aenderung should be accepted/denied by an SB
}
