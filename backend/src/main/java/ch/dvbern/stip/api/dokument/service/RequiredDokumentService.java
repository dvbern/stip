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

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDocumentsProducer> requiredDocumentProducers;
    private final Instance<RequiredCustomDocumentsProducer> requiredCustomDocumentProducers;
    private final GesuchService gesuchService;

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

    public boolean getGSCanFehlendeDokumenteEinreichen(final Gesuch gesuch) {
        if (gesuch.getGesuchStatus() != Gesuchstatus.FEHLENDE_DOKUMENTE) {
            return false;
        }
        /* check for any (normal | custom) required GesuchDokuments */
        var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);

        return !isAnyDocumentStillRequired;
    }

    private boolean isAnyDocumentStillRequired(final Gesuch gesuch) {
        return gesuch.getGesuchTranchen().stream().map(gesuchTranche -> {
            // check for any still required GesuchDokuments.
            var customDokumentsStillRequired = !getRequiredCustomDokumentsForGesuchFormular(gesuchTranche).isEmpty();
            var gesuchDokumenteStilRequired =
                !getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular()).isEmpty();
            // if any normal or custom GesuchDokument is still required,
            // the returned flag must be false
            if (customDokumentsStillRequired || gesuchDokumenteStilRequired) {
                return false;
            }
            return true;
        }).toList().stream().anyMatch(containsRequiredDocuments -> !containsRequiredDocuments);
    }

    // true, when all gesuchdokuments in state AUSSTEHEND contain no files
    public boolean getSBCanFehlendeDokumenteUebermitteln(final Gesuch gesuch) {
        if (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB) {
            return false;
        }
        /* check for any (normal | custom) required GesuchDokuments */
        final var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);

        // check for AUSSTEHENDE GesuchDokumente with no files
        final var containsAusstehendeGesuchDokumenteWithoutFiles =
            gesuch.getGesuchTranchen()
                .stream()
                .map(this::containsAusstehendeDokumenteWithNoFiles)
                .anyMatch(result -> result == true);

        // check for GesuchDokumente in state ABGELEHNT
        final var containsAbgelehnteGesuchDokumente = gesuch.getGesuchTranchen()
            .stream()
            .map(this::containsAbgelehnteDokumente)
            .anyMatch(result -> result == true);

        // check for AENDERUNGEN that are NOT in state UEBERPRUEFEN
        final var containsAenderungenNOTInStateUeberpruefen =
            containsAenderungNOTInTrancheStatus(gesuch, GesuchTrancheStatus.UEBERPRUEFEN);
        if (containsAenderungenNOTInStateUeberpruefen) {
            return false;
        }

        return isAnyDocumentStillRequired || containsAusstehendeGesuchDokumenteWithoutFiles
        || containsAbgelehnteGesuchDokumente;
    }

    // true when all (existing)gesuchdokuments over all tranchen are AKZEPTIERT
    public boolean getSBCanBearbeitungAbschliessen(final Gesuch gesuch) {
        final var allExistingDocumentsAccepted = gesuch.getGesuchTranchen()
            .stream()
            .map(this::allGesuchDokumentsAreAcceptedInTranche)
            .allMatch(result -> result == true);
        final var noRequiredDocumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .map(tranche -> getRequiredDokumentsForGesuchFormular(tranche.getGesuchFormular()).isEmpty())
            .allMatch(result -> result == true);
        final var noCustomRequiredDocumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .map(tranche -> getRequiredCustomDokumentsForGesuchFormular(tranche).isEmpty())
            .allMatch(result -> result == true);
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

    private boolean containsAusstehendeDokumenteWithNoFiles(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND)
                && gesuchDokument.getDokumente().isEmpty()
            )
            .count() > 0;
    }

    private boolean containsAbgelehnteDokumente(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.ABGELEHNT)
            )
            .count() > 0;
    }

    private boolean allGesuchDokumentsAreAcceptedInTranche(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .allMatch(gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AKZEPTIERT));
    }

    private boolean containsAenderungNOTInTrancheStatus(
        final Gesuch gesuch,
        final GesuchTrancheStatus trancheStatus
    ) {
        return gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getTyp().equals(GesuchTrancheTyp.AENDERUNG))
            .anyMatch(tranche -> !tranche.getStatus().equals(trancheStatus));
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
        // get existing GesuchDokumente of current tranche
        final var existingDokumentTypesHashSet = tranche
            .getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> !gesuchDokument.getDokumente().isEmpty())
            .map(GesuchDokument::getCustomDokumentTyp)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));

        // get required GesuchDokumente of current tranche
        final var requiredDokumentTypes = getRequiredCustomDokumentTypesForGesuch(tranche);

        // check if there is any mismatch / still missing GesuchDokument
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
