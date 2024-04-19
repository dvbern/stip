package ch.dvbern.stip.api.common.exception;

import java.util.ArrayList;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.Severity;
import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import ch.dvbern.stip.generated.dto.ValidationWarningDto;
import jakarta.validation.ConstraintViolation;

public final class ValidationsExceptionMapper {

    private ValidationsExceptionMapper() {
    }

    public static ValidationReportDto toDto(ValidationsException validationsException) {
        if (validationsException == null) {
            return new ValidationReportDto();
        }
        return toDto(validationsException.getViolations());
    }

    public static ValidationReportDto toDto(Set<? extends ConstraintViolation<?>> constraintViolations) {
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

        validationsReportDto.setValidationWarnings(warnings);
        validationsReportDto.setValidationErrors(errors);
        return validationsReportDto;
    }
}
