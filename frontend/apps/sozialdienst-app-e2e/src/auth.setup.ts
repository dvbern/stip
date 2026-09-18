import {
  authenticateAndSaveStorageState,
  getE2eUrls,
  extendedTest as setup,
  sozStorageStatePath,
} from '@dv/shared/util-fn/e2e-util';

// todo-e2e-next: remove duplicate authentication setup across e2e tests
setup('authenticate users', async ({ browser, testUser }, testInfo) => {
  const urls = getE2eUrls();

  if (!testUser) {
    throw new Error('testUser is required.');
  }

  const username = process.env[`E2E_${testUser}_1_USERNAME`];
  const password = process.env[`E2E_${testUser}_1_PASSWORD`];

  if (!username || !password) {
    throw new Error(
      `E2E_${testUser}_1_USERNAME and E2E_${testUser}_1_PASSWORD are required.`,
    );
  }

  await authenticateAndSaveStorageState(browser, {
    username,
    password,
    baseURL: urls.soz,
    storagePath: sozStorageStatePath(testInfo, 0),
  });
});
