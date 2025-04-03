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
  D: { index: 1, name: 'uploadDocuments' },
  F: { index: 2, name: 'freigeben' },
  U: { index: 3, name: 'uploadUnterschriftenblatt' },
  A: { index: 4, name: 'approve' },
} as const;
type Permissions = typeof Permissions;
type PermissionFlag = keyof typeof Permissions;
type P<T extends PermissionFlag> = T | ' ';

/**
 * Permissions for the gesuch and tranche interactions
 *
 * * `W` - Write
 * * `D` - Dokumente hochladen
 * * `F` - Freigeben
 * * `U` - Unterschriftenblatt hochladen
 */
type PermissionFlags = `${P<'W'>}${P<'D'>}${P<'F'>}${P<'U'>}${P<'A'>}`;

export type Permission = Permissions[PermissionFlag]['name'];
export type PermissionMap = Partial<ReturnType<typeof parsePermissions>>;

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
  IN_BEARBEITUNG_GS /**                */: { [gs]: 'WDF  ', [sb]: '   U ' },
  EINGEREICHT /**                      */: { [gs]: '     ', [sb]: '   U ' },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: '     ', [sb]: '   U ' },
  IN_BEARBEITUNG_SB /**                */: { [gs]: '     ', [sb]: 'W  UA' },
  IN_FREIGABE /**                      */: { [gs]: '     ', [sb]: '   U ' },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: '     ', [sb]: 'W  U ' },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: '     ', [sb]: '   U ' },
  FEHLENDE_DOKUMENTE /**               */: { [gs]: ' DF  ', [sb]: '   U ' },
  GESUCH_ABGELEHNT /**                 */: { [gs]: '     ', [sb]: '   U ' },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: '     ', [sb]: '   U ' },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: '     ', [sb]: '     ' },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: '     ', [sb]: '   U ' },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: '     ', [sb]: '   U ' },
  STIPENDIENANSPRUCH /**               */: { [gs]: '     ', [sb]: '     ' },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: '     ', [sb]: '   U ' },
  VERSANDBEREIT /**                    */: { [gs]: '     ', [sb]: '     ' },
  VERFUEGT /**                         */: { [gs]: '     ', [sb]: '   U ' },
  VERSENDET /**                        */: { [gs]: '     ', [sb]: '     ' },
  NEGATIVE_VERFUEGUNG /**              */: { [gs]: '     ', [sb]: '   U ' },
} as const satisfies Record<Gesuchstatus, Record<AppType, PermissionFlags>>;

/**
 * Similar as `permissionTableByAppType` but for the tranches
 *
 * @see {@link permissionTableByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: 'WDF  ', [sb]: '     ' },
  UEBERPRUEFEN: /**       */ { [gs]: '     ', [sb]: 'W   A' },
  FEHLENDE_DOKUMENTE: /** */ { [gs]: ' D   ', [sb]: '     ' },
  AKZEPTIERT: /**         */ { [gs]: '     ', [sb]: '     ' },
  ABGELEHNT: /**          */ { [gs]: 'WD   ', [sb]: '     ' },
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
  if (!gesuch || !appType)
    return { readonly: false, permissions: {} as PermissionMap };
  const getPermissions = {
    TRANCHE: () => getGesuchPermissions(gesuch, appType, rolesMap),
    AENDERUNG: () => getTranchePermissions(gesuch, appType, rolesMap),
    INITIAL: () => getGesuchPermissions(gesuch, appType, rolesMap),
  } satisfies Record<GesuchUrlType, unknown>;
  const { permissions, status } = getPermissions[trancheTyp ?? 'TRANCHE']();
  const canWrite = permissions.canWrite ?? true;

  return {
    readonly: trancheTyp === 'INITIAL' || !canWrite,
    permissions,
    isFehlendeDokumente: status === 'FEHLENDE_DOKUMENTE',
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
): { permissions: PermissionMap; status?: Gesuchstatus } => {
  if (!gesuch || !appType) return { permissions: {} };

  const state = permissionTableByAppType[gesuch.gesuchStatus][appType];
  const permissions = parsePermissions(state);
  return {
    permissions: applyDelegatedPermission(
      permissions,
      gesuch,
      appType,
      rolesMap,
    ),
    status: gesuch.gesuchStatus,
  };
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
): { permissions: PermissionMap; status?: GesuchTrancheStatus } => {
  if (!gesuch || !appType) return { permissions: {} };

  const state =
    trancheReadWritestatusByAppType[gesuch.gesuchTrancheToWorkWith.status][
      appType
    ];
  const permissions = parsePermissions(state);
  return {
    permissions: applyDelegatedPermission(
      permissions,
      gesuch,
      appType,
      rolesMap,
    ),
    status: gesuch.gesuchTrancheToWorkWith.status,
  };
};

/**
 * Special gesuch update check for the gesuch-app
 *
 * Currently it applies a check if the current user is allowed to update the gesuch
 * depending on if it is delegated and the user roles of the current user.
 */
export const canCurrentlyEdit = (
  appType: AppType,
  rolesMap: RolesMap,
  delegierung: Delegierung | undefined,
) => {
  // Only apply special rules for the gesuch-app
  if (appType !== 'gesuch-app') {
    return true;
  }

  return (
    !delegierung ||
    // OK if it is delegated and current user is a sozialdienst-mitarbeiter
    (!!delegierung && rolesMap['V0_Sozialdienst-Mitarbeiter'] === true)
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
  if (canCurrentlyEdit(appType, rolesMap, gesuch.delegierung)) {
    return permissions;
  }

  return {
    ...permissions,
    canWrite: false,
    canUploadDocuments: false,
    canFreigeben: false,
  };
};

/**
 * Used to define values that are accessed by the app type
 *
 * @example
 * ```ts
 * byAppType(this.config.appType, {
 *   'gesuch-app': () => this.trancheService.getGesuchDokumenteGS$(...),
 *   'sachbearbeitung-app': () => this.trancheService.getGesuchDokumenteSB$(...),
 * })()
 * ```
 */
export const byAppType = <R>(
  appType: AppType,
  map: Record<AppType, () => R>,
) => {
  return map[appType]();
};
