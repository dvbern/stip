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

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class RequiredDokumentServiceTest {
    @Test
    void getRequiredDokumentsForGesuchFormularTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer()))
        );
        final var requiredDocuments = service.getRequiredDokumentsForGesuchFormular(initFormular(List.of()));

        assertThat(requiredDocuments.size(), is(1));
        assertThat(requiredDocuments.get(0), is(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG));
    }

    @Test
    void getEmptyListTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockEmptyDocumentProducer()))
        );
        final var requiredDocuments = service.getRequiredDokumentsForGesuchFormular(initFormular(List.of()));

        assertThat(requiredDocuments.size(), is(0));
    }

    @Test
    void noExistingTest() {
        final var service = new RequiredDokumentService(
            new MockInstance(List.of(new MockDocumentProducer()))
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

    static class MockDocumentProducer extends RequiredDocumentProducer {
        @Override
        public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            return ImmutablePair.of("mock", Set.of(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG));
        }
    }

    static class MockEmptyDocumentProducer extends RequiredDocumentProducer {
        @Override
        public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            return ImmutablePair.of("", Set.of());
        }
    }

    static class MockInstance implements Instance<RequiredDocumentProducer> {
        private final List<RequiredDocumentProducer> collection;

        MockInstance(List<RequiredDocumentProducer> collection) {
            this.collection = collection;
        }

        @Override
        public Stream<RequiredDocumentProducer> stream() {
            return collection.stream();
        }

        @Override
        public Instance<RequiredDocumentProducer> select(Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends RequiredDocumentProducer> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends RequiredDocumentProducer> Instance<U> select(
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
        public void destroy(RequiredDocumentProducer instance) {

        }

        @Override
        public Handle<RequiredDocumentProducer> getHandle() {
            return null;
        }

        @Override
        public Iterable<? extends Handle<RequiredDocumentProducer>> handles() {
            return null;
        }

        @Override
        public RequiredDocumentProducer get() {
            return null;
        }

        @NotNull
        @Override
        public Iterator<RequiredDocumentProducer> iterator() {
            return null;
        }
    }
}
