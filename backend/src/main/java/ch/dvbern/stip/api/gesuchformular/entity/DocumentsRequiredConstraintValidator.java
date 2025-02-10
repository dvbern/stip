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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
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
        final var requiredDocs = producers.stream().map(x -> x.getRequiredDocuments(formular)).toList();
        final var dokumenteOfType = getRequiredDokumentTypes(formular);

        var filtered = requiredDocs.stream()
            .filter(x -> x.getRight().stream().anyMatch(y -> !dokumenteOfType.contains(y)))
            .map(Pair::getLeft)
            .toList();
        Set<String> allFiltered = new HashSet<>(filtered);

        final var customFiltered = getMissingCustomDocumentTypesByType(formular.getTranche());
        allFiltered.addAll(customFiltered);

        if (!allFiltered.isEmpty()) {
            return GesuchValidatorUtil.addProperties(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                allFiltered
            );
        }

        return true;
    }

    private List<String> getMissingCustomDocumentTypesByType(GesuchTranche tranche) {
        List<String> result = new ArrayList<>();
        final var required = getRequiredCustomDokumentTypes(tranche);
        final var existingByCustomDokumentTypId =
            getExistingGesuchDokumentsOfCustomDokumentType(tranche.getGesuchFormular()).stream()
                .map(x -> x.getId())
                .toList();

        required.forEach(req -> {
            if (!existingByCustomDokumentTypId.contains(req.getId())) {
                result.add(req.getType());
            }
        });
        return result;
    }

    private List<CustomDokumentTyp> getRequiredCustomDokumentTypes(GesuchTranche tranche) {
        ArrayList<CustomDokumentTyp> customDokumentTypes = new ArrayList<>();
        customProducers.stream()
            .map(producer -> producer.getRequiredDocuments(tranche))
            .forEach(x -> customDokumentTypes.addAll(x.getValue()));
        return customDokumentTypes;
    }

    private List<CustomDokumentTyp> getExistingGesuchDokumentsOfCustomDokumentType(GesuchFormular formular) {
        return formular.getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> Objects.nonNull(gesuchDokument.getCustomDokumentTyp())
                && !gesuchDokument.getDokumente().isEmpty()
            )
            .map(GesuchDokument::getCustomDokumentTyp)
            .toList();
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

        return gesuchDokumente.stream().map(GesuchDokument::getDokumentTyp).collect(Collectors.toSet());
    }
}
