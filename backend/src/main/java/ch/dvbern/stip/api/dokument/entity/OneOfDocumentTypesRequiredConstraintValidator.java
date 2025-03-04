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

package ch.dvbern.stip.api.dokument.entity;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OneOfDocumentTypesRequiredConstraintValidator
    implements ConstraintValidator<OneOfDocumentTypesRequiredConstraint, GesuchDokument> {
    @Override
    public boolean isValid(GesuchDokument gesuchDokument, ConstraintValidatorContext context) {
        // both custom typ & doc typ may be set to null when an AENDERUNG is being deleted
        // if (gesuchDokument.getGesuchTranche().getTyp() == GesuchTrancheTyp.AENDERUNG) {
        // return true;
        // }
        return Objects.nonNull(gesuchDokument.getDokumentTyp()) && Objects.isNull(gesuchDokument.getCustomDokumentTyp())
        || Objects.nonNull(gesuchDokument.getCustomDokumentTyp()) && Objects.isNull(gesuchDokument.getDokumentTyp());
    }
}
