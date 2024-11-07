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

package ch.dvbern.stip.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.IBANValidator;

public class IbanConstraintValidator implements ConstraintValidator<IbanConstraint, String> {
    private final IBANValidator validator;

    public IbanConstraintValidator() {
        var ch = IBANValidator.DEFAULT_IBAN_VALIDATOR.getValidator("CH");
        validator = new IBANValidator(new IBANValidator.Validator[] { ch });
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext constraintValidatorContext) {
        final var formattedIban = iban.replace(" ", "");
        return validator.isValid(formattedIban) && !ibanIsQR(formattedIban);
    }

    // The IBAN passed must be a valid IBAN
    boolean ibanIsQR(String iban) {
        // QR-IBANs are identified by a 5 digit number starting at the 4th digit.
        // If that number is between 30000 and 31999 then the IBAN is a QR-IBAN
        final var qrIdentificator = iban.substring(4, 9);
        final var qrNumber = Integer.parseInt(qrIdentificator);
        return qrNumber >= 30000 && qrNumber <= 31999;
    }
}
