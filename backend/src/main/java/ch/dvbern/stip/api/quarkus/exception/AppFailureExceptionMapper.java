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

import ch.dvbern.stip.api.common.exception.AppFailureException;
import ch.dvbern.stip.api.common.i18n.translations.TL;
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
