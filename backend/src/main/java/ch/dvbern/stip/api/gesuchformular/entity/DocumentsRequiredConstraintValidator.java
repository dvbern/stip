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
    @Inject
    Instance<RequiredDocumentsProducer> producers;

    @Inject
    Instance<RequiredCustomDocumentsProducer> customProducers;

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(producer -> producer.getRequiredDocuments(formular)).toList();
        final var dokumenteOfType = getRequiredDokumentTypes(formular);
        var filtered = requiredDocs.stream()
            .filter(doc -> doc.getRight().stream().anyMatch(dokumentTyp -> !dokumenteOfType.contains(dokumentTyp)))
            .map(Pair::getLeft)
            .toList();
        Set<String> allFiltered = new HashSet<>(filtered);

        final var customFiltered =
            DokumentValidationUtils.getMissingCustomDocumentTypesByType(customProducers, formular.getTranche());
        allFiltered.addAll(customFiltered);

        final var isNOTBeingEditedBySB =
            formular.getTranche().getGesuch().getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_SB;

        if (
            !allFiltered.isEmpty()
            && isNOTBeingEditedBySB
        ) {
            return GesuchValidatorUtil.addProperties(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                allFiltered
            );
        }

        return true;
    }

    private Set<GesuchDokument> getRequiredCustomDocumentTypes(GesuchFormular formular) {
        final var customGesuchDokumente = formular.getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(x -> Objects.nonNull(x.getCustomDokumentTyp()) && x.getDokumente().isEmpty())
            .collect(Collectors.toSet());
        return new HashSet<>(customGesuchDokumente);
    }

    private Set<DokumentTyp> getRequiredDokumentTypes(GesuchFormular formular) {
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

        return gesuchDokumente.stream()
            .filter(x -> Objects.isNull(x.getCustomDokumentTyp()))
            .map(GesuchDokument::getDokumentTyp)
            .collect(Collectors.toSet());
    }
}
