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

import java.util.ArrayList;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.Severity;
import ch.dvbern.stip.generated.dto.ValidationMessageDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.validation.ConstraintViolation;

public final class ValidationsExceptionMapper {

    private ValidationsExceptionMapper() {}

    public static ValidationReportDto toDto(ValidationsException validationsException) {
        if (validationsException == null) {
            return new ValidationReportDto();
        }
        return toDto(validationsException.getViolations());
    }

    public static ValidationReportDto toDto(Set<? extends ConstraintViolation<?>> constraintViolations) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        final var warnings = new ArrayList<ValidationMessageDto>();
        final var errors = new ArrayList<ValidationMessageDto>();

        constraintViolations.forEach(constraintViolation -> {
            final var payload = constraintViolation.getConstraintDescriptor().getPayload();
            final var propertyPath = constraintViolation.getPropertyPath();
            if (payload.contains(Severity.Warning.class)) {
                final var warningDto = new ValidationMessageDto();
                warningDto.setMessage(constraintViolation.getMessage());
                warningDto.setMessageTemplate(constraintViolation.getMessageTemplate());
                warningDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
                warnings.add(warningDto);
            } else {
                final var errorDto = new ValidationMessageDto();
                errorDto.setMessage(constraintViolation.getMessage());
                errorDto.setMessageTemplate(constraintViolation.getMessageTemplate());
                errorDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
                errors.add(errorDto);
            }
        });

        validationsReportDto.setValidationWarnings(warnings);
        validationsReportDto.setValidationErrors(errors);
        return validationsReportDto;
    }
}
