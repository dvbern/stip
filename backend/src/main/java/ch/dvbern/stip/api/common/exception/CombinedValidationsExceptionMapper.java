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
import ch.dvbern.stip.generated.dto.ValidationMessageDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
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
        final var warnings = new ArrayList<ValidationMessageDto>();
        final var errors = new ArrayList<ValidationMessageDto>();

        constraintViolations.forEach(constraintViolation -> {
            final var payload = constraintViolation.getConstraintDescriptor().getPayload();
            if (payload.contains(Severity.Warning.class)) {
                // set warnings
                final var warningDto = toMessageDto(constraintViolation);
                warnings.add(warningDto);
            } else if (payload.contains(Severity.Error.class)) {
                // set errors
                final var validationErrorDto = toMessageDto(constraintViolation);
                errors.add(validationErrorDto);
            }

            // if other severities are added, they are ignored by default,
            // and must be handled in following else ifs if required
        });
        errors.add(toMessageDto(additionalConstraintViolation));

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

        final var validationErrorDto = toMessageDto(validationsException);
        final var additionalValidationErrorDto = toMessageDto(additionalValidationsException);
        validationsReportDto.setValidationErrors(
            List.of(validationErrorDto, additionalValidationErrorDto)
        );

        return validationsReportDto;
    }

    private static ValidationMessageDto toMessageDto(
        final String message,
        final String messageTemplate,
        final String propertyPath
    ) {
        var messageDto = new ValidationMessageDto();
        messageDto.setMessage(message);
        messageDto.setMessageTemplate(messageTemplate);
        messageDto.setPropertyPath(propertyPath != null ? propertyPath : null);
        return messageDto;
    }

    private static ValidationMessageDto toMessageDto(ConstraintViolation<?> constraintViolation) {
        return toMessageDto(
            constraintViolation.getMessage(),
            constraintViolation.getMessageTemplate(),
            constraintViolation.getPropertyPath().toString()
        );
    }

    private static ValidationMessageDto toMessageDto(CustomConstraintViolation additionalConstraintViolation) {
        return toMessageDto(
            additionalConstraintViolation.getMessage(),
            additionalConstraintViolation.getMessageTemplate(),
            additionalConstraintViolation.getPropertyPath().toString()
        );
    }

    private static ValidationMessageDto toMessageDto(CustomValidationsException validationsException) {
        return toMessageDto(validationsException.getConstraintViolation());
    }
}
