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

import static ch.dvbern.stip.api.util.TestConstants.SUPER_USER_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "superuser",
    roles = {
        OidcConstants.ROLE_SACHBEARBEITER_ADMIN,
        OidcConstants.ROLE_SACHBEARBEITER,
        OidcConstants.ROLE_SUPER_USER,
        OidcPermissions.BENUTZER_DELETE,
        OidcPermissions.STAMMDATEN_DELETE,
        OidcPermissions.BUCHSTABENZUWEISUNG_CREATE,
        OidcPermissions.SOZIALDIENST_CREATE,
        OidcPermissions.SOZIALDIENST_READ,
        OidcPermissions.SOZIALDIENST_UPDATE,
        OidcPermissions.SOZIALDIENST_DELETE,
        OidcPermissions.BUCHSTABENZUWEISUNG_UPDATE,
        OidcPermissions.BUCHSTABENZUWEISUNG_READ,
        OidcPermissions.SOZIALDIENSTBENUTZER_CREATE,
        OidcPermissions.SOZIALDIENSTBENUTZER_READ,
        OidcPermissions.SOZIALDIENSTBENUTZER_UPDATE,
        OidcPermissions.SOZIALDIENSTBENUTZER_DELETE,
        OidcPermissions.SEND_EMAIL,
        OidcPermissions.STAMMDATEN_CREATE,
        OidcPermissions.STAMMDATEN_UPDATE,
        OidcPermissions.ADMIN_GESUCH_DELETE,
        OidcPermissions.NOTIZ_READ,
        OidcPermissions.NOTIZ_DELETE,
        OidcPermissions.BUCHHALTUNG_ENTRY_CREATE,
        OidcPermissions.UNTERSCHRIFTENBLATT_READ,
        OidcPermissions.AUSBILDUNG_READ,
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.NOTIFICATION_READ,
        OidcPermissions.DOKUMENT_READ,
        OidcPermissions.BUCHSTABENZUWEISUNG_READ,
        OidcPermissions.FALL_READ,
        OidcPermissions.AUSBILDUNG_UPDATE,
        OidcPermissions.BUCHHALTUNG_ENTRY_READ,
        OidcPermissions.NOTIZ_CREATE,
        OidcPermissions.UNTERSCHRIFTENBLATT_UPLOAD,
        OidcPermissions.CUSTOM_DOKUMENT_CREATE,
        OidcPermissions.SB_GESUCH_READ,
        OidcPermissions.DOKUMENT_ABLEHNEN_AKZEPTIEREN,
        OidcPermissions.NOTIZ_UPDATE,
        OidcPermissions.UNTERSCHRIFTENBLATT_DELETE,
        OidcPermissions.STAMMDATEN_READ,
        OidcPermissions.SB_GESUCH_UPDATE,
        OidcPermissions.CUSTOM_DOKUMENT_DELETE,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = SUPER_USER_ID),
        @Claim(key = "family_name", value = "SuperUser"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsSuperUser {
}
