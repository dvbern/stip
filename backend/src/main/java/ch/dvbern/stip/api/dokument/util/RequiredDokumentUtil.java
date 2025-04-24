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

package ch.dvbern.stip.api.dokument.util;

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
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequiredDokumentUtil {
    public List<DokumentTyp> getExistingDokumentTypesForGesuch(final GesuchFormular formular) {
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

    public Set<DokumentTyp> getAusstehendeDokumentTypesWithNoFilesAttached(final GesuchFormular formular) {
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

    public Set<DokumentTyp> getRequiredDokumentTypesForGesuch(
        final GesuchFormular formular,
        final Instance<RequiredDocumentsProducer> requiredDocumentProducers
    ) {
        return requiredDocumentProducers
            .stream()
            .map(requiredDocumentProducer -> requiredDocumentProducer.getRequiredDocuments(formular))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public Set<CustomDokumentTyp> getRequiredCustomDokumentTypesForGesuch(
        final GesuchTranche tranche,
        final Instance<RequiredCustomDocumentsProducer> requiredCustomDocumentProducers
    ) {
        return requiredCustomDocumentProducers
            .stream()
            .map(requiredDocumentProducer -> requiredDocumentProducer.getRequiredDocuments(tranche))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public boolean containsAusstehendeDokumenteWithNoFiles(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND)
                && gesuchDokument.getDokumente().isEmpty()
            )
            .count() > 0;
    }

    public boolean containsAusstehendeDokumenteWithFiles(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AUSSTEHEND)
                && !gesuchDokument.getDokumente().isEmpty()
            )
            .count() > 0;
    }

    public boolean containsAbgelehnteDokumente(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.ABGELEHNT)
            )
            .count() > 0;
    }

    public boolean allGesuchDokumentsAreAcceptedInTranche(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .allMatch(gesuchDokument -> gesuchDokument.getStatus().equals(Dokumentstatus.AKZEPTIERT));
    }
}
