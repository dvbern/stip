package ch.dvbern.stip.api.common.exception;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import java.util.List;

public final class CustomValidationsExceptionMapper {

    private CustomValidationsExceptionMapper() {
    }

    public static ValidationReportDto toDto(CustomValidationsException validationsException) {
        ValidationReportDto validationsReportDto = new ValidationReportDto();
        if (validationsException == null) {
            return validationsReportDto;
        }

        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setMessage(validationsException.getConstraintViolation().getMessage());
        validationErrorDto.setMessageTemplate(validationsException.getConstraintViolation().getMessageTemplate());
        validationsReportDto.setValidationErrors(List.of(validationErrorDto));

        return validationsReportDto;
    }
}
