package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.FailureType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collection;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
class AppValidationErrorResponse extends AppErrorResponse {

    List<Violation> violations;

    public AppValidationErrorResponse(Collection<Violation> violations) {
        super(FailureType.VALIDATION);

        this.violations = List.copyOf(violations);
    }
}
