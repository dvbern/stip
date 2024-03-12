package ch.dvbern.stip.api.quarkus.exception;

import ch.dvbern.stip.api.common.exception.AppValidationException;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Provider
@Slf4j
class AppValidationExceptionMapper implements ExceptionMapper<AppValidationException> {

    @SuppressWarnings({ "ProtectedField", "java:S6813" })
    @Inject
    protected TL tl;

    private Response responseFromException(AppValidationException exception) {
        var message = tl.translate(exception.getI18nMessage());

        AppValidationErrorResponse response =
            new AppValidationErrorResponse(List.of(
                Violation.fromReferenceList(
                    message,
                    exception.getClientKey(),
                    List.of()
                )
            ));

        return JsonMappingExceptionMapper.buildValidationErrorResponse(response);
    }

    @Override
    public Response toResponse(AppValidationException exception) {
        return responseFromException(exception);
    }
}
