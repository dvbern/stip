package ch.dvbern.stip.api.common.exception;

import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import lombok.Getter;

@Getter
public class CustomValidationsException extends RuntimeException {

	private final CustomConstraintViolation constraintViolation;

	public CustomValidationsException(String message, CustomConstraintViolation constraintViolation) {
		super(message);
		this.constraintViolation = constraintViolation;
	}
}
