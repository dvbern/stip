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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.validation.RequiredCustomDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class RequiredDokumentUtil {
    private Stream<GesuchDokument> getExistingGesuchDokumentBaseWithoutAttachedDokumenteStream(
        final GesuchFormular formular
    ) {
        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                dokument -> !dokument.getDokumente().isEmpty()
                && Objects.nonNull(dokument.getDokumentTyp())
            );
    }

    public List<DokumentTyp> getExistingGesuchDokumentTypesWithoutAttachedDokumente(final GesuchFormular formular) {
        return getExistingGesuchDokumentBaseWithoutAttachedDokumenteStream(formular)
            .map(GesuchDokument::getDokumentTyp)
            .toList();
    }

    public List<Pair<DokumentTyp, UUID>> getExistingGesuchDokumentRefsWithoutAttachedDokumente(
        final GesuchFormular formular
    ) {
        return getExistingGesuchDokumentBaseWithoutAttachedDokumenteStream(formular)
            .map(GesuchDokument::getReference)
            .toList();
    }

    public List<Pair<DokumentTyp, UUID>> getExistingGesuchDokumentTypes(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> Objects.nonNull(gesuchDokument.getDokumentTyp()))
            .map(GesuchDokument::getReference)
            .toList();
    }

    private Stream<GesuchDokument> getAusstehendeDokumentStreamWithNoFilesAttached(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> !gesuchDokument.getStatus().equals(GesuchDokumentStatus.AKZEPTIERT)
                && Objects.isNull(gesuchDokument.getCustomDokumentTyp()) && gesuchDokument.getDokumente().isEmpty()
            );
    }

    public Set<DokumentTyp> getAusstehendeDokumentTypesWithNoFilesAttached(final GesuchFormular formular) {
        return getAusstehendeDokumentStreamWithNoFilesAttached(formular).map(GesuchDokument::getDokumentTyp)
            .collect(Collectors.toSet());
    }

    public Set<Pair<DokumentTyp, UUID>> getAusstehendeDokumentRefsWithNoFilesAttached(final GesuchFormular formular) {
        return getAusstehendeDokumentStreamWithNoFilesAttached(formular)
            .map(GesuchDokument::getReference)
            .collect(Collectors.toSet());
    }

    public Set<DokumentTyp> getRequiredDokumentTypesForGesuch(
        final GesuchFormular formular,
        final Instance<RequiredDokumentsProducer> requiredDokumentProducers,
        final boolean includeHidden
    ) {
        return requiredDokumentProducers
            .stream()
            .map(requiredDokumentProducer -> requiredDokumentProducer.getRequiredDokuments(formular, includeHidden))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public Set<Pair<DokumentTyp, UUID>> getRequiredListDokumentRefsForGesuch(
        final GesuchFormular formular,
        final Instance<RequiredRefDokumentsProducer> requiredRefDokumentProducers,
        final boolean includeHidden
    ) {
        return requiredRefDokumentProducers
            .stream()
            .map(producer -> producer.getRequiredDokuments(formular, includeHidden))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public Set<CustomDokumentTyp> getRequiredCustomDokumentTypesForGesuch(
        final GesuchTranche tranche,
        final Instance<RequiredCustomDokumentsProducer> requiredCustomDokumentProducers
    ) {
        return requiredCustomDokumentProducers
            .stream()
            .map(requiredDokumentProducer -> requiredDokumentProducer.getRequiredDokuments(tranche))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            )
            .collect(Collectors.toSet());
    }

    public boolean containsAusstehendeDokumenteWithFiles(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(GesuchDokumentStatus.AUSSTEHEND)
                && !gesuchDokument.getDokumente().isEmpty()
            )
            .count() > 0;
    }

    public boolean containsAbgelehnteDokumente(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> gesuchDokument.getStatus().equals(GesuchDokumentStatus.ABGELEHNT)
            )
            .count() > 0;
    }

    public boolean allGesuchDokumentsAreAcceptedInTranche(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuchDokuments()
            .stream()
            .allMatch(gesuchDokument -> gesuchDokument.getStatus().equals(GesuchDokumentStatus.AKZEPTIERT));
    }
}
