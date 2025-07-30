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

package ch.dvbern.stip.api.lebenslauf.entity;

import java.util.Objects;
import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_BERUFSBEZEICHNUNG_NOT_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE;

public class LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraintValidator
    implements ConstraintValidator<LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraint, LebenslaufItem> {
    @Override
    public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
        final var hasFachrichtungBerufsbezeichnung = Objects.nonNull(lebenslaufItem.getFachrichtungBerufsbezeichnung());
        final var needsFachrichtungBerufsbezeichnung = Optional.ofNullable(lebenslaufItem.getAbschluss())
            .map(abschluss -> Objects.nonNull(abschluss.getZusatzfrage()))
            .orElse(false);

        if (needsFachrichtungBerufsbezeichnung == hasFachrichtungBerufsbezeichnung) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            hasFachrichtungBerufsbezeichnung
                ? VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE
                : VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_BERUFSBEZEICHNUNG_NOT_NULL_MESSAGE
        )
            .addConstraintViolation();
        return false;
    }
}
