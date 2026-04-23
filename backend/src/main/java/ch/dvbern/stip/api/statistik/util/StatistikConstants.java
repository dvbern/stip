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

package ch.dvbern.stip.api.statistik.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StatistikConstants {
    public static final String STATISTIK_JOB_PREFIX = "statistikCSVJob-";
    public static final String STATISTIK_JOB_YEAR_KEY = "year";

    public static final String STATISTIK_XML_ENCODING = "ISO-8859-1";
    public static final String STATISTIK_XML_PERSON_ID_CATEGORY = "CH.AHV";
    public static final String STATISTIK_XML_SCHEMA_PATH = "/statistik/bfs_statistik_definition.xsd";

    public static final String STATISTIK_FILE_PATH = "statistik/";
    public static final String STATISTIK_FILE_PREFIX = "statistik_";
    public static final String STATISTIK_FILE_EXTENSION = ".xml";

    public static final String STATISTIK_FILE_DOWNLOAD_TOKEN_CLAIM_ID = "statistik";
}
