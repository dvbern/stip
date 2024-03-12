package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
@Provider
public class HibernateConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final String SQL_STATE_UNIQUE_CONSTRAINT_VIOLATED = "23505";

    @Override
    public Response toResponse(ConstraintViolationException e) {

        if (e.getSQLState().equals(SQL_STATE_UNIQUE_CONSTRAINT_VIOLATED)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        throw AppFailureMessage.databaseConstraintViolation().create(e);
    }
}
