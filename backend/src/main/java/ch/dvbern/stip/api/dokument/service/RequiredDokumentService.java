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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDocumentsProducer> requiredDocumentProducers;
    private final Instance<RequiredCustomDocumentsProducer> requiredCustomDocumentProducers;

    private static List<DokumentTyp> getExistingDokumentTypesForGesuch(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                dokument -> !dokument.getDokumente().isEmpty()
                && Objects.nonNull(dokument.getDokumentTyp())
            )
            .map(GesuchDokument::getDokumentTyp)
            .toList();
    }

    private static List<CustomDokumentTyp> getExistingCustomDokumentTypesForGesuch(final GesuchTranche tranche) {
        return tranche.getGesuchDokuments()
            .stream()
            .filter(dokument -> !dokument.getDokumente().isEmpty() && Objects.nonNull(dokument.getCustomDokumentTyp()))
            .map(GesuchDokument::getCustomDokumentTyp)
            .toList();
    }

    private Set<DokumentTyp> getDokumentTypesWithNoFilesAttached(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> !gesuchDokument.getStatus().equals(Dokumentstatus.AKZEPTIERT)
                && Objects.isNull(gesuchDokument.getCustomDokumentTyp()) && gesuchDokument.getDokumente().isEmpty()
            )
            .map(GesuchDokument::getDokumentTyp)
            .collect(Collectors.toSet());
    }

    public boolean isGesuchDokumentRequired(final GesuchDokument gesuchDokument) {
        final var tranche = gesuchDokument.getGesuchTranche();
        final var requiredNormalDocuments = getRequiredDokumentTypesForGesuch(tranche.getGesuchFormular());
        final var requiredCustomDocuments = getRequiredCustomDokumentsForGesuchFormular(tranche);
        if (Objects.isNull(gesuchDokument.getCustomDokumentTyp())) {
            return requiredNormalDocuments.contains(gesuchDokument.getDokumentTyp());
        }

        return requiredCustomDocuments.contains(gesuchDokument.getCustomDokumentTyp());
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

    private Set<CustomDokumentTyp> getRequiredCustomDokumentTypesForGesuch(final GesuchTranche tranche) {
        return requiredCustomDocumentProducers
            .stream()
            .map(requiredDocumentProducer -> requiredDocumentProducer.getRequiredDocuments(tranche))
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
        final var notAcceptedDokumentTypes =
            getDokumentTypesWithNoFilesAttached(formular);

        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
                || notAcceptedDokumentTypes.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<CustomDokumentTyp> getRequiredCustomDokumentsForGesuchFormular(final GesuchTranche tranche) {
        final var existingDokumentTypesHashSet = new HashSet<>(
            tranche
                .getGesuchDokuments()
        );

        final var requiredDokumentTypes = getRequiredCustomDokumentTypesForGesuch(tranche);

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

        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                existingDokument -> superfluousDokumentTypesHashSet.contains(existingDokument.getDokumentTyp())
            )
            .toList();
    }
}
