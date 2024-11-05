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

import ch.dvbern.stip.api.common.exception.AppValidationException;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
class AppValidationExceptionMapper implements ExceptionMapper<AppValidationException> {
    @SuppressWarnings({ "ProtectedField", "java:S6813" })
    @Inject
    protected TL tl;

    private Response responseFromException(AppValidationException exception) {
        var message = tl.translate(exception.getI18nMessage());

        AppValidationErrorResponse response =
            new AppValidationErrorResponse(
                List.of(
                    Violation.fromReferenceList(
                        message,
                        exception.getClientKey(),
                        List.of()
                    )
                )
            );

        return JsonMappingExceptionMapper.buildValidationErrorResponse(response);
    }

    @Override
    public Response toResponse(AppValidationException exception) {
        return responseFromException(exception);
    }
}
