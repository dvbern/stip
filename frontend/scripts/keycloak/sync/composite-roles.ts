import { confirm } from '@inquirer/prompts';
import KeycloakAdminClient from '@keycloak/keycloak-admin-client';
import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';

import {
  CurrentRole,
  CurrentRoleOrPermission,
  NamedRoleRepresentation,
  PERMISSIONS_BY_ROLES,
  isDefined,
} from '../types';

/**
 * Syncs missing composite roles in Keycloak and asks for confirmation before each change.
 */
export const syncMissingCompositeRoles = async (
  kcAdminClient: KeycloakAdminClient,
  realm: string,
  missingCompositeRoles: { role: NamedRoleRepresentation; name: CurrentRole }[],
  allRolesOrPermissionsMap: Partial<
    Record<CurrentRoleOrPermission, RoleRepresentation>
  >,
) => {
  console.info('Syncing missing composite roles', missingCompositeRoles);
  console.info('======================================');
  const createMissingCompositeRoles = missingCompositeRoles.map(
    ({ role, name }) =>
      async () => {
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

        console.info(
          `Creating composite role ${role.name} with permissions:`,
          PERMISSIONS_BY_ROLES[name]?.join(', '),
        );
        if (
          !(await confirm({
            message: 'Are you sure you want to make these changes?',
          }))
        ) {
          console.info('Cancelling role update...');
          return false;
        }

        await kcAdminClient.roles.createComposite(
          {
            roleId: role.id,
            realm,
          },
          permissions,
        );
        return true;
      },
  );

  const updated: boolean[] = [];
  for (const create of createMissingCompositeRoles) {
    const result = await create();
    if (result) {
      updated.push(result);
    }
  }
  if (updated.some(Boolean)) {
    console.info(
      `Created composite roles ${missingCompositeRoles.map((r) => r.name)}`,
    );
  }
  console.info('======================================\n');
};
