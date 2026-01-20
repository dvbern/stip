import { AvailableBenutzerRole, RolesMap } from '@dv/shared/model/benutzer';
import { AppType } from '@dv/shared/model/config';
import {
  DelegierungSlim,
  GesuchTrancheStatus,
  GesuchUrlType,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { assertUnreachable, capitalized } from '@dv/shared/model/type-util';

const Permissions = {
  W: { index: 0, name: 'write' },
  D: { index: 1, name: 'uploadDocuments' },
  F: { index: 2, name: 'freigeben' },
  U: { index: 3, name: 'uploadUnterschriftenblatt' },
  A: { index: 4, name: 'approve' },
  N: { index: 5, name: 'negativVerfuegen' },
  J: { index: 6, name: 'createJurNotiz' },
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
type PermissionFlags =
  `${P<'W'>}${P<'D'>}${P<'F'>}${P<'U'>}${P<'A'>}${P<'N'>}${P<'J'>}`;

export type Permission = Permissions[PermissionFlag]['name'];
export type PermissionMap = Partial<ReturnType<typeof parsePermissions>>;
export type ShortRole = 'gs' | 'sb' | 'ju' | 'fe' | 'soz';

export const shortRoleMap = {
  gs: 'V0_Gesuchsteller',
  sb: 'V0_Sachbearbeiter',
  ju: 'V0_Jurist',
  fe: 'V0_Freigabestelle',
  soz: 'V0_Sozialdienst-Mitarbeiter',
} satisfies Partial<Record<ShortRole, AvailableBenutzerRole>>;

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

const GS_APP = 'gesuch-app' satisfies AppType;
const SB_APP = 'sachbearbeitung-app' satisfies AppType;

const perm = (flags: PermissionFlags, roles: ShortRole[]) => {
  return (rolesMap: RolesMap): PermissionFlags =>
    roles.some((shortRole) => !!rolesMap[shortRoleMap[shortRole]])
      ? flags
      : '       ';
};
type PermissionCheck = ReturnType<typeof perm>;

/**
 * Define the permissions for the gesuch based on the status.
 *
 * * Format is: { [Gesuchstatus]: { [AppType]: 'WV  ' | 'W   ' | ..., ... other AppTypes } }
 */
// prettier-ignore
export const permissionTableByAppType = {
  IN_BEARBEITUNG_GS                : { [GS_APP]: perm('WDF    ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  EINGEREICHT                      : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  ANSPRUCH_PRUEFEN                 : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  BEREIT_FUER_BEARBEITUNG          : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  IN_BEARBEITUNG_SB                : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('WD UANJ', ['sb']) },
  IN_FREIGABE                      : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U NJ', ['ju']) },
  ANSPRUCH_MANUELL_PRUEFEN         : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U NJ', ['sb']) },
  FEHLENDE_DOKUMENTE               : { [GS_APP]: perm(' DF    ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  GESUCH_ABGELEHNT                 : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  JURISTISCHE_ABKLAERUNG           : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  KEIN_STIPENDIENANSPRUCH          : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  NICHT_ANSPRUCHSBERECHTIGT        : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U NJ', ['sb']) },
  NICHT_BEITRAGSBERECHTIGT         : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  STIPENDIENANSPRUCH               : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  WARTEN_AUF_UNTERSCHRIFTENBLATT   : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  DATENSCHUTZBRIEF_AM_GENERIEREN   : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  DATENSCHUTZBRIEF_DRUCKBEREIT     : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  DATENSCHUTZBRIEF_VERSANDBEREIT   : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  VERFUEGUNG_AM_GENERIEREN         : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  VERFUEGUNG_DRUCKBEREIT           : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  VERFUEGUNG_VERSANDBEREIT         : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  VERFUEGUNG_VERSENDET             : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  VERFUEGT                         : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
  NEGATIVE_VERFUEGUNG              : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('   U   ', ['sb']) },
} as const satisfies Record<Gesuchstatus, Record<AppType, PermissionCheck>>;

/**
 * Similar as `permissionTableByAppType` but for the tranches
 *
 * @see {@link permissionTableByAppType}
 */
// prettier-ignore
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS:  { [GS_APP]: perm('WDF    ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  UEBERPRUEFEN:       { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('WD  A  ', ['sb']) },
  FEHLENDE_DOKUMENTE: { [GS_APP]: perm(' D     ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  AKZEPTIERT:         { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  ABGELEHNT:          { [GS_APP]: perm('WD     ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
  MANUELLE_AENDERUNG: { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: perm('       ', ['sb']) },
} as const satisfies Record<
  GesuchTrancheStatus,
  Record<AppType, PermissionCheck>
>;

/**
 * Returns permissions by gesuch type (TRANCHE, AENDERUNG, INITIAL), status and app type
 */
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
    delegierung?: DelegierungSlim;
  } | null,
  appType: AppType | undefined,
  rolesMap: RolesMap,
): { permissions: PermissionMap; status?: Gesuchstatus } => {
  if (!gesuch || !appType) return { permissions: {} };

  const state =
    permissionTableByAppType[gesuch.gesuchStatus][appType](rolesMap);
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
    delegierung?: DelegierungSlim;
  } | null,
  appType: AppType | undefined,
  rolesMap: RolesMap,
): { permissions: PermissionMap; status?: GesuchTrancheStatus } => {
  if (!gesuch || !appType) return { permissions: {} };

  const state =
    trancheReadWritestatusByAppType[gesuch.gesuchTrancheToWorkWith.status][
      appType
    ](rolesMap);
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
export const isNotReadonly = (
  appType: AppType,
  rolesMap: RolesMap,
  delegierung: DelegierungSlim | boolean | undefined,
) => {
  switch (appType) {
    case 'sachbearbeitung-app':
      return (
        ['V0_Sachbearbeiter', 'V0_Jurist'] satisfies AvailableBenutzerRole[]
      ).some((role) => rolesMap[role] === true);
    case 'gesuch-app': {
      const isDelegiert =
        typeof delegierung === 'boolean'
          ? delegierung
          : (delegierung?.delegierungAngenommen ?? false);
      if (!isDelegiert) {
        return true;
      }
      return rolesMap['V0_Sozialdienst-Mitarbeiter'] === true;
    }
    default:
      assertUnreachable(appType);
  }
};

/**
 * Revoke writability and document upload permissions if the gesuch is delegated
 */
const applyDelegatedPermission = (
  permissions: PermissionMap,
  gesuch: { delegierung?: DelegierungSlim },
  appType: AppType,
  rolesMap: RolesMap,
): PermissionMap => {
  if (isNotReadonly(appType, rolesMap, gesuch.delegierung)) {
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
