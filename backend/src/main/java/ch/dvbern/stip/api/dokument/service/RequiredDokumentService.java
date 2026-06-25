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

        var isAnyDocumentStillRequired = isAnyDocumentStillRequired(gesuch, false);
        return !isAnyDocumentStillRequired;
    }

    private boolean isAnyDocumentStillRequired(final Gesuch gesuch, final boolean includeHidden) {
        return gesuch.getGesuchTranchen()
            .stream()
            .anyMatch(gesuchTranche -> isAnyDocumentStillRequired(gesuchTranche, includeHidden));
    }

    private boolean isAnyDocumentStillRequired(final GesuchTranche gesuchTranche, final boolean includeHidden) {
        var customDokumentsStillRequired = !getRequiredCustomDokumentsForGesuchFormular(gesuchTranche).isEmpty();
        var gesuchDokumenteStillRequired =
            !getRequiredDokumentsForGesuchFormular(gesuchTranche.getGesuchFormular(), includeHidden).isEmpty();
        // if any normal or custom GesuchDokument is still required,
        return (customDokumentsStillRequired || gesuchDokumenteStillRequired);
    }

    public boolean getSBCanFehlendeDokumenteUebermitteln(final GesuchTranche aenderung) {
        if (
            aenderung.getTyp() != GesuchTrancheTyp.AENDERUNG
            || aenderung.getStatus() != GesuchTrancheStatus.UEBERPRUEFEN
        ) {
            return false;
        }

        final var containsUnprocessedGesuchDokuments =
            RequiredDokumentUtil.containsAusstehendeDokumenteWithFiles(aenderung);
        final var containsAbgelehnteGesuchDokumente = RequiredDokumentUtil.containsAbgelehnteDokumente(aenderung);

        final var shouldFehlendeDokumenteUebermitteln =
            isAnyDocumentStillRequired(aenderung, false)
            || containsAbgelehnteGesuchDokumente;

        return shouldFehlendeDokumenteUebermitteln && !containsUnprocessedGesuchDokuments;
    }

    public boolean getSBCanFehlendeDokumenteUebermitteln(final Gesuch gesuch) {
        if (gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB) {
            return false;
        }

        // GesuchDokuments in status AUSSTEHEND with files attached of Tranchen that are Typ Tranche
        final var containsUnprocessedGesuchDokuments =
            gesuch.getTranchenTranchen()
                .anyMatch(RequiredDokumentUtil::containsAusstehendeDokumenteWithFiles);

        final var containsAbgelehnteGesuchDokumente = gesuch.getTranchenTranchen()
            .anyMatch(RequiredDokumentUtil::containsAbgelehnteDokumente);

        final var shouldFehlendeDokumenteUebermitteln =
            isAnyDocumentStillRequired(gesuch, false)
            || containsAbgelehnteGesuchDokumente;

        return shouldFehlendeDokumenteUebermitteln && !containsUnprocessedGesuchDokuments;
    }

    public boolean getSBCanBearbeitungAbschliessen(final Gesuch gesuch) {
        final var allExistingDocumentsAccepted = gesuch.getTranchenTranchen()
            .allMatch(RequiredDokumentUtil::allGesuchDokumentsAreAcceptedInTranche);
        final var noRequiredDokumentsExisting = gesuch.getTranchenTranchen()
            .allMatch(tranche -> getRequiredDokumentsForGesuchFormular(tranche.getGesuchFormular(), true).isEmpty());
        final var noRequiredRefDokumentsExisting = gesuch.getTranchenTranchen()
            .allMatch(tranche -> getRequiredDokumentRefsForGesuchFormular(tranche.getGesuchFormular(), true).isEmpty());
        final var noCustomRequiredDokumentsExisting = gesuch.getTranchenTranchen()
            .allMatch(tranche -> getRequiredCustomDokumentsForGesuchFormular(tranche).isEmpty());
        return allExistingDocumentsAccepted && noRequiredDokumentsExisting && noRequiredRefDokumentsExisting && noCustomRequiredDokumentsExisting;
    }

    public boolean isGesuchDokumentRequired(final GesuchDokument gesuchDokument) {
        final var tranche = gesuchDokument.getGesuchTranche();
        final var isRefDokument = Objects.isNull(gesuchDokument.getEntryId());
        final var isCustomDokument = Objects.isNull(gesuchDokument.getCustomDokumentTyp());

        if (isRefDokument) {
            final var requiredListDocuments = RequiredDokumentUtil
                .getRequiredListDokumentRefsForGesuch(tranche.getGesuchFormular(), requiredRefDokumentProducers, true);

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
            .getRequiredDokumentTypesForGesuch(tranche.getGesuchFormular(), requiredDokumentProducers, true);
        return requiredNormalDokuments.contains(gesuchDokument.getDokumentTyp());
    }

    public List<DokumentTyp> getRequiredDokumentsForGesuchFormular(
        final GesuchFormular formular,
        final boolean includeHidden
    ) {
        final var uploadedDocumentTypes = new HashSet<>(
            RequiredDokumentUtil.getExistingGesuchDokumentTypesWithoutAttachedDokumente(formular)
        );

        final var requiredByProducers =
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDokumentProducers, includeHidden);
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

    public List<Pair<DokumentTyp, UUID>> getRequiredDokumentRefsForGesuchFormular(
        final GesuchFormular formular,
        final boolean includeHidden
    ) {
        final var uploadedDocumentRefs = new HashSet<>(
            RequiredDokumentUtil.getExistingGesuchDokumentRefsWithoutAttachedDokumente(formular)
        );

        final var requiredRefsByProducers =
            RequiredDokumentUtil
                .getRequiredListDokumentRefsForGesuch(formular, requiredRefDokumentProducers, includeHidden);
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

    public Map<String, Set<Pair<DokumentTyp, UUID>>> getRequiredDokumentRefMap(
        final GesuchFormular formular,
        final boolean includeHidden
    ) {
        return requiredRefDokumentProducers
            .stream()
            .map(producer -> producer.getRequiredDokuments(formular, includeHidden))
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
            RequiredDokumentUtil.getRequiredDokumentTypesForGesuch(formular, requiredDokumentProducers, true)
        );
        final var requiredDokumentRefHashSet = new HashSet<>(
            RequiredDokumentUtil.getRequiredListDokumentRefsForGesuch(formular, requiredRefDokumentProducers, true)
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
