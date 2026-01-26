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
    public static final String GS_GESUCH_CREATE = "V0_GESUCH_CREATE_GS";
    public static final String GS_GESUCH_READ = "V0_GESUCH_READ_GS";
    public static final String GS_GESUCH_UPDATE = "V0_GESUCH_UPDATE_GS";
    public static final String GS_GESUCH_DELETE = "V0_GESUCH_DELETE_GS";

    public static final String SB_GESUCH_UPDATE = "V0_GESUCH_UPDATE_SB";
    public static final String SB_GESUCH_READ = "V0_GESUCH_READ_SB";

    public static final String FREIWILLIG_DARLEHEN_READ = "V0_FREIWILLIG_DARLEHEN_READ";
    public static final String FREIWILLIG_DARLEHEN_UPDATE_GS = "V0_FREIWILLIG_DARLEHEN_UPDATE_GS";
    public static final String FREIWILLIG_DARLEHEN_DELETE = "V0_FREIWILLIG_DARLEHEN_DELETE";
    public static final String FREIWILLIG_DARLEHEN_UPDATE_SB = "V0_FREIWILLIG_DARLEHEN_UPDATE_SB";
    public static final String FREIWILLIG_DARLEHEN_FREIGABESTELLE = "V0_FREIWILLIG_DARLEHEN_FREIGABESTELLE";

    public static final String FREIGABESTELLE_GESUCH_UPDATE = "V0_GESUCH_UPDATE_FREIGABESTELLE";

    public static final String JURIST_GESUCH_READ = "V0_GESUCH_READ_JURIST";
    public static final String JURIST_GESUCH_UPDATE = "V0_GESUCH_UPDATE_JURIST";

    public static final String ADMIN_GESUCH_DELETE = "V0_GESUCH_DELETE_ADMIN";
    public static final String DEMO_DATA_APPLY = "V0_DEMO_DATA_APPLY";

    public static final String AENDERUNG_CREATE = "V0_AENDERUNG_CREATE";
    public static final String AENDERUNG_DELETE = "V0_AENDERUNG_DELETE";
    public static final String AENDERUNG_EINREICHEN = "V0_AENDERUNG_EINREICHEN";

    public static final String AUSBILDUNG_CREATE = "V0_AUSBILDUNG_CREATE";
    public static final String AUSBILDUNG_READ = "V0_AUSBILDUNG_READ";
    public static final String AUSBILDUNG_UPDATE = "V0_AUSBILDUNG_UPDATE";
    public static final String AUSBILDUNG_DELETE = "V0_AUSBILDUNG_DELETE";

    public static final String BENUTZER_GET = "V0_BENUTZER_GET";
    public static final String BENUTZER_CREATE = "V0_BENUTZER_CREATE";
    public static final String BENUTZER_UPDATE = "V0_BENUTZER_UPDATE";
    public static final String BENUTZER_DELETE = "V0_BENUTZER_DELETE";

    public static final String BUCHHALTUNG_ENTRY_CREATE = "V0_BUCHHALTUNG_ENTRY_CREATE";
    public static final String BUCHHALTUNG_ENTRY_READ = "V0_BUCHHALTUNG_ENTRY_READ";

    public static final String BUCHSTABENZUWEISUNG_CREATE = "V0_BUCHSTABENZUWEISUNG_CREATE";
    public static final String BUCHSTABENZUWEISUNG_READ = "V0_BUCHSTABENZUWEISUNG_READ";
    public static final String BUCHSTABENZUWEISUNG_UPDATE = "V0_BUCHSTABENZUWEISUNG_UPDATE";

    public static final String CUSTOM_DOKUMENT_CREATE = "V0_CUSTOM_DOKUMENT_CREATE";
    public static final String CUSTOM_DOKUMENT_READ = "V0_CUSTOM_DOKUMENT_READ";
    public static final String CUSTOM_DOKUMENT_DELETE = "V0_CUSTOM_DOKUMENT_DELETE";

    public static final String DOKUMENT_ABLEHNEN_AKZEPTIEREN = "V0_DOKUMENT_ABLEHNEN_AKZEPTIEREN";
    public static final String DOKUMENT_READ = "V0_DOKUMENT_READ";
    public static final String DOKUMENT_UPLOAD_GS = "V0_DOKUMENT_UPLOAD_GS";
    public static final String DOKUMENT_DELETE_GS = "V0_DOKUMENT_DELETE_GS";
    public static final String DOKUMENT_UPLOAD_SB = "V0_DOKUMENT_UPLOAD_SB";
    public static final String DOKUMENT_DELETE_SB = "V0_DOKUMENT_DELETE_SB";

    public static final String FALL_CREATE = "V0_FALL_CREATE";
    public static final String FALL_READ = "V0_FALL_READ";

    public static final String NOTIFICATION_READ = "V0_NOTIFICATION_READ";

    public static final String NOTIZ_CREATE = "V0_NOTIZ_CREATE";
    public static final String NOTIZ_READ = "V0_NOTIZ_READ";
    public static final String NOTIZ_UPDATE = "V0_NOTIZ_UPDATE";
    public static final String NOTIZ_DELETE = "V0_NOTIZ_DELETE";

    public static final String SEND_EMAIL = "V0_SEND_EMAIL";

    public static final String SOZIALDIENST_CREATE = "V0_SOZIALDIENST_CREATE";
    public static final String SOZIALDIENST_READ = "V0_SOZIALDIENST_READ";
    public static final String SOZIALDIENST_UPDATE = "V0_SOZIALDIENST_UPDATE";
    public static final String SOZIALDIENST_DELETE = "V0_SOZIALDIENST_DELETE";

    public static final String SOZIALDIENSTBENUTZER_CREATE = "V0_SOZIALDIENSTBENUTZER_CREATE";
    public static final String SOZIALDIENSTBENUTZER_READ = "V0_SOZIALDIENSTBENUTZER_READ";
    public static final String SOZIALDIENSTBENUTZER_UPDATE = "V0_SOZIALDIENSTBENUTZER_UPDATE";
    public static final String SOZIALDIENSTBENUTZER_DELETE = "V0_SOZIALDIENSTBENUTZER_DELETE";

    public static final String AUSBILDUNGSSTAETTE_CREATE = "V0_AUSBILDUNGSSTAETTE_CREATE";
    public static final String AUSBILDUNGSSTAETTE_READ = "V0_AUSBILDUNGSSTAETTE_READ";
    public static final String AUSBILDUNGSSTAETTE_UPDATE = "V0_AUSBILDUNGSSTAETTE_UPDATE";
    public static final String AUSBILDUNGSSTAETTE_DELETE = "V0_AUSBILDUNGSSTAETTE_DELETE";

    public static final String STAMMDATEN_CREATE = "V0_STAMMDATEN_CREATE";
    public static final String STAMMDATEN_READ = "V0_STAMMDATEN_READ";
    public static final String STAMMDATEN_UPDATE = "V0_STAMMDATEN_UPDATE";
    public static final String STAMMDATEN_DELETE = "V0_STAMMDATEN_DELETE";

    public static final String UNTERSCHRIFTENBLATT_READ = "V0_UNTERSCHRIFTENBLATT_READ";
    public static final String UNTERSCHRIFTENBLATT_UPLOAD = "V0_UNTERSCHRIFTENBLATT_CREATE";
    public static final String UNTERSCHRIFTENBLATT_DELETE = "V0_UNTERSCHRIFTENBLATT_DELETE";

    public static final String DELEGIERUNG_CREATE = "V0_DELEGIERUNG_CREATE";
    public static final String DELEGIERUNG_READ = "V0_DELEGIERUNG_READ";
    public static final String DELEGIERUNG_UPDATE = "V0_DELEGIERUNG_UPDATE";

    public static final String AUSZAHLUNG_CREATE = "V0_AUSZAHLUNG_CREATE";
    public static final String AUSZAHLUNG_UPDATE = "V0_AUSZAHLUNG_UPDATE";
    public static final String AUSZAHLUNG_READ = "V0_AUSZAHLUNG_READ";

    private OidcPermissions() {

    }
}
