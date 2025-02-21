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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.util.DokumentValidationUtils;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
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
    Instance<RequiredDocumentsProducer> producers;

    @Inject
    Instance<RequiredCustomDocumentsProducer> customProducers;

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(producer -> producer.getRequiredDocuments(formular)).toList();
        final var existingDokumenteOfType = getExistingRequiredDokumentTypes(formular);
        // when a required doc is not existing in existingDokumenteOfType, it is still missing...
        var filtered = requiredDocs.stream()
            .filter(
                doc -> doc.getRight().stream().anyMatch(dokumentTyp -> !existingDokumenteOfType.contains(dokumentTyp))
            )
            .map(Pair::getLeft)
            .toList();
        Set<String> allFiltered = new HashSet<>(filtered);

        // compare if all required custom documents have an existing match
        // (with a GesuchDokument that contains at least 1 file)
        // if no match, these custom dokuments appear in the validation message
        final var customFiltered =
            DokumentValidationUtils.getMissingCustomDocumentTypsByTranche(customProducers, formular.getTranche());
        customFiltered.forEach(missingCustomDok -> allFiltered.add(PAGENAME));

        if (!allFiltered.isEmpty()) {
            return GesuchValidatorUtil.addProperties(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                allFiltered
            );
        }

        return true;
    }

    private Set<DokumentTyp> getExistingRequiredDokumentTypes(GesuchFormular formular) {
        final Function<String, Set<DokumentTyp>> logAndReturn = path -> {
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

        if (gesuch.getGesuchStatus() == Gesuchstatus.FEHLENDE_DOKUMENTE) {
            return gesuchDokumente.stream()
                .filter(
                    gesuchDokument -> !gesuchDokument.getDokumente().isEmpty()
                )
                .map(GesuchDokument::getDokumentTyp)
                .collect(Collectors.toSet());
        }

        return gesuchDokumente.stream()
            .map(GesuchDokument::getDokumentTyp)
            .collect(Collectors.toSet());
    }
}
