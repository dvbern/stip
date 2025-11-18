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

import static ch.dvbern.stip.api.util.TestConstants.SOZIALDIENST_ADMIN_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "sozialdienst_admin",
    roles = {
        OidcConstants.ROLE_SOZIALDIENST_ADMIN,
        OidcConstants.ROLE_SOZIALDIENST_MITARBEITER,
        OidcPermissions.DOKUMENT_UPLOAD_GS,
        OidcPermissions.AUSZAHLUNG_READ,
        OidcPermissions.UNTERSCHRIFTENBLATT_READ,
        OidcPermissions.AUSBILDUNG_READ,
        OidcPermissions.BUCHHALTUNG_ENTRY_READ,
        OidcPermissions.AENDERUNG_DELETE,
        OidcPermissions.FALL_READ,
        OidcPermissions.AUSBILDUNGSSTAETTE_READ,
        OidcPermissions.DELEGIERUNG_READ,
        OidcPermissions.SOZIALDIENSTBENUTZER_CREATE,
        OidcPermissions.AUSZAHLUNG_CREATE,
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.AUSBILDUNG_UPDATE,
        OidcPermissions.SOZIALDIENSTBENUTZER_DELETE,
        OidcPermissions.SOZIALDIENSTBENUTZER_UPDATE,
        OidcPermissions.SOZIALDIENSTBENUTZER_READ,
        OidcPermissions.AUSBILDUNG_DELETE,
        OidcPermissions.AENDERUNG_CREATE,
        OidcPermissions.AUSZAHLUNG_UPDATE,
        OidcPermissions.AENDERUNG_EINREICHEN,
        OidcPermissions.DELEGIERUNG_UPDATE,
        OidcPermissions.DOKUMENT_DELETE_GS,
        OidcPermissions.NOTIFICATION_READ,
        OidcPermissions.DOKUMENT_READ,
        OidcPermissions.SOZIALDIENST_READ,
        OidcPermissions.DELEGIERUNG_CREATE,
        OidcPermissions.AUSBILDUNG_CREATE,
        OidcPermissions.STAMMDATEN_READ,
        OidcPermissions.FALL_CREATE,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = SOZIALDIENST_ADMIN_ID)
    }
)
public @interface TestAsSozialdienstAdmin {
}
