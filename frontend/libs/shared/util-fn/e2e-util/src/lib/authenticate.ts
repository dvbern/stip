import fs from 'fs';
import path from 'path';

import { test as baseTest } from '@playwright/test';
import { addSeconds } from 'date-fns';

import {
  BEARER_COOKIE,
  E2eUser,
  KeycloakResponse,
  REFRESH_COOKIE,
} from './playwright.config.base';

export * from '@playwright/test';

/**
 * Authenticate a user for the currently running e2e test.
 *
 * @see https://playwright.dev/docs/auth#moderate-one-account-per-parallel-worker
 */
export const createTest = (
  authType: E2eUser,
  options?: { contextPerTest?: boolean },
) => {
  const test = baseTest.extend<object, { workerStorageState: string }>({
    contextOptions: async ({ baseURL }, use) => {
      await use({
        ignoreHTTPSErrors: true,
        baseURL,
      });
    },

    // Use the same storage state for all tests in this worker.
    storageState: ({ workerStorageState }, use) => use(workerStorageState),

    // Authenticate once per worker with a worker-scoped fixture.
    workerStorageState: [
      async ({ browser }, use, workerInfo) => {
        // Use parallelIndex as a unique identifier for each worker.
        const id = test.info().parallelIndex + 1;
        const optionalSuffix = options?.contextPerTest
          ? `_${test.info().testId}`
          : '';
        const fileName = path.resolve(
          test.info().project.outputDir,
          `.auth/${id}${optionalSuffix}.json`,
        );

        if (fs.existsSync(fileName)) {
          // Reuse existing authentication state if any.
          await use(fileName);
          return;
        }

        // Important: make sure we authenticate in a clean environment by unsetting storage state.
        const page = await browser.newPage({
          storageState: undefined,
          // Don't know why it is necessary to set the baseURL again, should be inherited from the use context.
          baseURL: workerInfo.project.use.baseURL,
          ignoreHTTPSErrors: true,
        });

        const username = process.env[`E2E_${authType}_${id}_USERNAME`];
        const password = process.env[`E2E_${authType}_${id}_PASSWORD`];

        if (!username || !password) {
          throw new Error(
            `E2E_${authType}_${id}_USERNAME and E2E_${authType}_${id}_PASSWORD environment variables are required,` +
              'there are probably more parallel tests running than available users',
          );
        }

        await page.goto('/');
        await page.getByLabel('Username or email').fill(username);
        await page.getByLabel('Password', { exact: true }).fill(password);

        const responsePromise = page.waitForResponse(
          '**/realms/bern/protocol/openid-connect/token',
        );

        await page.getByRole('button', { name: 'Sign In' }).click();

        const response = await responsePromise;
        const url = new URL(response.url());
        const body: KeycloakResponse = await response.json();

        const unixTime =
          addSeconds(Date.now(), body.expires_in).getTime() / 1000;

        await page.context().addCookies([
          {
            name: BEARER_COOKIE,
            value: body.access_token,
            domain: url.host,
            path: '/realms/bern/',
            expires: unixTime,
            httpOnly: false,
            secure: true,
            sameSite: 'Lax',
          },
          {
            name: REFRESH_COOKIE,
            value: body.refresh_token,
            domain: url.host,
            path: '/realms/bern/',
            expires: -1,
            httpOnly: false,
            secure: true,
            sameSite: 'Lax',
          },
        ]);

        // End of authentication steps.

        await page.context().storageState({ path: fileName });
        await page.close();
        await use(fileName);
      },
      { scope: 'worker' },
    ],
  });
  return test;
};
