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

package ch.dvbern.stip.api.auszahlung.service;

import java.util.Optional;
import java.util.Set;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_AUSZAHLUNG_VALID_MESSAGE;

@RequestScoped
@RequiredArgsConstructor
public class AuszahlungValidatorService {
    private final Validator validator;

    public void validateAuszahlung(final Gesuch toValidate) {
        var auszahlungOpt = Optional.ofNullable(toValidate.getAusbildung().getFall().getAuszahlung());
        var violations = auszahlungOpt
            .map(Auszahlung::getZahlungsverbindung)
            .map(zahlungsverbindung -> validator.validate(zahlungsverbindung))
            .orElse(Set.of());

        if (auszahlungOpt.isEmpty() || !violations.isEmpty()) {
            throw new CustomValidationsException(
                "Keine Auszahlung vorhanden oder ungültige Zahlungsverbindung",
                new CustomConstraintViolation(
                    VALIDATION_GESUCHEINREICHEN_AUSZAHLUNG_VALID_MESSAGE,
                    "auszahlung"
                )
            );
        }
    }
}
