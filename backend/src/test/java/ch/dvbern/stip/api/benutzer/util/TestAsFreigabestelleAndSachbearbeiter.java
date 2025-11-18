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

import static ch.dvbern.stip.api.util.TestConstants.FREIGABESTELLE_AND_SACHBEARBEITER_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "freigabestelle",
    roles = {
        OidcConstants.ROLE_FREIGABESTELLE,
        OidcConstants.ROLE_SACHBEARBEITER,
        OidcPermissions.FREIGABESTELLE_GESUCH_UPDATE,
        OidcPermissions.NOTIZ_READ,
        OidcPermissions.SB_GESUCH_READ,
        OidcPermissions.STAMMDATEN_READ,
        OidcPermissions.AUSBILDUNGSSTAETTE_READ,
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.DOKUMENT_READ,
        OidcPermissions.FALL_READ,
        OidcPermissions.NOTIZ_READ,
        OidcPermissions.NOTIZ_DELETE,
        OidcPermissions.BUCHHALTUNG_ENTRY_CREATE,
        OidcPermissions.UNTERSCHRIFTENBLATT_READ,
        OidcPermissions.AUSBILDUNG_READ,
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.NOTIFICATION_READ,
        OidcPermissions.DOKUMENT_READ,
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
        OidcPermissions.AUSBILDUNGSSTAETTE_READ,
        OidcPermissions.SB_GESUCH_UPDATE,
        OidcPermissions.CUSTOM_DOKUMENT_DELETE,
        OidcPermissions.BUCHSTABENZUWEISUNG_READ,
        OidcPermissions.DOKUMENT_UPLOAD_SB,
        OidcPermissions.DOKUMENT_DELETE_SB,
        OidcPermissions.AUSBILDUNGSSTAETTE_CREATE,
        OidcPermissions.AUSBILDUNGSSTAETTE_UPDATE,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = FREIGABESTELLE_AND_SACHBEARBEITER_ID)
    }
)
public @interface TestAsFreigabestelleAndSachbearbeiter {
}
