package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.ExceptionId;
import ch.dvbern.stip.api.common.exception.FailureType;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;

import java.util.List;

@Getter
class AppFailureErrorResponse extends AppErrorResponse {

    private final ExceptionId id;
    private final String summary;

    public AppFailureErrorResponse(
        ExceptionId id,
        String summary,
        List<JsonMappingException.Reference> referenceList
    ) {
        this(id, summary, JsonMappingReferenceParser.fromReferenceList(referenceList).parse());
    }

    public AppFailureErrorResponse(ExceptionId id, String summary) {
        super(FailureType.FAILURE);
        this.id = id;
        this.summary = summary;
    }

    public AppFailureErrorResponse(
        ExceptionId id,
        String summary,
        String detail
    ) {
        super(FailureType.FAILURE);
        this.id = id;
        this.summary = summary + ", detail: " + detail;
    }
}
