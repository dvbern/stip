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

package ch.dvbern.stip.api.gesuchformular.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.CustomDocumentsRequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchFormularServiceTest {
    @Inject
    GesuchFormularService gesuchFormularService;
    @InjectMock
    RequiredDokumentService requiredDokumentServiceMock;
    @InjectMock
    CustomDocumentsRequiredDocumentProducer customDocumentsRequiredDocumentProducerMock;

    @BeforeAll
    void setup() {
        when(requiredDokumentServiceMock.getSuperfluousDokumentsForGesuch(any())).thenReturn(List.of());
        when(requiredDokumentServiceMock.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentServiceMock.getRequiredCustomDokumentsForGesuchFormular(any()))
            .thenReturn(List.of());
        QuarkusMock.installMockForType(requiredDokumentServiceMock, RequiredDokumentService.class);
    }

    @Test
    void pageValidation() {
        final var gesuch = new Gesuch();
        var gesuchTranche = new GesuchTranche();
        gesuchTranche.setId(UUID.randomUUID());
        final var gesuchFormular = new GesuchFormular();
        gesuchTranche.setGesuch(gesuch);
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchTranche.setGesuchFormular(gesuchFormular);
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

    @Test
    void validatePagesDokumenteTest() {
        // arrange
        final var gesuch = new Gesuch();
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        var tranche = new GesuchTranche();
        var gesuchFormular = new GesuchFormular();
        tranche.setTyp(GesuchTrancheTyp.TRANCHE);
        tranche.setGesuch(gesuch);
        gesuchFormular.setTranche(tranche);
        tranche.setGesuchFormular(gesuchFormular);

        /* case there are no required documents */
        when(customDocumentsRequiredDocumentProducerMock.getRequiredDocuments(any()))
            .thenReturn(ImmutablePair.of("", Set.of()));
        when(requiredDokumentServiceMock.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentServiceMock.getRequiredCustomDokumentsForGesuchFormular(any()))
            .thenReturn(List.of());
        // act
        var reportDto = gesuchFormularService.validatePages(gesuchFormular);
        // assert
        assertThat(reportDto.getValidationWarnings().size(), Matchers.is(0));

        /* case there are required custom dokuments */
        var customDokumentTyp = new CustomDokumentTyp();
        var customDokument = new GesuchDokument();
        customDokument.setGesuchTranche(tranche);
        customDokument.setCustomDokumentTyp(customDokumentTyp);
        tranche.setGesuchDokuments(List.of(customDokument));
        when(customDocumentsRequiredDocumentProducerMock.getRequiredDocuments(any()))
            .thenReturn(ImmutablePair.of("dokuments", Set.of(customDokumentTyp)));
        when(requiredDokumentServiceMock.getRequiredCustomDokumentsForGesuchFormular(any()))
            .thenReturn(List.of(customDokumentTyp));
        // act
        reportDto = gesuchFormularService.validatePages(gesuchFormular);
        assertThat(reportDto.getValidationWarnings().size(), Matchers.is(1));
        // upload dokument
        customDokument.setDokumente(List.of(new Dokument()));
        reportDto = gesuchFormularService.validatePages(gesuchFormular);
        assertThat(reportDto.getValidationWarnings().size(), Matchers.is(0));

        /* case there are both: required custom dokuments & normal dokuments */
        // act & assert
        final var kind = new Kind();
        kind.setUnterhaltsbeitraege(10);
        gesuchFormular.setKinds(Set.of(kind));
        var gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(tranche);
        gesuchDokument.setDokumentTyp(DokumentTyp.KINDER_ALIMENTENVERORDUNG);
        gesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        customDokument.setDokumente(List.of());
        reportDto = gesuchFormularService.validatePages(gesuchFormular);
        assertThat(reportDto.getValidationWarnings().size(), Matchers.is(2));
    }
}
