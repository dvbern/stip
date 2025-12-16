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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_AUSZAHLUNG_VALID_MESSAGE;

@RequestScoped
@RequiredArgsConstructor
public class AuszahlungValidatorService {
    private final Validator validator;

    public CustomConstraintViolation getZahlungsverbindungCustomConstraintViolation(
        final Gesuch gesuch
    ) {
        var relevantZahlungsverbindungOpt =
            Optional.ofNullable(gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung());
        Set<ConstraintViolation<Zahlungsverbindung>> violations =
            relevantZahlungsverbindungOpt
                .map(zahlungsverbindung -> validator.validate(zahlungsverbindung))
                .orElse(null);

        CustomConstraintViolation out = null;
        if (Objects.isNull(violations) || !violations.isEmpty()) {
            out = new CustomConstraintViolation(
                VALIDATION_GESUCHEINREICHEN_AUSZAHLUNG_VALID_MESSAGE,
                "auszahlung"
            );
        }
        return out;
    }
}
