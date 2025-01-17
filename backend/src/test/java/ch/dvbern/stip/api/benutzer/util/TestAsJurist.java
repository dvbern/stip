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

import static ch.dvbern.stip.api.common.util.OidcConstants.CLAIM_AHV_NUMMER;
import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID;
import static ch.dvbern.stip.api.util.TestConstants.JURIST_ID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TestSecurity(
    user = "jurist",
    roles = {
        OidcConstants.ROLE_JURIST,
        "AUSBILDUNG_CREATE",
        "AUSBILDUNG_READ",
        "AUSBILDUNG_UPDATE",
        "AUSBILDUNG_DELETE",
        "GESUCH_READ"
    }
)
@OidcSecurity(
    claims = {
        @Claim(key = "sub", value = JURIST_ID),
        @Claim(key = CLAIM_AHV_NUMMER, value = AHV_NUMMER_VALID),
        @Claim(key = "family_name", value = "Gesuchsteller"),
        @Claim(key = "given_name", value = "Hans")
    }
)
public @interface TestAsJurist {
}
