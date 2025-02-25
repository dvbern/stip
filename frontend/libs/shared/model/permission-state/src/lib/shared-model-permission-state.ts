import { AppType } from '@dv/shared/model/config';
import {
  GesuchTrancheStatus,
  GesuchUrlType,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { capitalized } from '@dv/shared/model/type-util';

const Permissions = {
  W: { index: 0, name: 'write' },
  V: { index: 1, name: 'viewVerfuegung' },
  D: { index: 2, name: 'uploadDocuments' },
  F: { index: 3, name: 'freigeben' },
  U: { index: 4, name: 'uploadUnterschriftenblatt' },
} as const;
type Permissions = typeof Permissions;
type PermissionFlag = keyof typeof Permissions;
type P<T extends PermissionFlag> = T | ' ';

/**
 * Permissions for the gesuch and tranche interactions
 *
 * * `W` - Write
 * * `V` - Verfuegung einsehen
 * * `D` - Dokumente hochladen
 * * `F` - Freigeben
 * * `U` - Unterschriftenblatt hochladen
 */
type PermissionFlags = `${P<'W'>}${P<'D'>}${P<'F'>}${P<'U'>}`;

export type Permission = Permissions[PermissionFlag]['name'];
export type PermissionMap = Partial<ReturnType<typeof getPermissions>>;

const hasPermission = (p: PermissionFlags, perm: keyof typeof Permissions) =>
  p.charAt(Permissions[perm].index) === perm;

/**
 * Transform a permission flags into a map of permissions with `can${Permission}` keys
 *
 * @example
 * ```ts
 * getPermissions('W  ') === {
 *   canWrite: true,
 *   canUploadDocuments: false,
 *   canFreigeben: false,
 *   canUploadUnterschriftenblatt: false
 * }
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
  IN_BEARBEITUNG_GS /**                */: { [gs]: 'WDF ', [sb]: '   U' },
  EINGEREICHT /**                      */: { [gs]: '    ', [sb]: '   U' },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: '    ', [sb]: '   U' },
  IN_BEARBEITUNG_SB /**                */: { [gs]: '    ', [sb]: 'W  U' },
  IN_FREIGABE /**                      */: { [gs]: '    ', [sb]: '   U' },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: '    ', [sb]: 'W  U' },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: '    ', [sb]: '   U' },
  FEHLENDE_DOKUMENTE /**               */: { [gs]: ' DF ', [sb]: '   U' },
  GESUCH_ABGELEHNT /**                 */: { [gs]: '    ', [sb]: '   U' },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: '    ', [sb]: '   U' },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: '    ', [sb]: '    ' },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: '    ', [sb]: '   U' },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: '    ', [sb]: '   U' },
  STIPENDIENANSPRUCH /**               */: { [gs]: '    ', [sb]: '    ' },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: '    ', [sb]: '   U' },
  VERSANDBEREIT /**                    */: { [gs]: '    ', [sb]: '    ' },
  VERFUEGT /**                         */: { [gs]: '    ', [sb]: '   U' },
  VERSENDET /**                        */: { [gs]: '    ', [sb]: '    ' },
  NEGATIVE_VERFUEGUNG /**              */: { [gs]: '    ', [sb]: '   U' },
} as const satisfies Record<Gesuchstatus, Record<AppType, PermissionFlags>>;

/**
 * Similar as `permissionTableByAppType` but for the tranches
 *
 * @see {@link permissionTableByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: 'WD  ', [sb]: '    ' },
  UEBERPRUEFEN: /**       */ { [gs]: '    ', [sb]: 'W   ' },
  AKZEPTIERT: /**         */ { [gs]: '    ', [sb]: '    ' },
  ABGELEHNT: /**          */ { [gs]: 'W   ', [sb]: '    ' },
  MANUELLE_AENDERUNG: /** */ { [gs]: '    ', [sb]: '    ' },
} as const satisfies Record<
  GesuchTrancheStatus,
  Record<AppType, PermissionFlags>
>;

export const preparePermissions = (
  trancheTyp: GesuchUrlType | null,
  gesuch: SharedModelGesuch | null,
  appType: AppType | undefined,
) => {
  if (!gesuch || !appType) return { readonly: false, permissions: {} };
  const gesuchPermissions = getGesuchPermissions(gesuch, appType);
  const tranchePermissions = getTranchePermissions(gesuch, appType);
  const permissions =
    trancheTyp === 'AENDERUNG' ? tranchePermissions : gesuchPermissions;
  const canWrite = permissions.canWrite ?? true;

  return {
    readonly: trancheTyp === 'INITIAL' || !canWrite,
    permissions,
  };
};

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
