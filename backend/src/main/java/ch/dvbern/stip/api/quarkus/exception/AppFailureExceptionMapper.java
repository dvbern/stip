package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.AppFailureException;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
@RequiredArgsConstructor
class AppFailureExceptionMapper implements ExceptionMapper<AppFailureException> {

    private final TL tl;

    @Override
    public Response toResponse(AppFailureException exception) {
        LOG.error("AppFailure[errorId={}]", exception.getId(), exception);

        final var msg = tl.translate(exception.getI18nMessage());

        return Response.serverError()
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new AppFailureErrorResponse(exception.getId(), msg))
            .build();
    }
}
