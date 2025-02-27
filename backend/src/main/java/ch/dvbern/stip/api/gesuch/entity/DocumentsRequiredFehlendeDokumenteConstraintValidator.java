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

package ch.dvbern.stip.api.gesuch.entity;

import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentsRequiredFehlendeDokumenteConstraintValidator
    implements ConstraintValidator<DocumentsRequiredFehlendeDokumenteConstraint, Gesuch> {

    @Inject
    GesuchService gesuchService;

    // this validation occurs only when SB sends missing documents to GS
    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        final var gesuchDokumentDtos = gesuchService.getGesuchDokumenteForGesuch(gesuch.getId());
        if (gesuchDokumentDtos.isEmpty()) {
            return true;
        }
        // custom gesuch dokumente are in state AUSSTEHEND and are treated separately - thats why they are excluded
        // in this check.
        // the separate check is done on site GS, in DocumentsRequiredConstraintValidator on page for documents
        final var nonCustomGesuchDokumente =
            gesuchDokumentDtos.stream()
                .filter(gesuchDokumentDto -> Objects.isNull(gesuchDokumentDto.getCustomDokumentTyp()))
                .toList();
        // check if any document is unprocessed by SB
        return !isAnyAusstehend(nonCustomGesuchDokumente);

    }

    private boolean isAnyAusstehend(final List<GesuchDokumentDto> gesuchDokumentDtos) {
        return gesuchDokumentDtos.stream()
            .anyMatch(gesuchDokumentDto -> gesuchDokumentDto.getStatus() == Dokumentstatus.AUSSTEHEND);
    }
}
