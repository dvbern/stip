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

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

class RequiredDokumentServiceTest {
    @Test
    void setGSCanFehlendeDokumenteEinreichenToFalseForGSIfDelegated() {
        // arrange
        final var sozialdienstService = Mockito.mock(SozialdienstService.class);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(Mockito.any())).thenReturn(true);

        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer())), null, sozialdienstService
        );

        // setup gs benutzer
        var gsBenutzer = new Benutzer();
        var gsRole = new Rolle();
        gsRole.setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        gsBenutzer.setRollen(Set.of(gsRole));
        // setup gesuch with delegierung
        var gesuch = new Gesuch();
        var fall = new Fall();
        fall.setGesuchsteller(gsBenutzer);
        var ausbildung = new Ausbildung();
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        var delegierung = new Delegierung();
        delegierung.setSozialdienst(new Sozialdienst());
        gesuch.getAusbildung().getFall().setDelegierung(delegierung);

        // act & assert
        assertThat(service.getGSCanFehlendeDokumenteEinreichen(gesuch, gsBenutzer), is(false));
    }

    @Test
    void setGSCanFehlendeDokumenteEinreichenToTrueForSozialdienstmitarbeiterIfDelegated() {
        // arrange
        final var sozialdienstService = Mockito.mock(SozialdienstService.class);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(Mockito.any())).thenReturn(true);

        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer())), null, sozialdienstService
        );

        // setup soz-ma benutzer
        var sozMaBenutzer = new Benutzer();
        var sozMaRole = new Rolle();
        sozMaRole.setKeycloakIdentifier(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER);
        sozMaBenutzer.setRollen(Set.of(sozMaRole));
        // setup gesuch with delegierung
        var gesuch = new Gesuch();
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        var fall = new Fall();
        fall.setGesuchsteller(sozMaBenutzer);
        var ausbildung = new Ausbildung();
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        var delegierung = new Delegierung();
        delegierung.setSozialdienst(new Sozialdienst());
        gesuch.getAusbildung().getFall().setDelegierung(delegierung);

        // act & assert
        assertThat(service.getGSCanFehlendeDokumenteEinreichen(gesuch, sozMaBenutzer), is(true));
    }

    @Test
    void getRequiredDokumentsForGesuchFormularTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer())), null, null
        );
        final var requiredDocuments = service.getRequiredDokumentsForGesuchFormular(initFormular(List.of()));

        assertThat(requiredDocuments.size(), is(1));
        assertThat(requiredDocuments.get(0), is(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG));
    }

    @Test
    void getEmptyListTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockEmptyDocumentProducer())),
            null, null
        );
        final var requiredDocuments = service.getRequiredDokumentsForGesuchFormular(initFormular(List.of()));

        assertThat(requiredDocuments.size(), is(0));
    }

    @Test
    void oneExistingTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer())),
            null, null
        );
        final var requiredDocuments = service
            .getRequiredDokumentsForGesuchFormular(initFormular(List.of(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG)));

        assertThat(requiredDocuments.size(), is(0));
    }

    private GesuchFormular initFormular(final List<DokumentTyp> existingTypes) {
        return new GesuchFormular().setTranche(
            new GesuchTranche().setGesuch(
                new Gesuch()
            )
                .setGesuchDokuments(
                    existingTypes.stream()
                        .map(x -> new GesuchDokument().setDokumentTyp(x).setDokumente(List.of(new Dokument())))
                        .toList()
                )
        );
    }

    public static class MockDocumentProducer implements RequiredDocumentsProducer {
        @Override
        public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            return ImmutablePair.of("mock", Set.of(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG));
        }
    }

    static class MockEmptyDocumentProducer implements RequiredDocumentsProducer {
        @Override
        public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            return ImmutablePair.of("", Set.of());
        }
    }

    public static class MockInstance implements Instance<RequiredDocumentsProducer> {
        private final List<RequiredDocumentsProducer> collection;

        MockInstance(List<RequiredDocumentsProducer> collection) {
            this.collection = collection;
        }

        @Override
        public Stream<RequiredDocumentsProducer> stream() {
            return collection.stream();
        }

        @Override
        public Instance<RequiredDocumentsProducer> select(Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends RequiredDocumentsProducer> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends RequiredDocumentsProducer> Instance<U> select(
            TypeLiteral<U> subtype,
            Annotation... qualifiers
        ) {
            return null;
        }

        @Override
        public boolean isUnsatisfied() {
            return false;
        }

        @Override
        public boolean isAmbiguous() {
            return false;
        }

        @Override
        public void destroy(RequiredDocumentsProducer instance) {

        }

        @Override
        public Handle<RequiredDocumentsProducer> getHandle() {
            return null;
        }

        @Override
        public Iterable<? extends Handle<RequiredDocumentsProducer>> handles() {
            return null;
        }

        @Override
        public RequiredDocumentsProducer get() {
            return null;
        }

        @NotNull
        @Override
        public Iterator<RequiredDocumentsProducer> iterator() {
            return null;
        }
    }
}
