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
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.common.validation.Severity;
import ch.dvbern.stip.generated.dto.ValidationMessageDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationsExceptionMapper {
    public ValidationReportDto toDto(
        ValidationsException validationsException,
        CustomConstraintViolation additionalConstraintViolation,
        Boolean hasDocuments
    ) {
        if (validationsException == null) {
            return new ValidationReportDto();
        }
        return toDto(validationsException.getViolations(), additionalConstraintViolation, hasDocuments);
    }

    public ValidationReportDto toDto(
        ValidationsException validationsException,
        Boolean hasDocuments
    ) {
        return toDto(validationsException, null, hasDocuments);
    }

    public ValidationReportDto toDto(
        ValidationsException validationsException
    ) {
        return toDto(validationsException, false);
    }

    public ValidationReportDto toDto(
        Set<? extends ConstraintViolation<?>> constraintViolations,
        CustomConstraintViolation additionalConstraintViolation,
        Boolean hasDocuments
    ) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        ArrayList<ValidationMessageDto> warnings = new ArrayList<>();
        ArrayList<ValidationMessageDto> errors = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {
            final Set<Class<? extends Payload>> payload = constraintViolation.getConstraintDescriptor().getPayload();
            final ValidationMessageDto messageDto = ExceptionMapperUtil.toMessageDto(constraintViolation);

            if (payload.contains(Severity.Warning.class)) {
                // set warnings
                warnings.add(messageDto);
            } else {
                // set errors. Violations with no payload are considered errors.
                // if other severities are added, they are thrown by default,
                // and must be handled seperately
                errors.add(messageDto);
            }
        });
        if (Objects.nonNull(additionalConstraintViolation)) {
            errors.add(ExceptionMapperUtil.toMessageDto(additionalConstraintViolation));
        }

        validationsReportDto.setValidationWarnings(warnings);
        validationsReportDto.setValidationErrors(errors);
        validationsReportDto.setHasDocuments(hasDocuments);

        return validationsReportDto;
    }

    public ValidationReportDto toDto(
        Set<? extends ConstraintViolation<?>> constraintViolations,
        Boolean hasDocuments
    ) {
        return toDto(constraintViolations, null, hasDocuments);
    }

    public ValidationReportDto toDto(
        CustomValidationsException validationsException,
        CustomConstraintViolation additionalConstraintViolation,
        Boolean hasDocuments
    ) {
        final ArrayList<ValidationMessageDto> validationErrors = new ArrayList<>();
        if (Objects.nonNull(validationsException)) {
            final ValidationMessageDto validationErrorDto = ExceptionMapperUtil.toMessageDto(validationsException);
            validationErrors.add(validationErrorDto);
        }

        if (Objects.nonNull(additionalConstraintViolation)) {
            validationErrors.add(ExceptionMapperUtil.toMessageDto(additionalConstraintViolation));
        }
        final ValidationReportDto report = new ValidationReportDto();
        report.setValidationErrors(validationErrors);
        report.setHasDocuments(hasDocuments);
        return report;
    }

    public ValidationReportDto toDto(
        CustomValidationsException validationsException,
        boolean hasDocuments
    ) {
        return toDto(validationsException, null, hasDocuments);
    }

    public ValidationReportDto toDto(
        CustomConstraintViolation additionalConstraintViolation,
        Boolean hasDocuments
    ) {
        return toDto((CustomValidationsException) null, additionalConstraintViolation, hasDocuments);
    }

    public ValidationReportDto toDto(
        Boolean hasDocuments
    ) {
        final ValidationReportDto report = new ValidationReportDto();
        report.setHasDocuments(hasDocuments);
        return report;
    }

    public static ValidationReportDto toDto(
        CustomValidationsException validationsException
    ) {
        return toDto(validationsException, false);
    }
}
