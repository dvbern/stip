import { RolesMap } from '@dv/shared/model/benutzer';
import {
  AppType,
  BusinessAppType,
  ensureIsBusinessAppType,
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

const GS_APP = 'gesuch-app' satisfies BusinessAppType;
const SB_APP = 'sachbearbeitung-app' satisfies BusinessAppType;

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
  IN_BEARBEITUNG_GS               : { [GS_APP]: perm('W  DE  ', ['gs']), [SB_APP]: perm(' R     ', ['sb']) },
  EINGEGEBEN                      : { [GS_APP]: perm('       ', ['gs']), [SB_APP]: perm(' RK  F ', ['sb']) },
  IN_FREIGABE                     : { [GS_APP]: perm('       ', ['gs']), [SB_APP]: perm(' RK   A', ['fe']) },
  AKZEPTIERT                      : { [GS_APP]: perm(' R     ', ['gs']), [SB_APP]: perm(' R     ', ['sb']) },
  ABGELEHNT                       : { [GS_APP]: perm(' R     ', ['gs']), [SB_APP]: perm(' R     ', ['sb']) },
} as const satisfies Record<
  DarlehenStatus,
  Record<BusinessAppType, PermissionCheck>
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
  ensureIsBusinessAppType(appType);
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
