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
