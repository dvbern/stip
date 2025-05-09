import 'dotenv/config';

import { join } from 'node:path';

import KcAdminClient from '@keycloak/keycloak-admin-client';
import RoleRepresentation from '@keycloak/keycloak-admin-client/lib/defs/roleRepresentation';
// eslint-disable-next-line import/order
import { Command } from 'commander';

// dotenv for user and password
import { config } from 'dotenv';

import { allowInsecure } from './ignore-reject-unauthorized';
import { syncMissingCompositeRoles } from './sync/composite-roles';
import { syncMissingPermissions } from './sync/permissions';
import { repairCompositeRoles } from './sync/repair-composite-roles';
import { syncMissingRoles } from './sync/roles';
import {
  CurrentRoleOrPermission,
  PERMISSIONS,
  ROLES,
  isDefined,
  isRoleOrPermission,
} from './types';
config({ path: join(__dirname, '../../.env') });

const USERNAME_ENV = 'KEYCLOAK_ADMIN_USERNAME';
const PASSWORD_ENV = 'KEYCLOAK_ADMIN_PASSWORD';

allowInsecure();
const env = process.env;

const known = {
  realms: ['bern', 'dv'],
} as const;

const program = new Command();
program.name('update-keycloak');

program.exitOverride((err) => {
  if (err.code === 'commander.missingMandatoryOptionValue') {
    console.log('');
    program.outputHelp();
    console.log('');
  }
  process.exit(err.exitCode);
});

const syncRolesCommand = program
  .command('sync-roles')
  .requiredOption('-u, --url <url>', 'Keycloak URL')
  .addHelpText(
    'before',
    `Syncs roles and permissions in Keycloak using roles-map.ts as a reference.
This script will create missing roles and permissions, and repair composite roles if needed.
`,
  )
  .addHelpText(
    'after',
    `

Example call:
  $ npm run sync-roles -- --url https://dev-auth-stip.apps.mercury.ocp.dvbern.ch
`,
  );

/**
 * Initialize the Keycloak admin client.
 */
async function initKc(url: string) {
  const kcAdminClient = new KcAdminClient({
    baseUrl: url,
  });
  const KEYCLOAK_ADMIN_USERNAME = env[`${USERNAME_ENV}_${'DEV'}`];
  const KEYCLOAK_ADMIN_PASSWORD = env[`${PASSWORD_ENV}_${'DEV'}`];
  await kcAdminClient.auth({
    username: KEYCLOAK_ADMIN_USERNAME,
    password: KEYCLOAK_ADMIN_PASSWORD,
    grantType: 'password',
    clientId: 'admin-cli',
  });

  return kcAdminClient;
}

/**
 * Find missing roles and permissions in Keycloak.
 */
async function findMissingRoles(kcAdminClient: KcAdminClient, realm: string) {
  const allRolesOrPermissions = (
    await kcAdminClient.roles.find({ realm })
  ).filter(isRoleOrPermission);
  const roles = await Promise.all(
    ROLES.map(async (role) => {
      const roleRepresentation = allRolesOrPermissions.find(
        (r) => r.name === role,
      );
      if (!roleRepresentation || !roleRepresentation.id) {
        console.warn(`Role ${role} not found`);
        return {
          type: 'NOT_FOUND',
          name: role,
        } as const;
      }

      if (!roleRepresentation.composite) {
        console.warn(`Role ${role} is not composite`);
        return {
          type: 'NOT_COMPOSITE',
          name: role,
          role: roleRepresentation,
        } as const;
      }

      return {
        type: 'COMPOSITE',
        name: role,
        role: roleRepresentation,
        children: (
          await kcAdminClient.roles.getCompositeRolesForRealm({
            id: roleRepresentation.id,
            realm,
          })
        ).filter(isRoleOrPermission),
      } as const;
    }),
  );

  const missingPermissions = PERMISSIONS.filter(
    (role) => !allRolesOrPermissions.some((r) => r.name === role),
  );

  return {
    allRolesOrPermissionsMap: allRolesOrPermissions.reduce(
      (acc, r) => ({
        ...acc,
        [r.name]: r,
      }),
      {} as Partial<Record<CurrentRoleOrPermission, RoleRepresentation>>,
    ),
    missingCompositeRoles: roles.filter((r) => r.type === 'NOT_COMPOSITE'),
    compositeRoles: roles.filter((r) => r.type === 'COMPOSITE'),
    missingRoles: roles
      .filter((r) => r.type === 'NOT_FOUND')
      .map((r) => r.name),
    missingPermissions,
  };
}

/**
 * Syncs roles and permissions in Keycloak using roles-map.ts as a reference.
 *
 * @param realm the realm to sync roles and permissions for
 */
async function syncRoles(realm: string) {
  const { url } = syncRolesCommand.opts();
  const kcAdminClient = await initKc(url);
  const {
    allRolesOrPermissionsMap,
    compositeRoles,
    missingCompositeRoles,
    missingPermissions,
    missingRoles,
  } = {
    ...(await findMissingRoles(kcAdminClient, realm)),
  };

  console.info('Current state of roles and permissions');
  console.info('======================================');
  console.info({
    missingPermissions,
    missingRoles,
    missingCompositeRoles: missingCompositeRoles.map((r) => r.name),
  });
  console.info('======================================\n');

  // Add missing permissions
  if (missingPermissions.length > 0) {
    await syncMissingPermissions(
      kcAdminClient,
      realm,
      missingPermissions,
      allRolesOrPermissionsMap,
    );
  }

  // Add missing roles
  if (missingRoles.length > 0) {
    await syncMissingRoles(
      kcAdminClient,
      realm,
      missingRoles,
      allRolesOrPermissionsMap,
    );
  }

  // Add missing composite roles
  if (missingCompositeRoles.length > 0) {
    await syncMissingCompositeRoles(
      kcAdminClient,
      realm,
      missingCompositeRoles,
      allRolesOrPermissionsMap,
    );
  }

  // Repair composite roles
  const repairedCompositeRoles = await repairCompositeRoles(
    kcAdminClient,
    realm,
    compositeRoles,
    allRolesOrPermissionsMap,
  );

  return {
    addedMissingCompositeRoles: missingCompositeRoles,
    addedMissingRoles: missingRoles,
    addedMissingPermissions: missingPermissions,
    repairedCompositeRoles,
  };
}

/**
 * Run an async function sequentially for each realm
 *
 * @param fn the async function to run
 */
const runForRealms = async <R>(fn: (realm: string) => Promise<R>) => {
  const results: { realm: string; result: R }[] = [];

  for (const realm of known.realms) {
    console.info('##################################');
    console.info('# Running for realm:             #');
    console.info(`# ${realm.padStart(30)} #`);
    console.info('##################################\n');
    const result = await fn(realm);
    results.push({ realm, result });
  }
  return results;
};

syncRolesCommand.action(async () => {
  try {
    const results = (await runForRealms((realm) => syncRoles(realm))).filter(
      isDefined,
    );
    if (results.length > 0) {
      console.info('Results:', results);
    }

    console.info('All roles and permissions are up to date');
  } catch (error) {
    if (error instanceof Error && error.name === 'ExitPromptError') {
      console.warn('User cancelled the operation');
    } else {
      console.error('Error adding role:', error);
    }
  }
});
program.parse(process.argv);
