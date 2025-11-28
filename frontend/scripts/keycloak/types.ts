import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';

import { DEMO_DATA_PERMISSIONS, PERMISSION_ROLE_LIST } from './roles-map';

export const known = {
  envs: ['DEV', 'UAT'],
  realms: ['bern', 'dv'],
} as const;
export type KnownEnv = (typeof known)['envs'][number];
export type KnownRealm = (typeof known)['realms'][number];

export const CURRENT_VERSION = 'V0' as const;
export type CurrentVersion = typeof CURRENT_VERSION;

export type Role =
  | 'Gesuchsteller'
  | 'Sozialdienst-Mitarbeiter'
  | 'Sozialdienst-Admin'
  | 'Sachbearbeiter'
  | 'Jurist'
  | 'Super-User'
  | 'Sachbearbeiter-Admin'
  | 'Freigabestelle';

type PermissionRoleList = typeof PERMISSION_ROLE_LIST;

type Remainder<T extends unknown[]> = T extends [unknown, ...infer R]
  ? R
  : never;
type Permission =
  | Remainder<PermissionRoleList[number]>[number]
  | (typeof DEMO_DATA_PERMISSIONS)[number];

export type CurrentRole = `${CurrentVersion}_${Role}`;
export type CurrentPermission = `${CurrentVersion}_${Permission}`;
export type CurrentRoleOrPermission = CurrentRole | CurrentPermission;
export type NamedRoleRepresentation = RoleRepresentation & {
  name: CurrentRoleOrPermission;
};
export type DefinedNamedRoleRepresentation = NamedRoleRepresentation & {
  id: string;
  name: CurrentRoleOrPermission;
};

const unique = <T>(arr: T[]) => {
  return Array.from(new Set(arr));
};
const _v = <const T extends string>(roleOrPermission: T) => {
  return `${CURRENT_VERSION}_${roleOrPermission}` as const;
};

export const ROLES = unique(
  PERMISSION_ROLE_LIST.flatMap(([roles]) => roles),
).map(_v);
export const PERMISSIONS = unique(
  PERMISSION_ROLE_LIST.flatMap(([, ...permissions]) => permissions),
).map(_v);
export const ENV_SPECIAL_PERMISSIONS = {
  DEV: DEMO_DATA_PERMISSIONS.map(_v),
  UAT: DEMO_DATA_PERMISSIONS.map(_v),
} satisfies Record<KnownEnv, string[]>;

export const PERMISSIONS_BY_ROLES = PERMISSION_ROLE_LIST.reduce(
  (acc, [roles, ...permissions]) => {
    roles
      .map((r) => _v(r))
      .forEach((r) => {
        permissions
          .map((p) => _v(p))
          .forEach((p) => (acc[r] = (acc[r] ?? []).concat(p)));
      });
    return acc;
  },
  {} as Partial<Record<CurrentRole, CurrentPermission[]>>,
);

export const ROLES_AND_PERMISSIONS = [...ROLES, ...PERMISSIONS] as const;
export const isRoleOrPermission = <T extends { name?: string }>(
  obj: T,
): obj is T & { name: CurrentRoleOrPermission } => {
  return [...DEMO_DATA_PERMISSIONS.map(_v), ...ROLES_AND_PERMISSIONS].includes(
    obj.name as CurrentRoleOrPermission,
  );
};
export const isDefinedRoleOrPermission = (
  role: RoleRepresentation,
): role is DefinedNamedRoleRepresentation => {
  if (!role.id || !role.name) {
    return false;
  }
  return true;
};

export const isDefined = <T>(value: T | undefined | null): value is T => {
  return value !== undefined && value !== null;
};
