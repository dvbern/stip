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

public final class DokumentDownloadConstants {
    private DokumentDownloadConstants() {}

    public static final String GESUCH_DOKUMENT_PATH = "gesuch/";

    public static final String GESUCH_ID_CLAIM = "gesuch_id";
    public static final String DOKUMENT_ID_CLAIM = "dokument_id";
    public static final String DARLEHEN_ID_CLAIM = "darlehen_id";
    public static final String VERFUEGUN_DOKUMENT_ID_CLAIM = "verfuegung_dokument_id";
    public static final String MASSENDRUCK_JOB_ID_CLAIM = "massendruck_id";
    public static final String DEMO_DATA_IMPORT_ID_CLAIM = "demo_data_import_id";
}
