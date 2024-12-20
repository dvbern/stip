import { AppType } from '@dv/shared/model/config';
import { GesuchTrancheStatus, Gesuchstatus } from '@dv/shared/model/gesuch';
import { capitalized } from '@dv/shared/model/type-util';

const Permissions = {
  W: { index: 0, name: 'write' },
  V: { index: 1, name: 'viewVerfuegung' },
  D: { index: 2, name: 'uploadMissingDocuments' },
  F: { index: 3, name: 'freigeben' },
} as const;
type Permissions = typeof Permissions;
type PermissionFlag = keyof typeof Permissions;
type P<T extends PermissionFlag> = T | ' ';

/**
 * Permissions for the gesuch and tranche interactions
 *
 * * `W` - Write
 * * `V` - Verfuegung einsehen
 * * `D` - Fehlende Dokumente hochladen
 * * `F` - Freigeben
 */
type PermissionFlags = `${P<'W'>}${P<'V'>}${P<'D'>}${P<'F'>}`;

export type Permission = Permissions[PermissionFlag]['name'];
export type PermissionMap = Partial<ReturnType<typeof getPermissions>>;

const hasPermission = (p: PermissionFlags, perm: keyof typeof Permissions) =>
  p.charAt(Permissions[perm].index) === perm;

/**
 * Transform a permission flags into a map of permissions with `can${Permission}` keys
 *
 * @example
 * ```ts
 * getPermissions('WV  ') === { canWrite: true, canViewVerfuegung: true, canUploadMissingDocuments: false, canFreigeben: false }
 * ```
 */
const getPermissions = (permission: PermissionFlags) => {
  return (Object.keys(Permissions) as PermissionFlag[]).reduce(
    (acc, perm) => {
      acc[`can${capitalized(Permissions[perm].name)}`] = hasPermission(
        permission,
        perm,
      );
      return acc;
    },
    {} as Record<`can${Capitalize<Permission>}`, boolean>,
  );
};

const gs = 'gesuch-app' satisfies AppType;
const sb = 'sachbearbeitung-app' satisfies AppType;

/**
 * Define the permissions for the gesuch based on the status.
 *
 * * Format is: { [Gesuchstatus]: { [AppType]: 'WV  ' | 'W   ' | ..., ... other AppTypes } }
 */
export const permissionTableByAppType = {
  IN_BEARBEITUNG_GS /**                */: { [gs]: 'W  F', [sb]: '    ' },
  EINGEREICHT /**                      */: { [gs]: '    ', [sb]: '    ' },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: '    ', [sb]: ' V  ' },
  IN_BEARBEITUNG_SB /**                */: { [gs]: '    ', [sb]: 'WV  ' },
  IN_FREIGABE /**                      */: { [gs]: '    ', [sb]: ' V  ' },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: '    ', [sb]: 'WV  ' },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: '    ', [sb]: '    ' },
  FEHLENDE_DOKUMENTE /**               */: { [gs]: '  DF', [sb]: '    ' },
  GESUCH_ABGELEHNT /**                 */: { [gs]: '    ', [sb]: ' V  ' },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: '    ', [sb]: ' V  ' },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: '    ', [sb]: ' V  ' },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: '    ', [sb]: ' V  ' },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: '    ', [sb]: ' V  ' },
  STIPENDIENANSPRUCH /**               */: { [gs]: '    ', [sb]: ' V  ' },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: '    ', [sb]: ' V  ' },
  VERSANDBEREIT /**                    */: { [gs]: '    ', [sb]: ' V  ' },
  VERFUEGT /**                         */: { [gs]: '    ', [sb]: ' V  ' },
  VERSENDET /**                        */: { [gs]: '    ', [sb]: ' V  ' },
  NEGATIVE_VERFUEGUNG /**              */: { [gs]: '    ', [sb]: '    ' },
} as const satisfies Record<Gesuchstatus, Record<AppType, PermissionFlags>>;

/**
 * Similar as `permissionTableByAppType` but for the tranches
 *
 * @see {@link permissionTableByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: 'W   ', [sb]: '    ' },
  UEBERPRUEFEN: /**       */ { [gs]: '    ', [sb]: 'W   ' },
  AKZEPTIERT: /**         */ { [gs]: '    ', [sb]: '    ' },
  ABGELEHNT: /**          */ { [gs]: 'W   ', [sb]: '    ' },
  MANUELLE_AENDERUNG: /** */ { [gs]: '    ', [sb]: '    ' },
} as const satisfies Record<
  GesuchTrancheStatus,
  Record<AppType, PermissionFlags>
>;

/**
 * Get the permissions for the gesuch based on the status and the app type
 */
export const getGesuchPermissions = (
  gesuch: { gesuchStatus: Gesuchstatus } | null,
  appType: AppType | undefined,
): PermissionMap => {
  if (!gesuch || !appType) return {};

  const state = permissionTableByAppType[gesuch.gesuchStatus][appType];
  return getPermissions(state);
};

/**
 * Get the permissions for the tranche based on the status and the app type
 */
export const getTranchePermissions = (
  gesuch: { gesuchTrancheToWorkWith: { status: GesuchTrancheStatus } } | null,
  appType?: AppType,
): PermissionMap => {
  if (!gesuch || !appType) return {};

  const state =
    trancheReadWritestatusByAppType[gesuch.gesuchTrancheToWorkWith.status][
      appType
    ];
  return getPermissions(state);
};
