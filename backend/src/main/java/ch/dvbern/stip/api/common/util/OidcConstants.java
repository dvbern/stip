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

package ch.dvbern.stip.api.common.util;

import java.util.Collections;
import java.util.Set;

public final class OidcConstants {
    public static final String ROLE_GESUCHSTELLER = "V0_Gesuchsteller";
    public static final String ROLE_SACHBEARBEITER = "V0_Sachbearbeiter";
    public static final String ROLE_ADMIN = "V0_Sachbearbeiter-Admin";
    public static final String ROLE_JURIST = "V0_Jurist";
    public static final String ROLE_FREIGABESTELLE = "Freigabestelle";
    public static final String ROLE_SOZIALDIENST_MITARBEITER = "V0_Sozialdienst-Mitarbeiter";
    public static final String ROLE_SOZIALDIENST_ADMIN = "V0_Sozialdienst-Admin";
    public static final String ROLE_SUPER_USER = "V0_Super-User";

    public static final String REQUIRED_ACTION_UPDATE_PASSWORD = "UPDATE_PASSWORD";
    public static final Set<String> POSSIBLE_SB_ROLES = Collections.unmodifiableSet(
        Set.of(
            ROLE_SACHBEARBEITER,
            ROLE_ADMIN,
            ROLE_JURIST
        )
    );

    public static final Set<String> POSSIBLE_USER_ROLES = Collections.unmodifiableSet(
        Set.of(
            ROLE_SACHBEARBEITER,
            ROLE_ADMIN,
            ROLE_JURIST,
            ROLE_SOZIALDIENST_MITARBEITER,
            ROLE_SOZIALDIENST_ADMIN
        )
    );

    private OidcConstants() {}
}
