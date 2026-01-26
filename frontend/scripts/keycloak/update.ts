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
import {
  deleteSuperfluousPermissions,
  syncMissingPermissions,
} from './sync/permissions';
import { repairCompositeRoles } from './sync/repair-composite-roles';
import { syncMissingRoles } from './sync/roles';
import {
  CURRENT_VERSION,
  CurrentRole,
  CurrentRoleOrPermission,
  ENV_SPECIAL_PERMISSIONS,
  KnownEnv,
  KnownRealm,
  PERMISSIONS,
  ROLES,
  isDefined,
  isDefinedRoleOrPermission,
  isRoleOrPermission,
  known,
} from './types';
config({ path: join(__dirname, '../../.env') });

const URL_ENV = 'KEYCLOAK_ADMIN_URL';
const USERNAME_ENV = 'KEYCLOAK_ADMIN_USERNAME';
const PASSWORD_ENV = 'KEYCLOAK_ADMIN_PASSWORD';
const ADD_USER_PASSWORD_ENV = 'NEW_USER_PASSWORD';

allowInsecure();
const env = process.env;

const program = new Command();
program.name('update-keycloak');

program.exitOverride((err) => {
  if (
    [
      'commander.optionMissingArgument',
      'commander.missingMandatoryOptionValue',
    ].includes(err.code)
  ) {
    console.log('');
    program.outputHelp({ error: true });
    console.log('');
  }
  process.exit(err.exitCode);
});

const envOption = program
  .createOption('-e, --env <env>', 'which environment to use')
  .makeOptionMandatory(true)
  .choices(known.envs);
const syncRolesCommand = program
  .command('sync-roles')
  .addOption(envOption)
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
  $ npm run sync-roles -- -e DEV
`,
  );
const addUsersCommand = program
  .command('add-users')
  .addOption(envOption)
  .addOption(
    program
      .createOption(
        '-u, --user <user>',
        `the user to add and its first and last name, comma separated. example: "username,FirstName,LastName[,email]"
- If no email is provided, a default email will be generated based on the username (stip-{username}@mailbucket.dvbern.ch).`,
      )
      .makeOptionMandatory(true),
  )
  .addOption(
    program
      .createOption(
        '-r, --roles <roles...>',
        'roles to assign to the user, comma separated',
      )
      .choices(ROLES)
      .makeOptionMandatory(true),
  )
  .addOption(
    program
      .createOption(
        '--realm realm',
        `realm to add users to, default is "${known.realms[0]}"`,
      )
      .choices(known.realms)
      .default(known.realms[0]),
  )
  .addHelpText(
    'before',
    `Adds users to Keycloak with the specified roles.
This script will create users if they do not exist and assign the specified roles.
Or update the roles of existing users.

The password will be taken from the environment variable \`${ADD_USER_PASSWORD_ENV}\`.
`,
  )
  .addHelpText(
    'after',
    `

Example call:
  $ npm run add-users -- -e DEV -u "stip-scph-gs-2,Philipp,GS 2,philipp.schaerer+gs-2@dvbern.ch" -r V0_Gesuchsteller
  $ npm run add-users -- -e DEV -u "stip-scph-gs-1,Philipp,GS 1"         -r V0_Gesuchsteller
  $ npm run add-users -- -e DEV -u "stip-scph-sb-1,Philipp,SB 1"         -r V0_Sachbearbeiter
  $ npm run add-users -- -e DEV -u "stip-scph-frei-1,Philipp,Freigabe 1" -r V0_Freigabestelle
  $ npm run add-users -- -e DEV -u "stip-scph-admin-1,Philipp,Admin 1"   -r V0_Sachbearbeiter-Admin V0_Sachbearbeiter
`,
  );

/**
 * Initialize the Keycloak admin client.
 */
async function initKc(target: KnownEnv) {
  const KEYCLOAK_ADMIN_URL = env[`${URL_ENV}_${target}`];
  const KEYCLOAK_ADMIN_USERNAME = env[`${USERNAME_ENV}_${target}`];
  const KEYCLOAK_ADMIN_PASSWORD = env[`${PASSWORD_ENV}_${target}`];

  const missingEnvs = [
    [`${URL_ENV}_${target}`, KEYCLOAK_ADMIN_URL],
    [`${USERNAME_ENV}_${target}`, KEYCLOAK_ADMIN_USERNAME],
    [`${PASSWORD_ENV}_${target}`, KEYCLOAK_ADMIN_PASSWORD],
  ]
    .filter(([, env]) => !env)
    .map(([env]) => env);
  if (missingEnvs.length > 0) {
    console.error('Missing environment variables:', missingEnvs);
    console.error('Please check your .env file\n');
    process.exit(1);
  }

  const kcAdminClient = new KcAdminClient({
    baseUrl: KEYCLOAK_ADMIN_URL,
  });
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
async function findMissingRoles(
  kcAdminClient: KcAdminClient,
  realm: KnownRealm,
  env: KnownEnv,
) {
  const rawRolesOrPermissions = await kcAdminClient.roles.find({ realm });
  const allRolesOrPermissions =
    rawRolesOrPermissions.filter(isRoleOrPermission);
  const roles = await Promise.all(
    ROLES.map(async (role) => {
      const roleRepresentation = allRolesOrPermissions.find(
        (r) => r.name === role,
      );
      if (!roleRepresentation?.id) {
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

  const allPermissions = [...PERMISSIONS, ...ENV_SPECIAL_PERMISSIONS[env]];
  const missingPermissions = allPermissions.filter(
    (role) => !allRolesOrPermissions.some((r) => r.name === role),
  );
  const superfluousPermissions = rawRolesOrPermissions.filter(
    (role) =>
      !isRoleOrPermission(role) && role.name?.startsWith(CURRENT_VERSION),
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
    superfluousPermissions: superfluousPermissions
      .map((r) => r.name)
      .filter(isDefined),
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
async function syncRoles(realm: KnownRealm) {
  const { env } = syncRolesCommand.opts() as { env: KnownEnv };
  const kcAdminClient = await initKc(env);
  const {
    allRolesOrPermissionsMap,
    compositeRoles,
    missingCompositeRoles,
    missingPermissions,
    superfluousPermissions,
    missingRoles,
  } = {
    ...(await findMissingRoles(kcAdminClient, realm, env)),
  };

  console.info('Current state of roles and permissions');
  console.info('======================================');
  console.info({
    missingPermissions,
    superfluousPermissions,
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

  let deleted = false;
  if (superfluousPermissions.length > 0) {
    deleted = await deleteSuperfluousPermissions(
      kcAdminClient,
      realm,
      superfluousPermissions,
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
    removedSuperfluousPermissions: deleted ? superfluousPermissions : [],
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
    const results = (
      await runForRealms((realm) => syncRoles(realm as KnownRealm))
    ).filter(isDefined);
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
addUsersCommand.action(async () => {
  const {
    env: targetEnv,
    user: userInfo,
    roles: rawRoles,
    realm,
  } = addUsersCommand.opts() as {
    env: KnownEnv;
    user: string;
    roles: CurrentRole[];
    realm: KnownRealm;
  };

  if (!userInfo || !rawRoles) {
    addUsersCommand.addHelpText('after', 'Invalid user format');
    addUsersCommand.help({ error: true });
  }
  const [username, firstName, lastName, email] = userInfo.split(',');
  const roles = rawRoles.map((r) => r.trim());

  if (!username || !roles || roles.length === 0) {
    addUsersCommand.addHelpText(
      'after',
      'Please provide a username and at least one role',
    );
    addUsersCommand.help({ error: true });
  }

  const kcAdminClient = await initKc(targetEnv);
  const ADD_USER_PASSWORD = env[ADD_USER_PASSWORD_ENV];
  if (!ADD_USER_PASSWORD) {
    addUsersCommand.addHelpText(
      'after',
      `Please set the environment variable ${ADD_USER_PASSWORD_ENV} to the password for the new user`,
    );
    addUsersCommand.help({ error: true });
  }

  const foundUsers = await kcAdminClient.users.find({
    username,
    realm,
  });

  let userId: string | undefined;
  if (foundUsers.length < 1) {
    console.info(`User ${username} not found, creating...`);
    const newUser = await kcAdminClient.users.create({
      realm,
      username,
      email: email ?? `stip-${username}@mailbucket.dvbern.ch`,
      enabled: true,
      emailVerified: true,
      firstName,
      lastName,
      credentials: [
        {
          type: 'password',
          value: ADD_USER_PASSWORD,
          temporary: false,
        },
      ],
    });
    userId = newUser.id;
  } else {
    console.info(`User ${username} found, updating...`);
    userId = foundUsers[0].id;
  }

  if (!userId) {
    console.error(`User ${username} not found or created`);
    process.exit(1);
  }

  const userRoles = await kcAdminClient.users.listRealmRoleMappings({
    id: userId,
    realm,
  });

  const rolesToAdd = roles.filter(
    (role) => !userRoles.some((r) => r.name === role),
  );

  if (rolesToAdd.length > 0) {
    console.info(`Adding roles ${rolesToAdd.join(', ')} to user ${username}`);
    const rolesToAddRepr = rolesToAdd.map((role) => ({
      name: role,
    }));
    const rawRolesOrPermissions = (
      await kcAdminClient.roles.find({ realm })
    ).filter(isDefinedRoleOrPermission);
    const newRoles = rolesToAddRepr
      .map((rawRole) =>
        rawRolesOrPermissions.find((role) => role.name === rawRole.name),
      )
      .filter(isDefined);

    if (newRoles.length < 1) {
      console.info('No new roles to add');
      return;
    }
    await kcAdminClient.users.addRealmRoleMappings({
      id: userId,
      realm,
      roles: newRoles,
    });
    console.info(`Added roles ${rolesToAdd.join(', ')} to user ${username}`);
  } else {
    console.info(`User ${username} already has all roles`);
  }
});
program.parse(process.argv);
