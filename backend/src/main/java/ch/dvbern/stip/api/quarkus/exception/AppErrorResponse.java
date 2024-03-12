package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.FailureType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
abstract class AppErrorResponse {

    private final FailureType type;

    protected AppErrorResponse(FailureType type) {
        this.type = type;
    }

}
