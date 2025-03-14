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
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.util.RequiredDokumentUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDocumentsProducer> requiredDocumentProducers;
    private final Instance<RequiredCustomDocumentsProducer> requiredCustomDocumentProducers;
    private final GesuchService gesuchService;

    public boolean getGSCanFehlendeDokumenteEinreichen(final Gesuch gesuch) {
        if (gesuch.getGesuchStatus() != Gesuchstatus.FEHLENDE_DOKUMENTE) {
            return false;
        }
        var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);
        return !isAnyDocumentStillRequired;
    }

    private boolean isAnyDocumentStillRequired(final Gesuch gesuch) {
        return gesuch.getGesuchTranchen().stream().map(gesuchTranche -> {
            var customDokumentsStillRequired = !getRequiredCustomDokumentsForGesuchFormular(gesuchTranche).isEmpty();
            var gesuchDokumenteStilRequired =
                !getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular()).isEmpty();
            // if any normal or custom GesuchDokument is still required,
            // the returned flag must be false
            return !(customDokumentsStillRequired || gesuchDokumenteStilRequired);
        }).toList().stream().anyMatch(containsRequiredDocuments -> !containsRequiredDocuments);
    }

    public boolean getSBCanFehlendeDokumenteUebermitteln(final Gesuch gesuch) {
        if (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB) {
            return false;
        }
        final var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);

        final var containsAusstehendeGesuchDokumenteWithoutFiles =
            gesuch.getGesuchTranchen()
                .stream()
                .anyMatch(RequiredDokumentUtil::containsAusstehendeDokumenteWithNoFiles);

        final var containsAbgelehnteGesuchDokumente = gesuch.getGesuchTranchen()
            .stream()
            .anyMatch(RequiredDokumentUtil::containsAbgelehnteDokumente);

        final var containsAenderungenNOTInStateUeberpruefen =
            RequiredDokumentUtil.containsAenderungNOTInTrancheStatus(gesuch, GesuchTrancheStatus.UEBERPRUEFEN);

        if (containsAenderungenNOTInStateUeberpruefen) {
            return false;
        }

        return isAnyDocumentStillRequired || containsAusstehendeGesuchDokumenteWithoutFiles
        || containsAbgelehnteGesuchDokumente;
    }

    public boolean getSBCanBearbeitungAbschliessen(final Gesuch gesuch) {
        final var allExistingDocumentsAccepted = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(RequiredDokumentUtil::allGesuchDokumentsAreAcceptedInTranche);
        final var noRequiredDocumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(tranche -> getRequiredDokumentsForGesuchFormular(tranche.getGesuchFormular()).isEmpty());
        final var noCustomRequiredDocumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(tranche -> getRequiredCustomDokumentsForGesuchFormular(tranche).isEmpty());
        try {
            validateBearbeitungAbschliessenForAllTranchen(gesuch);

        } catch (ValidationsException e) {
            return false;
        }
        return allExistingDocumentsAccepted && noRequiredDocumentsExisting && noCustomRequiredDocumentsExisting;
    }

    public void validateBearbeitungAbschliessenForAllTranchen(final Gesuch gesuch) {
        gesuch.getGesuchTranchen().forEach(tranche -> {
            gesuchService.validateBearbeitungAbschliessen(tranche.getId());
        });
    }

    public boolean isGesuchDokumentRequired(final GesuchDokument gesuchDokument) {
        final var tranche = gesuchDokument.getGesuchTranche();
        final var requiredNormalDocuments = RequiredDokumentUtil
            .getRequiredDokumentTypesForGesuch(tranche.getGesuchFormular(), requiredDocumentProducers);
        final var requiredCustomDocuments = getRequiredCustomDokumentsForGesuchFormular(tranche);
        if (Objects.isNull(gesuchDokument.getCustomDokumentTyp())) {
            return requiredNormalDocuments.contains(gesuchDokument.getDokumentTyp());
        }

        return requiredCustomDocuments.contains(gesuchDokument.getCustomDokumentTyp());
    }

    public List<DokumentTyp> getRequiredDokumentsForGesuchFormular(final GesuchFormular formular) {
        final var existingDokumentTypesHashSet = new HashSet<>(
            RequiredDokumentUtil.getExistingDokumentTypesForGesuch(formular)
        );

        final var requiredDokumentTypes =
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDocumentProducers);
        final var notAcceptedDokumentTypes =
            RequiredDokumentUtil.getDokumentTypesWithNoFilesAttached(formular);

        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
                || notAcceptedDokumentTypes.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<CustomDokumentTyp> getRequiredCustomDokumentsForGesuchFormular(final GesuchTranche tranche) {
        // get existing GesuchDokumente of current tranche
        final var existingDokumentTypesHashSet = tranche
            .getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> !gesuchDokument.getDokumente().isEmpty())
            .map(GesuchDokument::getCustomDokumentTyp)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));

        // get required GesuchDokumente of current tranche
        final var requiredDokumentTypes =
            RequiredDokumentUtil.getRequiredCustomDokumentTypesForGesuch(tranche, requiredCustomDocumentProducers);

        // check if there is any mismatch / still missing GesuchDokument
        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<GesuchDokument> getSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        final var existingDokumentTypes = RequiredDokumentUtil.getExistingDokumentTypesForGesuch(formular);

        final var requiredDokumentTypesHashSet = new HashSet<>(
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDocumentProducers)
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
