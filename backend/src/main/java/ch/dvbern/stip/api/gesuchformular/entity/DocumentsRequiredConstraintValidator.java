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

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
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
    @Inject
    Instance<RequiredDocumentsProducer> producers;

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(x -> x.getRequiredDocuments(formular)).toList();
        final var dokumenteOfType = getRequiredDokumentTypes(formular);

        final var filtered = requiredDocs.stream()
            .filter(x -> x.getRight().stream().anyMatch(y -> !dokumenteOfType.contains(y)))
            .map(Pair::getLeft)
            .toList();

        if (!filtered.isEmpty()) {
            return GesuchValidatorUtil.addProperties(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                filtered
            );
        }

        return true;
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
