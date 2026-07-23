import { RolesMap } from '@dv/shared/model/benutzer';
import {
  AppConfig,
  AppView,
  ensureIsBusinessAppConfig,
} from '@dv/shared/model/config';
import { DarlehenStatus, DelegierungSlim } from '@dv/shared/model/gesuch';
import { capitalized } from '@dv/shared/model/type-util';

import {
  ShortRole,
  isNotReadonly,
  shortRoleMap,
} from './shared-model-permission-state';

const DarlehenPermissions = {
  W: { index: 0, name: 'writeGs' },
  R: { index: 1, name: 'readSbForm' },
  K: { index: 2, name: 'writeSb' },
  D: { index: 3, name: 'uploadDocuments' },
  E: { index: 4, name: 'eingeben' },
  F: { index: 5, name: 'freigeben' },
  A: { index: 6, name: 'approveDarlehen' },
} as const;
type DarlehenPermissions = typeof DarlehenPermissions;
type DarlehenPermissionFlag = keyof DarlehenPermissions;
type P<T extends DarlehenPermissionFlag> = T | ' ';

type DarlehenPermissionFlags =
  `${P<'W'>}${P<'R'>}${P<'K'>}${P<'D'>}${P<'E'>}${P<'F'>}${P<'A'>}`;

export type DarlehenPermission =
  DarlehenPermissions[DarlehenPermissionFlag]['name'];

const hasPermission = (
  p: DarlehenPermissionFlags,
  perm: keyof typeof DarlehenPermissions,
) => p.charAt(DarlehenPermissions[perm].index) === perm;

const GS_APP = 'gesuchsteller' satisfies AppView;
const SB_APP = 'sachbearbeiter' satisfies AppView;

type MultiplePermissionFlags = [DarlehenPermissionFlags, ShortRole][];

/**
 * Check the permission for multiple roles, merging flags that are matching the rolesMap.
 */
const mPerm = (flagsByRoles: MultiplePermissionFlags) => {
  return (rolesMap: RolesMap): DarlehenPermissionFlags => {
    let combinedPermissions: DarlehenPermissionFlags = '       ';

    for (const [flags, shortRole] of flagsByRoles) {
      // If the rolesMap contains the shortRole, merge the flags into combinedPermissions.
      if (rolesMap[shortRoleMap[shortRole]]) {
        combinedPermissions = combinedPermissions
          .split('')
          .map((char, index) => (char !== ' ' ? char : flags.charAt(index)))
          .join('') as DarlehenPermissionFlags;
      }
    }

    return combinedPermissions;
  };
};

const perm = (flags: DarlehenPermissionFlags, roles: ShortRole[]) => {
  return (rolesMap: RolesMap): DarlehenPermissionFlags =>
    roles.some((shortRole) => !!rolesMap[shortRoleMap[shortRole]])
      ? flags
      : '       ';
};

type PermissionCheck = ReturnType<typeof perm>;

const parsePermissions = (permission: DarlehenPermissionFlags) => {
  return (Object.keys(DarlehenPermissions) as DarlehenPermissionFlag[]).reduce(
    (acc, perm) => {
      acc[`can${capitalized(DarlehenPermissions[perm].name)}`] = hasPermission(
        permission,
        perm,
      );
      return acc;
    },
    {} as Record<`can${Capitalize<DarlehenPermission>}`, boolean>,
  );
};
export type DarlehenPermissionMap = ReturnType<typeof parsePermissions>;

// prettier-ignore
export const darlehenPermissionTableByAppConfig = {
  IN_BEARBEITUNG_GS               : { [GS_APP]: perm('W  DE  ', ['gs', 'soz']), [SB_APP]: mPerm([[' R     ', 'sb'],
                                                                                                 [' R     ', 'fe']]) },
  EINGEGEBEN                      : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: mPerm([[' RK  F ', 'sb'],
                                                                                                 [' R     ', 'fe']]) },
  IN_FREIGABE                     : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: mPerm([[' R     ', 'sb'],
                                                                                                 [' RK   A', 'fe']]) },
  AKZEPTIERT                      : { [GS_APP]: perm(' R     ', ['gs', 'soz']), [SB_APP]: mPerm([[' R     ', 'sb'],
                                                                                                 [' R     ', 'fe']]) },
  ABGELEHNT                       : { [GS_APP]: perm(' R     ', ['gs', 'soz']), [SB_APP]: mPerm([[' R     ', 'sb'],
                                                                                                 [' R     ', 'fe']]) },
} as const satisfies Record<
  DarlehenStatus,
  Record<AppView, PermissionCheck>
>;

const applyDelegatedDarlehenPermissions = (
  permissions: DarlehenPermissionMap,
  appConfig: AppConfig,
  rolesMap: RolesMap,
  delegierung?: DelegierungSlim | boolean | undefined,
): DarlehenPermissionMap => {
  if (isNotReadonly(appConfig, rolesMap, delegierung)) {
    return permissions;
  }

  return {
    ...permissions,
    canWriteGs: false,
    canUploadDocuments: false,
    canEingeben: false,
  };
};

export const getDarlehenPermissions = (
  status: DarlehenStatus | undefined,
  appConfig: AppConfig,
  rolesMap: RolesMap,
  delegierung?: DelegierungSlim | boolean | undefined,
) => {
  if (!status) {
    return { permissions: undefined, status };
  }
  ensureIsBusinessAppConfig(appConfig);
  const state =
    darlehenPermissionTableByAppConfig[status][appConfig.view](rolesMap);
  const permissions = parsePermissions(state);

  return {
    permissions: applyDelegatedDarlehenPermissions(
      permissions,
      appConfig,
      rolesMap,
      delegierung,
    ),
    status,
  };
};
