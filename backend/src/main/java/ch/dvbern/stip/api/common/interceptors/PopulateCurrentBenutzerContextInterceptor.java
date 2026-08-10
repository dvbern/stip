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

import java.util.Objects;

import ch.dvbern.stip.api.benutzer.entity.CurrentBenutzerContext;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.JwtUtil;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

@PopulateCurrentBenutzerContext
@Interceptor
public class PopulateCurrentBenutzerContextInterceptor {
    @Inject
    BenutzerService benutzerService;

    @Inject
    Instance<JsonWebToken> token;

    @Inject
    CurrentBenutzerContext currentBenutzerContext;

    @AroundInvoke
    Object populate(final InvocationContext context) throws Exception {
        final var currentBenutzer = benutzerService.getCurrentBenutzerNoThrow();

        if (Objects.nonNull(currentBenutzer)) {
            currentBenutzerContext.setCurrentBenutzer(currentBenutzer.getId(), currentBenutzer.getFullName());
        } else {
            String currentBenutzerFullName = JwtUtil.extractUsernameFromJwt(token);
            currentBenutzerContext.setCurrentBenutzer(null, currentBenutzerFullName);
        }
        return context.proceed();
    }
}
