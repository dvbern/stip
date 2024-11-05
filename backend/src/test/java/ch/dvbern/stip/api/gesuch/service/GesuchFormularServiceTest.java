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
