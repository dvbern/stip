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

package ch.dvbern.stip.api.dokument.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.validation.RequiredCustomDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.generator.entities.gesuch.GesuchTestBuilder;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.inject.Instance;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequiredDokumentServiceTest {
    private static final DokumentTyp REQUIRED_DOKUMENT_TYP = DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE;
    private Instance<RequiredDokumentsProducer> requiredDokumentProducers;
    private Instance<RequiredRefDokumentsProducer> requiredRefDokumentProducers;
    private Instance<RequiredCustomDokumentsProducer> requiredCustomDokumentProducers;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        RequiredDokumentsProducer mockProducer = mock(RequiredDokumentsProducer.class);
        requiredDokumentProducers = mock(Instance.class);
        requiredRefDokumentProducers = mock(Instance.class);
        requiredCustomDokumentProducers = mock(Instance.class);

        when(mockProducer.getRequiredDokuments(any(), anyBoolean()))
            .thenReturn(ImmutablePair.of("mock", Set.of(REQUIRED_DOKUMENT_TYP)));
        when(requiredDokumentProducers.stream()).thenReturn(Stream.of(mockProducer));
    }

    @Test
    void gsCannotFehlendeDokumenteUebermittelnIfDelegiert() {
        // arrange
        final var sozialdienstService = Mockito.mock(SozialdienstService.class);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        final var service = new RequiredDokumentService(
            requiredDokumentProducers, requiredRefDokumentProducers, requiredCustomDokumentProducers,
            sozialdienstService
        );

        final var gesuch = createGesuch();
        final var delegierung = new Delegierung();
        final var gsBenutzer = gesuch.getAusbildung().getFall().getGesuchsteller();

        delegierung.setSozialdienst(new Sozialdienst());
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        gesuch.getLatestGesuchTranche().setGesuchDokuments(createGesuchDokumentForTyps(REQUIRED_DOKUMENT_TYP));
        gesuch.getAusbildung().getFall().setCurrentDelegierung(delegierung).getCurrentDelegierung().akzeptieren();

        // act & assert
        assertThat(service.getGSCanFehlendeDokumenteEinreichen(gesuch, gsBenutzer), is(false));
    }

    @Test
    void sozialdienstMitarbeiterCanFehlendeDokumenteUebermittelnIfDelegiert() {
        // arrange
        final var sozialdienstService = Mockito.mock(SozialdienstService.class);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        final var service = new RequiredDokumentService(
            requiredDokumentProducers, requiredRefDokumentProducers, requiredCustomDokumentProducers,
            sozialdienstService
        );
        final var gesuch = createGesuch();
        final var delegierung = new Delegierung();
        final var gsBenutzer = gesuch.getAusbildung().getFall().getGesuchsteller();
        delegierung.setSozialdienst(new Sozialdienst());
        gesuch.getLatestGesuchTranche().setGesuchDokuments(createGesuchDokumentForTyps(REQUIRED_DOKUMENT_TYP));
        gesuch.getAusbildung().getFall().setCurrentDelegierung(delegierung).getCurrentDelegierung().akzeptieren();

        // act & assert
        assertThat(service.getGSCanFehlendeDokumenteEinreichen(gesuch, gsBenutzer), is(true));
    }

    @Test
    void returnsRequiredDokumentsIfNoneRegistered() {
        final var service = new RequiredDokumentService(
            requiredDokumentProducers, requiredRefDokumentProducers, requiredCustomDokumentProducers, null
        );
        final var requiredDokuments = service.getRequiredDokumentsForGesuchFormular(createFormular(List.of()), true);

        assertThat(requiredDokuments.size(), is(1));
    }

    @Test
    void noRequiredDokumentsIfTheRequiredTypIsRegistered() {
        final var service = new RequiredDokumentService(
            requiredDokumentProducers, requiredRefDokumentProducers, requiredCustomDokumentProducers, null
        );
        final var requiredDokuments = service
            .getRequiredDokumentsForGesuchFormular(
                createFormular(List.of(REQUIRED_DOKUMENT_TYP)),
                true
            );

        assertThat(requiredDokuments.size(), is(0));
    }

    private Gesuch createGesuch() {
        return GesuchTestBuilder.standardWithNestedDeps(LocalDate.now())
            .with(gesuch -> gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE))
            .build();
    }

    private GesuchFormular createFormular(final List<DokumentTyp> existingTypes) {
        return new GesuchFormular().setTranche(
            new GesuchTranche().setGesuch(
                new Gesuch()
            )
                .setGesuchDokuments(createGesuchDokumentForTyps(existingTypes.toArray(new DokumentTyp[] {})))
        );
    }

    private List<GesuchDokument> createGesuchDokumentForTyps(final DokumentTyp... dokumentTyps) {
        return Stream.of(dokumentTyps)
            .map(dokumentTyp -> new GesuchDokument().setDokumentTyp(dokumentTyp).setDokumente(List.of(new Dokument())))
            .toList();
    }
}
