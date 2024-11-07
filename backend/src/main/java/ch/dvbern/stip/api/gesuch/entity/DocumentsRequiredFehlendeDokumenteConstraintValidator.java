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

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentsRequiredFehlendeDokumenteConstraintValidator
    implements ConstraintValidator<DocumentsRequiredFehlendeDokumenteConstraint, Gesuch> {

    @Inject
    GesuchService gesuchService;

    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        var anyAusstehend = false;
        var anyAbgelehnt = false;

        final var gesuchDokumentDtos = gesuchService.getGesuchDokumenteForGesuch(gesuch.getId());
        if (gesuchDokumentDtos.isEmpty()) {
            return true;
        }

        for (final var gesuchDokumentDto : gesuchDokumentDtos) {
            if (gesuchDokumentDto.getStatus() == Dokumentstatus.AUSSTEHEND) {
                anyAusstehend = true;
                break;
            }

            if (gesuchDokumentDto.getStatus() == Dokumentstatus.ABGELEHNT) {
                anyAbgelehnt = true;
            }
        }

        // Only return true if none are ausstehen and at least 1 is abgelehnt
        return !anyAusstehend && anyAbgelehnt;
    }
}
