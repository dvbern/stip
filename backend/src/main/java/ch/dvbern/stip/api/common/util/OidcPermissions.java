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
    public static final String GS_GESUCH_CREATE = "GESUCH_CREATE_GS";
    public static final String GS_GESUCH_READ = "GESUCH_READ_GS";
    public static final String GS_GESUCH_UPDATE = "GESUCH_UPDATE_GS";
    public static final String GS_GESUCH_DELETE = "GESUCH_DELETE_GS";

    public static final String SB_GESUCH_UPDATE = "GESUCH_UPDATE_SB";
    public static final String SB_GESUCH_READ = "GESUCH_READ_SB";

    public static final String JURIST_GESUCH_READ = "GESUCH_READ_JURIST";

    // TODO KSTIP-1967: Is this needed?
    public static final String ADMIN_GESUCH_DELETE = "GESUCH_DELETE_ADMIN";

    public static final String AENDERUNG_CREATE = "AENDERUNG_CREATE";
    public static final String AENDERUNG_DELETE = "AENDERUNG_DELETE";
    public static final String AENDERUNG_EINREICHEN = "AENDERUNG_EINREICHEN";

    public static final String AUSBILDUNG_CREATE = "AUSBILDUNG_CREATE";
    public static final String AUSBILDUNG_READ = "AUSBILDUNG_READ";
    public static final String AUSBILDUNG_UPDATE = "AUSBILDUNG_UPDATE";
    public static final String AUSBILDUNG_DELETE = "AUSBILDUNG_DELETE";

    public static final String BENUTZER_DELETE = "BENUTZER_DELETE";

    public static final String BUCHHALTUNG_ENTRY_CREATE = "BUCHHALTUNG_ENTRY_CREATE";
    public static final String BUCHHALTUNG_ENTRY_READ = "BUCHHALTUNG_ENTRY_CREATE";

    public static final String BUCHSTABENZUWEISUNG_CREATE = "BUCHSTABENZUWEISUNG_CREATE";
    public static final String BUCHSTABENZUWEISUNG_READ = "BUCHSTABENZUWEISUNG_READ";
    public static final String BUCHSTABENZUWEISUNG_UPDATE = "BUCHSTABENZUWEISUNG_UPDATE";

    public static final String CUSTOM_DOKUMENT_CREATE = "CUSTOM_DOKUMENT_CREATE";
    public static final String CUSTOM_DOKUMENT_READ = "CUSTOM_DOKUMENT_READ";
    public static final String CUSTOM_DOKUMENT_DELETE = "CUSTOM_DOKUMENT_DELETE";

    public static final String DOKUMENT_ABLEHNEN_AKZEPTIEREN = "DOKUMENT_ABLEHNEN_AKZEPTIEREN";
    public static final String DOKUMENT_READ = "DOKUMENT_READ";
    public static final String DOKUMENT_UPLOAD = "DOKUMENT_UPLOAD";
    public static final String DOKUMENT_DELETE = "DOKUMENT_DELETE";

    public static final String FALL_CREATE = "FALL_CREATE";
    public static final String FALL_READ = "FALL_READ";

    public static final String NOTIFICATION_READ = "NOTIFICATION_READ";

    public static final String NOTIZ_CREATE = "NOTIZ_CREATE";
    public static final String NOTIZ_READ = "NOTIZ_READ";
    public static final String NOTIZ_UPDATE = "NOTIZ_UPDATE";
    public static final String NOTIZ_DELETE = "NOTIZ_DELETE";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    public static final String SOZIALDIENST_CREATE = "SOZIALDIENST_CREATE";
    public static final String SOZIALDIENST_READ = "SOZIALDIENST_READ";
    public static final String SOZIALDIENST_UPDATE = "SOZIALDIENST_UPDATE";
    public static final String SOZIALDIENST_DELETE = "SOZIALDIENST_DELETE";

    public static final String SOZIALDIENSTBENUTZER_CREATE = "SOZIALDIENSTBENUTZER_CREATE";
    public static final String SOZIALDIENSTBENUTZER_READ = "SOZIALDIENSTBENUTZER_READ";
    public static final String SOZIALDIENSTBENUTZER_UPDATE = "SOZIALDIENSTBENUTZER_UPDATE";
    public static final String SOZIALDIENSTBENUTZER_DELETE = "SOZIALDIENSTBENUTZER_DELETE";

    public static final String STAMMDATEN_CREATE = "STAMMDATEN_CREATE";
    public static final String STAMMDATEN_READ = "STAMMDATEN_READ";
    public static final String STAMMDATEN_UPDATE = "STAMMDATEN_UPDATE";
    public static final String STAMMDATEN_DELETE = "STAMMDATEN_DELETE";

    public static final String UNTERSCHRIFTENBLATT_READ = "UNTERSCHRIFTENBLATT_READ";
    public static final String UNTERSCHRIFTENBLATT_UPLOAD = "UNTERSCHRIFTENBLATT_CREATE";
    public static final String UNTERSCHRIFTENBLATT_DELETE = "UNTERSCHRIFTENBLATT_DELETE";

    private OidcPermissions() {

    }
}
