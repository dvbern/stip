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

public class SapDeliverysLengthConstraintValidator
    implements ConstraintValidator<SapDeliverysLengthConstraint, Buchhaltung> {
    public static final Integer MAX_SAP_DELIVERYS_CREATE_BUSINESSPARTNER = 3;
    public static final Integer MAX_SAP_DELIVERYS_AUSZAHLUNG = 3;

    @Override
    public boolean isValid(Buchhaltung value, ConstraintValidatorContext context) {
        return switch (value.getBuchhaltungType()) {
            case SALDOAENDERUNG, STIPENDIUM -> value.getSapDeliverys().isEmpty();
            case AUSZAHLUNG_INITIAL, AUSZAHLUNG_REMAINDER -> value.getSapDeliverys()
                .size() <= MAX_SAP_DELIVERYS_AUSZAHLUNG;
            case BUSINESSPARTNER_CREATE, BUSINESSPARTNER_CHANGE -> value.getSapDeliverys()
                .size() <= MAX_SAP_DELIVERYS_CREATE_BUSINESSPARTNER;
        };
    }
}
