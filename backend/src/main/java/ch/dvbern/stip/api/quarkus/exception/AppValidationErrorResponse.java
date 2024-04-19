package ch.dvbern.stip.api.quarkus.exception;

import java.util.Collection;
import java.util.List;

import ch.dvbern.stip.api.common.exception.FailureType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AppValidationErrorResponse extends AppErrorResponse {
    List<Violation> violations;

    public AppValidationErrorResponse(Collection<Violation> violations) {
        super(FailureType.VALIDATION);

        this.violations = List.copyOf(violations);
    }
}
