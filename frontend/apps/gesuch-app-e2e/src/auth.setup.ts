import { test as setup } from '@playwright/test';

import {
  authenticateAndSaveStorageState,
  getE2eUrls,
  gsStorageStatePath,
  sbStorageStatePath,
} from '@dv/shared/util-fn/e2e-util';

/**
 * Authenticate all e2e users once, before the parallel workers start.
 *
 * One Gesuchsteller account per worker (to avoid interference on shared
 * server-side state) plus a single shared Sachbearbeiter account. Running this
 * as a `setup` project dependency makes the login count deterministic and
 * removes the per-worker authentication race.
 */
setup('authenticate users', async ({ browser }, testInfo) => {
  setup.setTimeout(120_000);

  const urls = getE2eUrls();
  const workerCount = testInfo.config.workers;

  for (let workerIndex = 0; workerIndex < workerCount; workerIndex++) {
    const accountId = workerIndex + 1;
    const username = process.env[`E2E_GESUCHSTELLER_${accountId}_USERNAME`];
    const password = process.env[`E2E_GESUCHSTELLER_${accountId}_PASSWORD`];

    if (!username || !password) {
      throw new Error(
        `E2E_GESUCHSTELLER_${accountId}_USERNAME and E2E_GESUCHSTELLER_${accountId}_PASSWORD are required (one Gesuchsteller per worker).`,
      );
    }

    await authenticateAndSaveStorageState(browser, {
      username,
      password,
      baseURL: urls.gs,
      storagePath: gsStorageStatePath(testInfo, workerIndex),
    });
  }

  const sbUsername = process.env['E2E_SACHBEARBEITER_1_USERNAME'];
  const sbPassword = process.env['E2E_SACHBEARBEITER_1_PASSWORD'];

  if (!sbUsername || !sbPassword) {
    throw new Error(
      'E2E_SACHBEARBEITER_1_USERNAME and E2E_SACHBEARBEITER_1_PASSWORD are required.',
    );
  }

  await authenticateAndSaveStorageState(browser, {
    username: sbUsername,
    password: sbPassword,
    baseURL: urls.sb,
    storagePath: sbStorageStatePath(testInfo),
  });
});
