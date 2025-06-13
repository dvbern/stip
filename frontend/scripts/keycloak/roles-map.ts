import { Role } from './types';

/**
 * Represents the association between roles and permissions.
 *
 * Some roles are mentioned multiple times in the list because
 * they sometimes share different permissions with other roles.
 *
 * Structure:
 * - Each entry should contain a group of roles and their associated permissions.
 *   - The first elements represent the roles.
 *   - Following elements represent the permissions.
 */
export const PERMISSION_ROLE_LIST = [
  [['Gesuchsteller'], 'DELEGIERUNG_CREATE'],
  [
    ['Gesuchsteller', 'Sozialdienst-Mitarbeiter'],
    'FALL_CREATE',
    'FALL_READ',
    'GESUCH_CREATE_GS',
    'GESUCH_READ_GS',
    'GESUCH_UPDATE_GS',
    'GESUCH_DELETE_GS',
    'AENDERUNG_CREATE',
    'AENDERUNG_DELETE',
    'AENDERUNG_EINREICHEN',
    'SOZIALDIENST_READ',
    'AUSBILDUNGSSTAETTE_READ',
    'AUSBILDUNG_CREATE',
    'AUSBILDUNG_READ',
    'AUSBILDUNG_UPDATE',
    'AUSBILDUNG_DELETE',
    'DOKUMENT_READ',
    'DOKUMENT_UPLOAD',
    'DOKUMENT_DELETE',
    'CUSTOM_DOKUMENT_READ',
    'NOTIFICATION_READ',
    'UNTERSCHRIFTENBLATT_READ',
    'AUSZAHLUNG_CREATE',
    'AUSZAHLUNG_UPDATE',
    'AUSZAHLUNG_READ',
  ],
  [
    ['Sachbearbeiter'],
    'BUCHSTABENZUWEISUNG_READ',
    'AUSBILDUNGSSTAETTE_READ',
    'GESUCH_UPDATE_SB',
    'GESUCH_READ_SB',
    'BUCHHALTUNG_ENTRY_CREATE',
    'CUSTOM_DOKUMENT_CREATE',
    'CUSTOM_DOKUMENT_DELETE',
    'DOKUMENT_ABLEHNEN_AKZEPTIEREN',
    'UNTERSCHRIFTENBLATT_CREATE',
    'UNTERSCHRIFTENBLATT_DELETE',
    'AUSZAHLUNG_READ',
  ],
  [
    ['Jurist'],
    'AUSBILDUNGSSTAETTE_READ',
    'AUSBILDUNGSSTAETTE_CREATE',
    'AUSBILDUNGSSTAETTE_UPDATE',
    'AUSBILDUNGSSTAETTE_DELETE',
    'STAMMDATEN_READ',
    'GESUCH_READ_JURIST',
    'GESUCH_UPDATE_JURIST',
  ],
  [['Super-User'], 'GESUCH_DELETE_ADMIN'],
  [
    ['Sozialdienst-Admin', 'Sozialdienst-Mitarbeiter'],
    'SOZIALDIENSTBENUTZER_READ',
    'DELEGIERUNG_READ',
    'DELEGIERUNG_UPDATE',
  ],
  [
    ['Jurist', 'Sachbearbeiter'],
    'NOTIZ_CREATE',
    'NOTIZ_READ',
    'NOTIZ_UPDATE',
    'NOTIZ_DELETE',
    'SEND_EMAIL',
    'BUCHHALTUNG_ENTRY_READ',
  ],
  [
    ['Sozialdienst-Admin', 'Sachbearbeiter-Admin'],
    'STAMMDATEN_READ',
    'SOZIALDIENSTBENUTZER_CREATE',
    'SOZIALDIENSTBENUTZER_READ',
    'SOZIALDIENSTBENUTZER_UPDATE',
    'SOZIALDIENSTBENUTZER_DELETE',
  ],
  [
    ['Sachbearbeiter-Admin'],
    'Sachbearbeiter',
    'BENUTZER_DELETE',
    'AUSBILDUNGSSTAETTE_READ',
    'AUSBILDUNGSSTAETTE_CREATE',
    'AUSBILDUNGSSTAETTE_UPDATE',
    'AUSBILDUNGSSTAETTE_DELETE',
    'BUCHSTABENZUWEISUNG_CREATE',
    'BUCHSTABENZUWEISUNG_READ',
    'BUCHSTABENZUWEISUNG_UPDATE',
    'SOZIALDIENST_CREATE',
    'SOZIALDIENST_READ',
    'SOZIALDIENST_UPDATE',
    'SOZIALDIENST_DELETE',
    'STAMMDATEN_CREATE',
    'STAMMDATEN_READ',
    'STAMMDATEN_UPDATE',
    'STAMMDATEN_DELETE',
    'SEND_EMAIL',
  ],
] as const satisfies [Role[], ...string[]][];
