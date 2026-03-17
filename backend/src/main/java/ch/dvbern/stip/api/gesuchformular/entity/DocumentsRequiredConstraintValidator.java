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

package ch.dvbern.stip.api.gesuchformular.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredCustomDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDokumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredRefDokumentsProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.util.DokumentValidationUtils;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;

@Slf4j
public class DocumentsRequiredConstraintValidator
    implements ConstraintValidator<DocumentsRequiredConstraint, GesuchFormular> {
    private static final String PAGENAME = "dokuments";
    @Inject
    Instance<RequiredDokumentsProducer> producers;
    @Inject
    Instance<RequiredRefDokumentsProducer> refProducers;

    @Inject
    Instance<RequiredCustomDokumentsProducer> customProducers;

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(producer -> producer.getRequiredDokuments(formular)).toList();
        final var requiredRefDocs =
            refProducers.stream().map(producer -> producer.getRequiredDokuments(formular)).toList();

        final var existingDokuments = getExistingRequiredGesuchDokuments(formular);
        final var existingDokumentTypMap = existingDokuments.stream()
            .filter(d -> Objects.nonNull(d.getDokumentTyp()) && Objects.isNull(d.getEntryId()))
            .collect(Collectors.toUnmodifiableMap(GesuchDokument::getDokumentTyp, Function.identity(), (a, b) -> a));
        final var existingDokumentRefMap = existingDokuments.stream()
            .filter(d -> Objects.nonNull(d.getDokumentTyp()) && Objects.nonNull(d.getEntryId()))
            .collect(Collectors.toUnmodifiableMap(GesuchDokument::getReference, Function.identity(), (a, b) -> a));

        // when a required doc is not existing in existingDokuments, it is still missing...
        final var missingGesuchDokuments = requiredDocs.stream()
            .filter(
                doc -> doc.getRight().stream().anyMatch(d -> !existingDokumentTypMap.containsKey(d))
            )
            .map(Pair::getLeft)
            .toList();

        // same for list dokuments like kinds and geschwisters
        final var missingRefGesuchDokuments = requiredRefDocs.stream()
            .filter(
                doc -> doc.getRight().stream().anyMatch(d -> !existingDokumentRefMap.containsKey(d))
            )
            .map(Pair::getLeft)
            .toList();

        // add an entry (pointing to the documents page) for each missing custom document,
        // so that the GS is informed about which he is required to upload
        final var missingCustomDokuments =
            DokumentValidationUtils.getMissingCustomDocumentTypsByTranche(customProducers, formular.getTranche());

        final Set<String> allMissingDokuments = new HashSet<>(missingGesuchDokuments);
        allMissingDokuments.addAll(missingRefGesuchDokuments);
        if (!missingCustomDokuments.isEmpty()) {
            allMissingDokuments.add(PAGENAME);
        }

        if (
            !allMissingDokuments.isEmpty()
        ) {
            return GesuchValidatorUtil.addProperties(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                allMissingDokuments
            );
        }

        return true;
    }

    private Set<GesuchDokument> getExistingRequiredGesuchDokuments(GesuchFormular formular) {
        final Function<String, Set<GesuchDokument>> logAndReturn = path -> {
            LOG.error(
                "If this happens in testing it's OK: {} on GesuchFormular with id '{}' is null",
                path,
                formular.getId()
            );
            return Set.of();
        };

        final var tranche = formular.getTranche();
        if (tranche == null) {
            return logAndReturn.apply("GesuchTranche");
        }

        final var gesuch = tranche.getGesuch();
        if (gesuch == null) {
            return logAndReturn.apply("GesuchTranche->Gesuch");
        }

        final var gesuchDokumente = tranche.getGesuchDokuments();
        if (gesuchDokumente == null) {
            return logAndReturn.apply("GesuchTranche->GesuchDokumente");
        }

        return gesuchDokumente.stream()
            .filter(
                gesuchDokument -> !gesuchDokument.getDokumente().isEmpty()
            )
            .collect(Collectors.toSet());
    }
}
