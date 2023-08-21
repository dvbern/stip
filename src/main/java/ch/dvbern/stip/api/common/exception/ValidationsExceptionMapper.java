package ch.dvbern.stip.api.common.exception;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

public final class ValidationsExceptionMapper {

	private ValidationsExceptionMapper() {
	}

	public static ValidationReportDto toDto(ValidationsException validationsException) {
		ValidationReportDto validationsReportDto = new ValidationReportDto();
		if (validationsException == null) {
			return validationsReportDto;
		}
		validationsException.getViolations().stream().forEach(constraintViolation -> {
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