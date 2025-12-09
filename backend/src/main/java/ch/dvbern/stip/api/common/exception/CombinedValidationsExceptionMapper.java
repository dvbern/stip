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
            if (payload.contains(Severity.Warning.class)) {
                // set warnings
                final var warningDto = toWarningDto(constraintViolation);
                warnings.add(warningDto);
            } else {
                // set errors
                final var validationErrorDto = toErrorDto(constraintViolation);
                errors.add(validationErrorDto);
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

        final var validationErrorDto = toErrorDto(validationsException);
        final var additionalValidationErrorDto = toErrorDto(additionalValidationsException);
        validationsReportDto.setValidationErrors(
            List.of(validationErrorDto, additionalValidationErrorDto)
        );

        return validationsReportDto;
    }

    private static ValidationWarningDto toWarningDto(ConstraintViolation<?> constraintViolation) {
        final var propertyPath = constraintViolation.getPropertyPath();
        var validationWarningDto = new ValidationWarningDto();
        validationWarningDto.setMessage(constraintViolation.getMessage());
        validationWarningDto.setMessageTemplate(constraintViolation.getMessageTemplate());
        validationWarningDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
        return validationWarningDto;
    }

    private static ValidationErrorDto toErrorDto(ConstraintViolation<?> constraintViolation) {
        final var propertyPath = constraintViolation.getPropertyPath();
        var validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setMessage(constraintViolation.getMessage());
        validationErrorDto.setMessageTemplate(constraintViolation.getMessageTemplate());
        validationErrorDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
        return validationErrorDto;
    }

    private static ValidationErrorDto toErrorDto(CustomConstraintViolation additionalConstraintViolation) {
        final var propertyPath = additionalConstraintViolation.getPropertyPath();
        final var additionalErrorDto = new ValidationErrorDto();
        additionalErrorDto.setMessage(additionalConstraintViolation.getMessage());
        additionalErrorDto.setMessageTemplate(additionalConstraintViolation.getMessageTemplate());
        additionalErrorDto.setPropertyPath(propertyPath != null ? propertyPath.toString() : null);
        return additionalErrorDto;
    }

    private static ValidationErrorDto toErrorDto(CustomValidationsException validationsException) {
        var validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setMessage(validationsException.getConstraintViolation().getMessage());
        validationErrorDto.setMessageTemplate(validationsException.getConstraintViolation().getMessageTemplate());
        validationErrorDto.setPropertyPath(validationsException.getConstraintViolation().getPropertyPath());
        return validationErrorDto;
    }
}
