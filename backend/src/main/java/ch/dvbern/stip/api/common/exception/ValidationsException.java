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

package ch.dvbern.stip.api.common.exception;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ValidationsException extends RuntimeException {
    private final transient Set<ConstraintViolation<?>> violations;

    public ValidationsException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = new HashSet<>(violations);
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        this.violations.forEach(x -> sb.append(x.getMessage()));

        return sb.toString();
    }
}
