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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.common.validation.RequiredCustomDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.util.RequiredDokumentUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDokumentsProducer> requiredDokumentProducers;
    private final Instance<RequiredRefDokumentsProducer> requiredRefDokumentProducers;
    private final Instance<RequiredCustomDokumentsProducer> requiredCustomDokumentProducers;
    private final SozialdienstService sozialdienstService;

    public boolean getGSCanFehlendeDokumenteEinreichen(
        final Gesuch gesuch,
        final Benutzer benutzer
    ) {
        if (
            !AuthorizerUtil
                .canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(gesuch, benutzer, sozialdienstService)
        ) {
            return false;
        }
        if (
            (gesuch.getGesuchStatus() != Gesuchstatus.FEHLENDE_DOKUMENTE)
            && gesuch.getGesuchTranchen()
                .stream()
                .noneMatch(gesuchTranche -> gesuchTranche.getStatus() == GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
        ) {
            return false;
        }

        var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);
        return !isAnyDocumentStillRequired;
    }

    private boolean isAnyDocumentStillRequired(final Gesuch gesuch) {
        return gesuch.getGesuchTranchen().stream().anyMatch(gesuchTranche -> {
            var customDokumentsStillRequired = !getRequiredCustomDokumentsForGesuchFormular(gesuchTranche).isEmpty();
            var gesuchDokumenteStillRequired =
                !getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular()).isEmpty();
            // if any normal or custom GesuchDokument is still required,
            return (customDokumentsStillRequired || gesuchDokumenteStillRequired);
        });
    }

    public boolean getSBCanFehlendeDokumenteUebermitteln(final Gesuch gesuch) {
        if (
            (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB)
            && gesuch.getGesuchTranchen()
                .stream()
                .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
                .noneMatch(gesuchTranche -> gesuchTranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
        ) {
            return false;
        }

        final var containsAenderungenPendingOnGs =
            gesuch.getGesuchTranchen()
                .stream()
                .filter(tranche -> tranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
                .anyMatch(
                    tranche -> GesuchTrancheStatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT.contains(tranche.getStatus())
                );

        if (containsAenderungenPendingOnGs) {
            return false;
        }

        final var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch);

        // GesuchDokuments in status AUSSTEHEND with files attached of Tranchen that are Typ Tranche or Typ aenderung in
        // status Ueberpruefen
        final var containsUnprocessedGesuchDokuments =
            gesuch.getGesuchTranchen()
                .stream()
                .filter(
                    gesuchTranche -> !(gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                    && gesuchTranche.getStatus() != GesuchTrancheStatus.UEBERPRUEFEN)
                )
                .anyMatch(RequiredDokumentUtil::containsAusstehendeDokumenteWithFiles);

        final var containsAbgelehnteGesuchDokumente = gesuch.getGesuchTranchen()
            .stream()
            .anyMatch(RequiredDokumentUtil::containsAbgelehnteDokumente);

        final var shouldFehlendeDokumenteUebermitteln =
            isAnyDocumentStillRequired
            || containsAbgelehnteGesuchDokumente;

        return shouldFehlendeDokumenteUebermitteln && !containsUnprocessedGesuchDokuments;
    }

    public boolean getSBCanBearbeitungAbschliessen(final Gesuch gesuch) {
        final var allExistingDocumentsAccepted = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(RequiredDokumentUtil::allGesuchDokumentsAreAcceptedInTranche);
        final var noRequiredDokumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(tranche -> getRequiredDokumentsForGesuchFormular(tranche.getGesuchFormular()).isEmpty());
        final var noCustomRequiredDokumentsExisting = gesuch.getGesuchTranchen()
            .stream()
            .allMatch(tranche -> getRequiredCustomDokumentsForGesuchFormular(tranche).isEmpty());
        return allExistingDocumentsAccepted && noRequiredDokumentsExisting && noCustomRequiredDokumentsExisting;
    }

    public boolean isGesuchDokumentRequired(final GesuchDokument gesuchDokument) {
        final var tranche = gesuchDokument.getGesuchTranche();
        final var isRefDokument = Objects.isNull(gesuchDokument.getEntryId());
        final var isCustomDokument = Objects.isNull(gesuchDokument.getCustomDokumentTyp());

        if (isRefDokument) {
            final var requiredListDocuments = RequiredDokumentUtil
                .getRequiredListDokumentRefsForGesuch(tranche.getGesuchFormular(), requiredRefDokumentProducers);

            return requiredListDocuments.stream()
                .anyMatch(
                    pair -> gesuchDokument.getDokumentTyp().equals(pair.getLeft())
                    && gesuchDokument.getEntryId().equals(pair.getRight())
                );
        }

        if (isCustomDokument) {
            final var requiredCustomDocuments = getRequiredCustomDokumentsForGesuchFormular(tranche);
            return requiredCustomDocuments.contains(gesuchDokument.getCustomDokumentTyp());
        }

        final var requiredNormalDokuments = RequiredDokumentUtil
            .getRequiredDokumentTypesForGesuch(tranche.getGesuchFormular(), requiredDokumentProducers);
        return requiredNormalDokuments.contains(gesuchDokument.getDokumentTyp());
    }

    public List<DokumentTyp> getRequiredDokumentsForGesuchFormular(final GesuchFormular formular) {
        final var uploadedDocumentTypes = new HashSet<>(
            RequiredDokumentUtil.getExistingGesuchDokumentTypesWithoutAttachedDokumente(formular)
        );

        final var requiredByProducers =
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDokumentProducers);
        final var ausstehendWithMissingFiles =
            RequiredDokumentUtil.getAusstehendeDokumentTypesWithNoFilesAttached(formular);

        // check if :
        // * for each producer entry, there is an uploaded GesuchDokument
        // * there is no empty GesuchDokument listed in the producer
        // if opposite, this GesuchDokument is listed as still required
        return requiredByProducers
            .stream()
            .filter(
                requiredDokumentType -> !uploadedDocumentTypes.contains(requiredDokumentType)
                || ausstehendWithMissingFiles.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<Pair<DokumentTyp, UUID>> getRequiredDokumentRefsForGesuchFormular(final GesuchFormular formular) {
        final var uploadedDocumentRefs = new HashSet<>(
            RequiredDokumentUtil.getExistingGesuchDokumentRefsWithoutAttachedDokumente(formular)
        );

        final var requiredRefsByProducers =
            RequiredDokumentUtil.getRequiredListDokumentRefsForGesuch(formular, requiredRefDokumentProducers);
        final var ausstehendWithMissingFiles =
            RequiredDokumentUtil.getAusstehendeDokumentRefsWithNoFilesAttached(formular);

        // check if :
        // * for each producer entry, there is an uploaded GesuchDokument
        // * there is no empty GesuchDokument listed in the producer
        // if opposite, this GesuchDokument is listed as still required
        return requiredRefsByProducers
            .stream()
            .filter(
                pair -> !uploadedDocumentRefs.contains(pair)
                || ausstehendWithMissingFiles.contains(pair)
            )
            .toList();
    }

    public Map<String, Set<Pair<DokumentTyp, UUID>>> getRequiredDokumentRefMap(final GesuchFormular formular) {
        return requiredRefDokumentProducers
            .stream()
            .map(producer -> producer.getRequiredDokuments(formular))
            .collect(Collectors.toUnmodifiableMap(Pair::getLeft, Pair::getRight));
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
            RequiredDokumentUtil.getRequiredCustomDokumentTypesForGesuch(tranche, requiredCustomDokumentProducers);

        // check if there is any mismatch / still missing GesuchDokument
        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
            )
            .toList();
    }

    public List<GesuchDokument> getSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        final var existingDokumentRefs = RequiredDokumentUtil.getExistingGesuchDokumentTypes(formular);

        final var requiredDokumentTypesHashSet = new HashSet<>(
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDokumentProducers)
        );
        final var requiredDokumentRefHashSet = new HashSet<>(
            RequiredDokumentUtil.getRequiredListDokumentRefsForGesuch(formular, requiredRefDokumentProducers)
        );

        final var superfluousDokumentTypesSet = existingDokumentRefs
            .stream()
            .filter(
                existingDokumentRef -> !requiredDokumentTypesHashSet.contains(existingDokumentRef.getLeft())
                && !requiredDokumentRefHashSet.contains(existingDokumentRef)
            )
            .collect(Collectors.toSet());

        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                existingDokument -> superfluousDokumentTypesSet.contains(existingDokument.getReference())
            )
            .toList();
    }
}
