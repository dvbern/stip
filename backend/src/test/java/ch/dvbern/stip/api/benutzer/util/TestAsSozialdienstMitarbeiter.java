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
import ch.dvbern.stip.api.common.util.OidcPermissions;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;

import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_MITARBEITER_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "sozialdienst_mitarbeiter",
    roles = {
        OidcConstants.ROLE_SOZIALDIENST_MITARBEITER,
        OidcPermissions.AENDERUNG_CREATE,
        OidcPermissions.UNTERSCHRIFTENBLATT_READ,
        OidcPermissions.GS_GESUCH_READ,
        OidcPermissions.AUSBILDUNG_READ,
        OidcPermissions.NOTIFICATION_READ,
        OidcPermissions.DOKUMENT_READ,
        OidcPermissions.GS_GESUCH_DELETE,
        OidcPermissions.FALL_READ,
        OidcPermissions.GS_GESUCH_UPDATE,
        OidcPermissions.AUSBILDUNG_CREATE,
        OidcPermissions.AUSBILDUNG_UPDATE,
        OidcPermissions.AUSBILDUNG_DELETE,
        OidcPermissions.GS_GESUCH_CREATE,
        OidcPermissions.AENDERUNG_EINREICHEN,
        OidcPermissions.STAMMDATEN_READ,
        OidcPermissions.DOKUMENT_DELETE,
        OidcPermissions.FALL_CREATE,
        OidcPermissions.DELEGIERUNG_READ,
        OidcPermissions.DELEGIERUNG_UPDATE,
        OidcPermissions.AUSZAHLUNG_CREATE,
        OidcPermissions.AUSZAHLUNG_UPDATE,
        OidcPermissions.AUSZAHLUNG_READ,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = SOZIALDIENST_MITARBEITER_ID),
        @Claim(key = "family_name", value = "Sozialdienst_Mitarbeiter"),
        @Claim(key = "given_name", value = "Max")
    }
)
public @interface TestAsSozialdienstMitarbeiter {
}
