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

public final class Constants {

    public static final int DB_DEFAULT_STRING_SMALL_LENGTH = 20;
    public static final int DB_DEFAULT_STRING_MEDIUM_LENGTH = 255;
    public static final int DB_DEFAULT_STRING_INPUT_MAX_LENGTH = 2000;
    public static final int DB_DEFAULT_STRING_MAX_LENGTH = 2200;
    public static final int DB_DEFAULT_STRING_ISO3CODE_LENGTH = 3;
    public static final int DB_DEFAULT_STRING_ISO2CODE_LENGTH = 2;
    public static final int DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH = 100_000;

    public static final int MAX_AGE_AUSBILDUNGSBEGIN = 35;
    public static final int FALL_GESUCH_NUMBER_GEN_SEED = 184932;

    public static final float CH_STEUERN_PERCENTAGE = 0.1f;
    public static final int CH_STEUERN_EINKOMMEN_LIMIT = 20000;

    public static final String DVB_MAILBUCKET_MAIL = "stip@mailbucket.dvbern.ch";
    public static final String VERANLAGUNGSSTATUS_DEFAULT_VALUE = "Manuell";

    private Constants() {
        throw new IllegalStateException("Constants class");
    }
}
