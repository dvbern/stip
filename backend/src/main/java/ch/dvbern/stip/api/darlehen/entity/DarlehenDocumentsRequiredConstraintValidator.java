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

package ch.dvbern.stip.api.darlehen.entity;

import ch.dvbern.stip.api.darlehen.service.DarlehenService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DarlehenDocumentsRequiredConstraintValidator
    implements ConstraintValidator<DarlehenDocumentsRequiredConstraint, FreiwilligDarlehen> {
    @Inject
    DarlehenService darlehenService;

    @Override
    public boolean isValid(FreiwilligDarlehen freiwilligDarlehen, ConstraintValidatorContext context) {
        final var missingDocumentTypes = darlehenService.getRequiredDokumentsForDarlehen(freiwilligDarlehen.getId());
        final var currentDocumentTypes = freiwilligDarlehen.getDokumente()
            .stream()
            .filter(d -> !d.getDokumente().isEmpty())
            .map(DarlehenDokument::getDokumentType)
            .toList();
        if (!currentDocumentTypes.isEmpty()) {
            missingDocumentTypes.removeIf(currentDocumentTypes::contains);
        }
        return missingDocumentTypes.isEmpty();
    }
}
