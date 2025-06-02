import { confirm } from '@inquirer/prompts';
import KeycloakAdminClient from '@keycloak/keycloak-admin-client';
import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';

import {
  CurrentRole,
  CurrentRoleOrPermission,
  isDefined,
  isRoleOrPermission,
} from '../types';

/**
 * Syncs missing roles in Keycloak and asks for confirmation before creating them.
 */
export const syncMissingRoles = async (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  missingRoles: CurrentRole[],
  allRolesOrPermissionsMap: Partial<
    Record<CurrentRoleOrPermission, RoleRepresentation>
  >,
) => {
  console.info('Syncing missing roles', missingRoles);
  console.info('======================================');
  if (
    !(await confirm({
      message: 'Are you sure you want to create the roles?',
    }))
  ) {
    console.warn('Aborting role and permission sync...');
    process.exit(0);
  }

  const createMissingRoles = missingRoles.map(async (role) => {
    console.info(`Creating role ${role}`);
    await kcAdminClient.roles.create({
      name: role,
      realm,
    });
    const createdRole = await kcAdminClient.roles.findOneByName({
      name: role,
      realm,
    });
    if (!createdRole) {
      console.error(`Role ${role} not found after creation`);
      return;
    }
    return createdRole;
  });

  if (createMissingRoles.length) {
    (await Promise.all(createMissingRoles))
      .filter(isDefined)
      .filter(isRoleOrPermission)
      .forEach((newRole) => {
        allRolesOrPermissionsMap[newRole.name] = newRole;
      });

    console.info(`Created roles ${missingRoles.join(', ')}`);
  }
  console.info('======================================\n');
};
