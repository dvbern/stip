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

package ch.dvbern.stip.api.benutzer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.dvbern.stip.api.common.util.OidcConstants;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "admin",
    roles = {
        OidcConstants.ROLE_ADMIN,
        "GESUCHSPERIODE_DELETE",
        "GESUCHSPERIODE_UPDATE",
        "GESUCHSPERIODE_CREATE",
        "GESUCHSPERIODE_READ",
        "GESUCH_READ",
        "GESUCH_UPDATE",
        "FALL_UPDATE",
        "GESUCH_CREATE",
        "FALL_CREATE",
        "GESUCHSPERIODE_READ",
        "GESUCH_DELETE",
        "FALL_DELETE",
        "FALL_READ",
        "STAMMDATEN_CREATE",
        "STAMMDATEN_DELETE",
        "STAMMDATEN_READ",
        "STAMMDATEN_UPDATE",
        "SEND_EMAIL"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = "c1dd0d38-0beb-4694-af37-10bb7da5b12a"),
        @Claim(key = "family_name", value = "Admin"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsAdmin {
}
