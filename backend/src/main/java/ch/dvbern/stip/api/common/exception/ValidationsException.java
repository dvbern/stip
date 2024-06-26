package ch.dvbern.stip.api.common.exception;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ValidationsException extends RuntimeException {
    private final transient Set<ConstraintViolation<?>> violations;

    public ValidationsException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = new HashSet<>(violations);
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        this.violations.forEach(x -> sb.append(x.getMessage()));

        return sb.toString();
    }
}
