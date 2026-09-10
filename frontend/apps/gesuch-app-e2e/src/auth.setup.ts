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
 * server-side state) plus two shared Sachbearbeiter accounts.
 */
// todo: make users configurable as well?
setup('authenticate users', async ({ browser }, testInfo) => {
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

  const sbWorkerCount = 2;
  for (let i = 0; i < sbWorkerCount; i++) {
    const accountId = i + 1;
    const sbUsername = process.env[`E2E_SACHBEARBEITER_${accountId}_USERNAME`];
    const sbPassword = process.env[`E2E_SACHBEARBEITER_${accountId}_PASSWORD`];

    if (!sbUsername || !sbPassword) {
      throw new Error(
        `E2E_SACHBEARBEITER_${accountId}_USERNAME and E2E_SACHBEARBEITER_${accountId}_PASSWORD are required.`,
      );
    }

    await authenticateAndSaveStorageState(browser, {
      username: sbUsername,
      password: sbPassword,
      baseURL: urls.sb,
      storagePath: sbStorageStatePath(testInfo, i),
    });
  }
});
