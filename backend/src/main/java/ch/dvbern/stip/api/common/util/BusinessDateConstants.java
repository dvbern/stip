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

import java.time.ZoneId;

public final class BusinessDateConstants {
    public static final ZoneId ZUERICH_ZONE = ZoneId.of("Europe/Zurich");

    public static final int MIN_AGE_EIGENER_WOHNSITZ = 20;
    public static final int VERSPAETET_EINGEREICHT_STICHTAG = 15;
    public static final int MAX_AGE_AUSBILDUNGSBEGIN = 35;
    public static final int PIA_GEBURTSDATUM_STICHTAG_MIN_AGE = 16;
    public static final int PIA_GEBURTSDATUM_STICHTAG_MONTH = 8;
    public static final int PIA_GEBURTSDATUM_STICHTAG_DAY = 1;
}
