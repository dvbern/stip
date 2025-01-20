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

public final class OidcConstants {

    public static final String CLAIM_AHV_NUMMER = "ahv_nummer";
    public static final String ROLE_GESUCHSTELLER = "Gesuchsteller";
    public static final String ROLE_SACHBEARBEITER = "Sachbearbeiter";
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_JURIST = "Jurist";
    public static final String ROLE_FREIGABESTELLE = "Freigabestelle";
    public static final String ROLE_SOZIALDIENST_MITARBEITER = "Sozialdienst-Mitarbeiter";
    public static final String ROLE_SOZIALDIENST_ADMIN = "Sozialdienst-Admin";

    public static final String REQUIRED_ACTION_UPDATE_PASSWORD = "UPDATE_PASSWORD";

    private OidcConstants() {}
}
