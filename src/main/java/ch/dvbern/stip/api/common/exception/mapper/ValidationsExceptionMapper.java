package ch.dvbern.stip.api.common.exception.mapper;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named
public class ValidationsExceptionMapper {
	public ValidationReportDto toDto(ValidationsException validationsException) {
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