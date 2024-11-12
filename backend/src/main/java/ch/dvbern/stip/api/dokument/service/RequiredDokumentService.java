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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDocumentProducer> requiredDocumentProducers;

    private static List<GesuchDokument> getExistingDokumentsForGesuch(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments();
    }

    private static List<DokumentTyp> getExistingDokumentTypesForGesuch(final GesuchFormular formular) {
        return getExistingDokumentsForGesuch(formular)
            .stream()
            .filter(dokument -> !dokument.getDokumente().isEmpty())
            .map(GesuchDokument::getDokumentTyp)
            .toList();
    }

    private Set<DokumentTyp> getRequiredDokumentTypesForGesuch(final GesuchFormular formular) {
        return requiredDocumentProducers
            .stream()
            .map(requiredDocumentProducer -> requiredDocumentProducer.getRequiredDocuments(formular))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public List<DokumentTyp> getRequiredDokumentsForGesuchFormular(final GesuchFormular formular) {
        final var existingDokumentTypesHashSet = new HashSet<>(
            getExistingDokumentTypesForGesuch(formular)
        );

        final var requiredDokumentTypes = getRequiredDokumentTypesForGesuch(formular);

        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<GesuchDokument> getSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        final var existingDokumentTypes = getExistingDokumentTypesForGesuch(formular);

        final var requiredDokumentTypesHashSet = new HashSet<>(
            getRequiredDokumentTypesForGesuch(formular)
        );

        final var superfluousDokumentTypes = existingDokumentTypes
            .stream()
            .filter(
                existingDokumentType -> !requiredDokumentTypesHashSet.contains(existingDokumentType)
            )
            .toList();
        final var superfluousDokumentTypesHashSet = new HashSet<>(superfluousDokumentTypes);

        return getExistingDokumentsForGesuch(formular)
            .stream()
            .filter(
                existingDokument -> superfluousDokumentTypesHashSet.contains(existingDokument.getDokumentTyp())
            )
            .toList();
    }
}
