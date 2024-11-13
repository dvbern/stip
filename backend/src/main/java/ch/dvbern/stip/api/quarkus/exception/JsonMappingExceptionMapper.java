/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.quarkus.exception;

import java.util.List;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.exception.AppValidationException;
import ch.dvbern.stip.api.common.exception.ExceptionId;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.exception.AppFailureMessage.internalError;

@Provider
@Slf4j
@RequiredArgsConstructor
class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
    private final TL tl;

    private final ConstraintViolationParser violationParser = new ConstraintViolationParser();

    public static Response buildValidationErrorResponse(AppValidationErrorResponse validation) {
        return Response
            .status(Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(validation)
            .build();
    }

    @Override
    public Response toResponse(JsonMappingException exception) {
        try {
            Throwable cause = exception.getCause();
            if (cause instanceof ConstraintViolationException cv) {
                List<Violation> violations = violationParser.parse(cv.getConstraintViolations());
                return buildValidationErrorResponse(new AppValidationErrorResponse(violations));
            }

            if (cause instanceof AppValidationException av) {
                // this case happens when e.g. the emailaddress is parsed and fails
                // cause of this we get a message from the caused exception but the path from the jsonexception
                return buildValidationErrorResponse(
                    new AppValidationErrorResponse(
                        List.of(
                            Violation.fromReferenceList(
                                tl.translate(av.getI18nMessage()),
                                av.getClientKey(),
                                exception.getPath()
                            )
                        )
                    )
                );
            }

            AppFailureMessage failureMessage = AppFailureMessage.jsonMapping();
            ExceptionId exceptionId = failureMessage.getId();
            LOG.error("Unhandled exception: {}", exceptionId, exception);
            var msg = tl.translate(failureMessage.getI18nMessage());

            return buildFailureResponse(
                new AppFailureErrorResponse(exceptionId, msg, exception.getPath())
            );
        } catch (RuntimeException rte) {
            AppFailureMessage internalErrorMessage = internalError("null");
            ExceptionId exceptionId = internalErrorMessage.getId();
            LOG.error("Error while building the error message: {}", exceptionId, rte);
            var msg = tl.translate(internalErrorMessage.getI18nMessage());
            return buildFailureResponse(
                new AppFailureErrorResponse(
                    exceptionId,
                    msg,
                    "error building the validation error message, see server logfile for details"
                )
            );
        }
    }

    private Response buildFailureResponse(AppFailureErrorResponse failure) {
        return Response
            .serverError()
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(failure)
            .build();
    }
}
