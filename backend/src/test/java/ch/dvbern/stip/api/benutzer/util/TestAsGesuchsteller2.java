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

import static ch.dvbern.stip.api.common.util.OidcConstants.CLAIM_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_2_TEST_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_2_TEST_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "gesuchsteller2",
    roles = {
        OidcConstants.ROLE_GESUCHSTELLER,
        OidcPermissions.DOKUMENT_UPLOAD,
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
        OidcPermissions.CUSTOM_DOKUMENT_READ,
        OidcPermissions.AENDERUNG_DELETE,
        "default-roles-bern"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = GESUCHSTELLER_2_TEST_ID),
        @Claim(key = CLAIM_AHV_NUMMER, value = GESUCHSTELLER_2_TEST_AHV_NUMMER),
        @Claim(key = "family_name", value = "Gesuchsteller 2"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsGesuchsteller2 {
}
