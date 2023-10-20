package ch.dvbern.stip.api.common.exception;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public final class ValidationsExceptionMapper {

	private ValidationsExceptionMapper() {
	}

	public static ValidationReportDto toDto(ValidationsException validationsException) {
		if (validationsException == null) {
			return new ValidationReportDto();
		}
		return constraintViolationstoDto(validationsException.getViolations());
	}

	public static ValidationReportDto constraintViolationstoDto(Set<? extends ConstraintViolation<?>> constraintViolations) {
		ValidationReportDto validationsReportDto = new ValidationReportDto();
		constraintViolations.forEach(constraintViolation -> {
					ValidationErrorDto validationErrorDto = new ValidationErrorDto();
					validationErrorDto.setMessage(constraintViolation.getMessage());
					validationErrorDto.setMessageTemplate(constraintViolation.getMessageTemplate());
					validationErrorDto.setPropertyPath(constraintViolation.getPropertyPath() != null ?
							constraintViolation.getPropertyPath().toString() :
							null);
					validationsReportDto.getValidationErrors().add(validationErrorDto);
				}
		);
		return validationsReportDto;
	}
}