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

package ch.dvbern.stip.api.buchhaltung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SaldoaenderungBuchhaltungNotZeroConstraintValidator
    implements ConstraintValidator<SaldoaenderungBuchhaltungNotZeroConstraint, Buchhaltung> {

    @Override
    public boolean isValid(Buchhaltung value, ConstraintValidatorContext context) {
        return switch (value.getBuchhaltungType()) {
            case SALDOAENDERUNG -> !value.getBetrag().equals(0);
            case STIPENDIUM, BUSINESSPARTNER_CREATE, BUSINESSPARTNER_CHANGE, AUSZAHLUNG_INITIAL, AUSZAHLUNG_REMAINDER -> true;
        };
    }
}
