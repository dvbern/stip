import { RolesMap } from '@dv/shared/model/benutzer';
import { AppType } from '@dv/shared/model/config';
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

const GS_APP = 'gesuch-app' satisfies AppType;
const SB_APP = 'sachbearbeitung-app' satisfies AppType;

type PermArg = Partial<Record<ShortRole, DarlehenPermissionFlags>>;

/**
 * A permission assignement for cases where roles have different permissions
 * Permissions are combined, since picking the first matching role would lead to
 * unexpected results if a user has multiple roles.
 */
const multiPerm = (arg: PermArg) => {
  return (rolesMap: RolesMap): DarlehenPermissionFlags => {
    let combinedPermissions: DarlehenPermissionFlags = '       ';

    for (const shortRole in arg) {
      if (rolesMap[shortRoleMap[shortRole as ShortRole]]) {
        const rolePermissions = arg[shortRole as ShortRole];
        if (rolePermissions) {
          combinedPermissions = combinedPermissions
            .split('')
            .map((char, index) =>
              char !== ' ' ? char : rolePermissions.charAt(index),
            )
            .join('') as DarlehenPermissionFlags;
        }
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
export const darlehenPermissionTableByAppType = {
  IN_BEARBEITUNG_GS               : { [GS_APP]: perm('W  DE  ', ['gs', 'soz']), [SB_APP]: multiPerm({sb: ' R     ', fe: ' R     '}) },
  EINGEGEBEN                      : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: multiPerm({sb: ' RK  F ', fe: ' R     '}) },
  IN_FREIGABE                     : { [GS_APP]: perm('       ', ['gs', 'soz']), [SB_APP]: multiPerm({sb: ' R     ', fe: ' RK   A'}) },
  AKZEPTIERT                      : { [GS_APP]: perm(' R     ', ['gs', 'soz']), [SB_APP]: multiPerm({sb: ' R     ', fe: ' R     '}) },
  ABGELEHNT                       : { [GS_APP]: perm(' R     ', ['gs', 'soz']), [SB_APP]: multiPerm({sb: ' R     ', fe: ' R     '}) },
} as const satisfies Record<
  DarlehenStatus,
  Record<AppType, PermissionCheck>
>;

const applyDelegatedDarlehenPermissions = (
  permissions: DarlehenPermissionMap,
  appType: AppType,
  rolesMap: RolesMap,
  delegierung?: DelegierungSlim,
): DarlehenPermissionMap => {
  if (isNotReadonly(appType, rolesMap, delegierung)) {
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
  appType: AppType,
  rolesMap: RolesMap,
  delegierung?: DelegierungSlim,
) => {
  if (!status) {
    return { permissions: undefined, status };
  }
  const state = darlehenPermissionTableByAppType[status][appType](rolesMap);
  const permissions = parsePermissions(state);

  return {
    permissions: applyDelegatedDarlehenPermissions(
      permissions,
      appType,
      rolesMap,
      delegierung,
    ),
    status,
  };
};
