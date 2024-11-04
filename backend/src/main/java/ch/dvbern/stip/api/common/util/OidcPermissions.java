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

public final class OidcPermissions {
    public static final String GESUCH_CREATE = "GESUCH_CREATE";
    public static final String GESUCH_READ = "GESUCH_READ";
    public static final String GESUCH_UPDATE = "GESUCH_UPDATE";
    public static final String GESUCH_DELETE = "GESUCH_DELETE";

    public static final String FALL_CREATE = "FALL_CREATE";
    public static final String FALL_READ = "FALL_READ";
    public static final String FALL_UPDATE = "FALL_UPDATE";
    public static final String FALL_DELETE = "FALL_DELETE";

    public static final String AUSBILDUNG_CREATE = "AUSBILDUNG_CREATE";
    public static final String AUSBILDUNG_DELETE = "AUSBILDUNG_DELETE";
    public static final String AUSBILDUNG_READ = "AUSBILDUNG_READ";
    public static final String AUSBILDUNG_UPDATE = "AUSBILDUNG_UPDATE";

    public static final String STAMMDATEN_CREATE = "STAMMDATEN_CREATE";
    public static final String STAMMDATEN_DELETE = "STAMMDATEN_DELETE";
    public static final String STAMMDATEN_READ = "STAMMDATEN_READ";
    public static final String STAMMDATEN_UPDATE = "STAMMDATEN_UPDATE";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    private OidcPermissions() {

    }
}
