import { confirm } from '@inquirer/prompts';
import KeycloakAdminClient from '@keycloak/keycloak-admin-client';
import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';

import {
  CurrentRoleOrPermission,
  isDefined,
  isRoleOrPermission,
} from '../types';

/**
 * Syncs missing permissions roles in Keycloak and asks for confirmation before creating them.
 */
export const syncMissingPermissions = async (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  missingPermissions: CurrentRoleOrPermission[],
  allRolesOrPermissionsMap: Partial<
    Record<CurrentRoleOrPermission, RoleRepresentation>
  >,
) => {
  console.info('Syncing missing permissions', missingPermissions);
  console.info('======================================');
  if (
    !(await confirm({
      message: 'Are you sure you want to create the permissions?',
    }))
  ) {
    console.warn('Aborting role and permission sync...');
    process.exit(0);
  }
  const createMissingPermissions = missingPermissions.map(
    async (permission) => {
      await kcAdminClient.roles.create({
        name: permission,
        realm,
      });

      const createdPermission = await kcAdminClient.roles.findOneByName({
        name: permission,
        realm,
      });

      if (!createdPermission) {
        console.error(`Permission ${permission} not found after creation`);
        return;
      }

      return createdPermission;
    },
  );

  if (createMissingPermissions.length) {
    (await Promise.all(createMissingPermissions))
      .filter(isDefined)
      .filter(isRoleOrPermission)
      .forEach((newRole) => {
        allRolesOrPermissionsMap[newRole.name] = newRole;
      });

    console.info('Created permissions:', missingPermissions);
  }
  console.info('======================================\n');
};

export const deleteSuperfluousPermissions = async (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  superfluousPermissions: string[],
) => {
  console.info('Removing superfluous permissions', superfluousPermissions);
  console.info('======================================');
  if (
    !(await confirm({
      message: 'Are you sure you want to remove the permissions?',
    }))
  ) {
    console.warn('Aborting role and permission removal...');
    return false;
  }
  const deleteMissingPermissions = superfluousPermissions.map(
    async (permission) => {
      await kcAdminClient.roles.delByName({
        name: permission,
        realm,
      });

      const deletedPermission = await kcAdminClient.roles.findOneByName({
        name: permission,
        realm,
      });

      if (deletedPermission) {
        console.error(`Permission ${permission} still found after creation`);
        return { name: permission, deleted: false };
      }

      return { name: permission, deleted: true };
    },
  );

  if (deleteMissingPermissions.length) {
    (await Promise.all(deleteMissingPermissions))
      .filter(isDefined)
      .forEach((deletedRole) => deletedRole.name);

    console.info('Deleted permissions:', superfluousPermissions);
  }
  console.info('======================================\n');
  return true;
};
