import { confirm } from '@inquirer/prompts';
import KeycloakAdminClient from '@keycloak/keycloak-admin-client';
import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';

import {
  CurrentRole,
  CurrentRoleOrPermission,
  NamedRoleRepresentation,
  PERMISSIONS_BY_ROLES,
  isDefined,
  isRoleOrPermission,
} from '../types';

/**
 * Repairs composite roles in Keycloak and asks for confirmation before each change.
 */
export const repairCompositeRoles = async (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  compositeRoles: {
    role: NamedRoleRepresentation;
    name: CurrentRole;
    children: NamedRoleRepresentation[];
  }[],
  allRolesOrPermissionsMap: Partial<
    Record<CurrentRoleOrPermission, RoleRepresentation>
  >,
) => {
  console.info(
    'Checking for roles to repair',
    compositeRoles.map((r) => r.name),
  );
  console.info('======================================');
  const repairIncompleteRoles = compositeRoles.map(
    repairRole(kcAdminClient, realm, allRolesOrPermissionsMap),
  );

  const repairedCompositeRoles: (CurrentRoleOrPermission | null)[] = [];
  for (const repair of repairIncompleteRoles) {
    const repaired = await repair();
    if (repaired) {
      repairedCompositeRoles.push(repaired);
    }
  }

  if (repairedCompositeRoles.filter(Boolean).length > 0) {
    console.info(`Repaired composite roles:`, repairedCompositeRoles);
  } else {
    console.info(`No composite roles required repair`);
  }
  console.info('======================================\n');

  return repairedCompositeRoles;
};

/**
 * Internal function that keeps track of the already asked state to for better line-breaking
 */
const repairRole = (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  allRolesOrPermissionsMap: Partial<
    Record<CurrentRoleOrPermission, RoleRepresentation>
  >,
) => {
  let alreadyAsked = false;
  return (params: {
      role: NamedRoleRepresentation;
      name: CurrentRole;
      children: NamedRoleRepresentation[];
    }) =>
    async () => {
      const { role, name, children } = params;
      const permissions = PERMISSIONS_BY_ROLES[name]
        ?.map((permission) => allRolesOrPermissionsMap[permission])
        .filter(isDefined);

      if (!permissions) {
        return;
      }

      if (!role.id) {
        console.error(`Role ${role.name} has no id`);
        return;
      }

      const expected = permissions
        .filter(isRoleOrPermission)
        .map((p) => p.name)
        .filter(isDefined);
      const actual = children.map((p) => p.name).filter(isDefined);
      const missingPermissions = expected
        .filter((x) => !actual.includes(x))
        .map((permission) => allRolesOrPermissionsMap[permission])
        .filter(isDefined);

      if (!missingPermissions.length) {
        return;
      }

      console.info(
        `${alreadyAsked ? '\n' : ''}Repairing composite role ${role.name} with permissions:`,
        missingPermissions?.map((r) => r.name).filter(isDefined),
      );
      alreadyAsked = true;
      if (
        !(await confirm({
          message: 'Are you sure you want to make these changes?',
        }))
      ) {
        console.info('Cancelling role update...\n');
        return null;
      }

      await kcAdminClient.roles.createComposite(
        {
          roleId: role.id,
          realm,
        },
        missingPermissions,
      );

      return role.name;
    };
};
