package ch.dvbern.stip.api.quarkus.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var violations = new ConstraintViolationParser()
            .parse(exception.getConstraintViolations());
        return JsonMappingExceptionMapper.buildValidationErrorResponse(
            new AppValidationErrorResponse(violations));
    }
}
