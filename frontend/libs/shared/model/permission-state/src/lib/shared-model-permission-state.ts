import { RolesMap } from '@dv/shared/model/benutzer';
import { AppType } from '@dv/shared/model/config';
import {
  Delegierung,
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
type PermissionFlags = `${P<'W'>}${P<'V'>}${P<'D'>}${P<'F'>}${P<'U'>}`;

export type Permission = Permissions[PermissionFlag]['name'];
export type PermissionMap = Partial<ReturnType<typeof parsePermissions>>;

const hasPermission = (p: PermissionFlags, perm: keyof typeof Permissions) =>
  p.charAt(Permissions[perm].index) === perm;

/**
 * Transform a permission flags into a map of permissions with `can${Permission}` keys
 *
 * @example
 * ```ts
 * getPermissions('WV  ') === {
 *   canWrite: true,
 *   canViewVerfuegung: true,
 *   canUploadDocuments: false,
 *   canFreigeben: false,
 *   canUploadUnterschriftenblatt: false
 * }
 * ```
 */
const parsePermissions = (permission: PermissionFlags) => {
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
  IN_BEARBEITUNG_GS /**                */: { [gs]: 'W DF ', [sb]: '    U' },
  EINGEREICHT /**                      */: { [gs]: '     ', [sb]: ' V  U' },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: '     ', [sb]: ' V  U' },
  IN_BEARBEITUNG_SB /**                */: { [gs]: '     ', [sb]: 'WV  U' },
  IN_FREIGABE /**                      */: { [gs]: '     ', [sb]: ' V  U' },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: '     ', [sb]: 'WV  U' },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: '     ', [sb]: ' V  U' },
  FEHLENDE_DOKUMENTE /**               */: { [gs]: '  DF ', [sb]: ' V  U' },
  GESUCH_ABGELEHNT /**                 */: { [gs]: '     ', [sb]: ' V  U' },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: '     ', [sb]: ' V  U' },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: '     ', [sb]: ' V   ' },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: '     ', [sb]: ' V  U' },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: '     ', [sb]: ' V  U' },
  STIPENDIENANSPRUCH /**               */: { [gs]: '     ', [sb]: ' V   ' },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: '     ', [sb]: ' V  U' },
  VERSANDBEREIT /**                    */: { [gs]: '     ', [sb]: ' V   ' },
  VERFUEGT /**                         */: { [gs]: '     ', [sb]: ' V  U' },
  VERSENDET /**                        */: { [gs]: '     ', [sb]: ' V   ' },
  NEGATIVE_VERFUEGUNG /**              */: { [gs]: '     ', [sb]: ' V  U' },
} as const satisfies Record<Gesuchstatus, Record<AppType, PermissionFlags>>;

/**
 * Similar as `permissionTableByAppType` but for the tranches
 *
 * @see {@link permissionTableByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: 'W D  ', [sb]: '     ' },
  UEBERPRUEFEN: /**       */ { [gs]: '     ', [sb]: 'W    ' },
  AKZEPTIERT: /**         */ { [gs]: '     ', [sb]: '     ' },
  ABGELEHNT: /**          */ { [gs]: 'W    ', [sb]: '     ' },
  MANUELLE_AENDERUNG: /** */ { [gs]: '     ', [sb]: '     ' },
} as const satisfies Record<
  GesuchTrancheStatus,
  Record<AppType, PermissionFlags>
>;

export const preparePermissions = (
  trancheTyp: GesuchUrlType | null,
  gesuch: SharedModelGesuch | null,
  appType: AppType | undefined,
  rolesMap: RolesMap,
) => {
  if (!gesuch || !appType) return { readonly: false, permissions: {} };
  const gesuchPermissions = getGesuchPermissions(gesuch, appType, rolesMap);
  const tranchePermissions = getTranchePermissions(gesuch, appType, rolesMap);
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
  gesuch: {
    gesuchStatus: Gesuchstatus;
    delegierung?: Delegierung;
  } | null,
  appType: AppType | undefined,
  rolesMap: RolesMap,
): PermissionMap => {
  if (!gesuch || !appType) return {};

  const state = permissionTableByAppType[gesuch.gesuchStatus][appType];
  return applyDelegatedPermission(
    parsePermissions(state),
    gesuch,
    appType,
    rolesMap,
  );
};

/**
 * Get the permissions for the tranche based on the status and the app type
 */
export const getTranchePermissions = (
  gesuch: {
    gesuchTrancheToWorkWith: { status: GesuchTrancheStatus };
    delegierung?: Delegierung;
  } | null,
  appType: AppType | undefined,
  rolesMap: RolesMap,
): PermissionMap => {
  if (!gesuch || !appType) return {};

  const state =
    trancheReadWritestatusByAppType[gesuch.gesuchTrancheToWorkWith.status][
      appType
    ];
  return applyDelegatedPermission(
    parsePermissions(state),
    gesuch,
    appType,
    rolesMap,
  );
};

/**
 * Special gesuch update check for the gesuch-app
 *
 * Currently it applies a check if the current user is allowed to update the gesuch
 * depending on if it is delegated and the user roles of the current user.
 */
export const canCurrentlyEdit = (
  permissions: PermissionMap,
  appType: AppType,
  rolesMap: RolesMap,
  delegierung: Delegierung | undefined,
) => {
  // Only apply special rules for the gesuch-app
  if (appType !== 'gesuch-app') {
    return !!permissions.canWrite;
  }
  // If the gesuch is not writable anyway, return false
  if (!permissions.canWrite) {
    return false;
  }

  return (
    // OK if it is not delegated and current user is not a sozialdienst-mitarbeiter
    (!delegierung && rolesMap['Sozialdienst-Mitarbeiter'] !== true) ||
    // OK if it is delegated and current user is a sozialdienst-mitarbeiter
    (!!delegierung && rolesMap['Sozialdienst-Mitarbeiter'] === true)
  );
};

/**
 * Revoke writability and document upload permissions if the gesuch is delegated
 */
const applyDelegatedPermission = (
  permissions: PermissionMap,
  gesuch: { delegierung?: Delegierung },
  appType: AppType,
  rolesMap: RolesMap,
): PermissionMap => {
  if (canCurrentlyEdit(permissions, appType, rolesMap, gesuch.delegierung)) {
    return permissions;
  }

  return {
    ...permissions,
    canWrite: false,
    canUploadDocuments: false,
    canFreigeben: false,
  };
};
