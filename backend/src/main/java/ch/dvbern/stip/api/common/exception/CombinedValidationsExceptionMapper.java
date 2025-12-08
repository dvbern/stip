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
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.common.validation.Severity;
import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationWarningDto;
import jakarta.validation.ConstraintViolation;

public final class CombinedValidationsExceptionMapper {
    private CombinedValidationsExceptionMapper() {}

    public static ValidationReportDto toDto(
        ValidationsException validationsException,
        CustomValidationsException additionalValidationsException
    ) {
        if (validationsException == null) {
            return new ValidationReportDto();
        }
        return toDto(validationsException.getViolations(), additionalValidationsException.getConstraintViolation());
    }

    public static ValidationReportDto toDto(
        Set<? extends ConstraintViolation<?>> constraintViolations,
        CustomConstraintViolation additionalConstraintViolation
    ) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        final var warnings = new ArrayList<ValidationWarningDto>();
        final var errors = new ArrayList<ValidationErrorDto>();

        constraintViolations.forEach(constraintViolation -> {
            final var payload = constraintViolation.getConstraintDescriptor().getPayload();
            final var propertyPath = constraintViolation.getPropertyPath();
            if (payload.contains(Severity.Warning.class)) {
                final var warningDto = new ValidationWarningDto();
                warningDto.setMessage(constraintViolation.getMessage());
                warningDto.setMessageTemplate(constraintViolation.getMessageTemplate());
                warningDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
                warnings.add(warningDto);
            } else {
                final var errorDto = new ValidationErrorDto();
                errorDto.setMessage(constraintViolation.getMessage());
                errorDto.setMessageTemplate(constraintViolation.getMessageTemplate());
                errorDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
                errors.add(errorDto);
            }
        });
        errors.add(toErrorDto(additionalConstraintViolation));

        validationsReportDto.setValidationWarnings(warnings);
        validationsReportDto.setValidationErrors(errors);
        return validationsReportDto;
    }

    public static ValidationReportDto toDto(
        CustomValidationsException validationsException,
        CustomValidationsException additionalValidationsException
    ) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        if (validationsException == null) {
            return validationsReportDto;
        }

        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setMessage(validationsException.getConstraintViolation().getMessage());
        validationErrorDto.setMessageTemplate(validationsException.getConstraintViolation().getMessageTemplate());
        validationErrorDto.setPropertyPath(validationsException.getConstraintViolation().getPropertyPath());
        validationsReportDto.setValidationErrors(
            List.of(validationErrorDto, toErrorDto(additionalValidationsException.getConstraintViolation()))
        );

        return validationsReportDto;
    }

    private static ValidationErrorDto toErrorDto(CustomConstraintViolation additionalConstraintViolation) {
        final var propertyPath = additionalConstraintViolation.getPropertyPath();
        final var additionalErrorDto = new ValidationErrorDto();
        additionalErrorDto.setMessage(additionalConstraintViolation.getMessage());
        additionalErrorDto.setMessageTemplate(additionalConstraintViolation.getMessageTemplate());
        additionalErrorDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
        return additionalErrorDto;
    }
}
