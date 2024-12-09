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

package ch.dvbern.stip.api.common.interceptors;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.validation.Validator;

@Validated
@Interceptor
@UnlessBuildProfile("prod")
public class ValidatedInterceptor {
    @Inject
    Validator validator;

    @AroundInvoke
    Object validate(final InvocationContext context) throws Exception {
        final var response = context.proceed();
        if (response == null) {
            return null;
        }

        final var violations = validator.validate(response);
        if (!violations.isEmpty()) {
            throw new ValidationsException(
                String.format(
                    "Response validation of class %s failed",
                    response.getClass()
                ),
                violations
            );
        }

        return response;
    }
}
