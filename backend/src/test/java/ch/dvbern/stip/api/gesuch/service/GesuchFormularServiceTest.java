package ch.dvbern.stip.api.gesuch.service;

import java.util.List;

import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchFormularServiceTest {
    @Inject
    GesuchFormularService gesuchFormularService;

    @BeforeAll
    void setup() {
        final var requiredDokumentServiceMock = Mockito.mock(RequiredDokumentService.class);
        Mockito.when(requiredDokumentServiceMock.getSuperfluousDokumentsForGesuch(any())).thenReturn(List.of());
        Mockito.when(requiredDokumentServiceMock.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        QuarkusMock.installMockForType(requiredDokumentServiceMock, RequiredDokumentService.class);
    }

    @Test
    void pageValidation() {
        final var gesuch = new Gesuch();
        final var gesuchTranche = new GesuchTranche();
        final var gesuchFormular = new GesuchFormular();
        gesuchTranche.setGesuch(gesuch);
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchFormular.setTranche(gesuchTranche);
        var reportDto = gesuchFormularService.validatePages(gesuchFormular);
        assertThat(reportDto.getValidationErrors(), Matchers.is(empty()));

        gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
        reportDto = gesuchFormularService.validatePages(gesuchFormular);
        var violationCount = reportDto.getValidationErrors().size();
        assertThat(reportDto.getValidationErrors(), Matchers.is(not(empty())));

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternteilUnbekanntVerstorben(true)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
        );
        reportDto = gesuchFormularService.validatePages(gesuchFormular);
        assertThat(reportDto.getValidationErrors().size(), Matchers.is(greaterThan(violationCount)));
    }
}
