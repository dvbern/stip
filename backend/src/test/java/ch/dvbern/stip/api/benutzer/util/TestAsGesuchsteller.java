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

import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_TEST_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "gesuchsteller",
    roles = {
        OidcConstants.ROLE_GESUCHSTELLER,
        OidcPermissions.DOKUMENT_UPLOAD_GS,
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
        OidcPermissions.AUSBILDUNGSSTAETTE_READ,
        OidcPermissions.DOKUMENT_DELETE_GS,
        OidcPermissions.FALL_CREATE,
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.AENDERUNG_DELETE,
        OidcPermissions.SOZIALDIENST_READ,
        OidcPermissions.DELEGIERUNG_CREATE,
        OidcPermissions.AUSZAHLUNG_CREATE,
        OidcPermissions.AUSZAHLUNG_UPDATE,
        OidcPermissions.AUSZAHLUNG_READ,
        OidcPermissions.FREIWILLIG_DARLEHEN_READ,
        OidcPermissions.FREIWILLIG_DARLEHEN_UPDATE_GS,
        OidcPermissions.FREIWILLIG_DARLEHEN_DELETE,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = GESUCHSTELLER_TEST_ID)
    }
)
public @interface TestAsGesuchsteller {
}
