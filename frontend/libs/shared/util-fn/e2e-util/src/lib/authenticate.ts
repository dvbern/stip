import fs from 'fs';
import path from 'path';

import { Browser, Page, TestInfo } from '@playwright/test';
import { addSeconds } from 'date-fns';

import {
  BEARER_COOKIE,
  E2eUser,
  KeycloakResponse,
  REFRESH_COOKIE,
  compress,
} from './playwright.config.base';

export * from '@playwright/test';

/**
 * Directory where the `setup` project writes the per-role authentication
 * storage states that the multi-user fixtures later read. Derived from the
 * config root so the setup project and the test projects agree on the path.
 */
export const getAuthDir = (info: TestInfo) =>
  path.resolve(info.config.rootDir, '../playwright/.auth');

export const gsStorageStatePath = (info: TestInfo, workerIndex: number) =>
  path.join(getAuthDir(info), `gs_${workerIndex}.json`);

export const sbStorageStatePath = (info: TestInfo, index = 0) =>
  path.join(getAuthDir(info), `sb_${index}.json`);

const sessionStatePath = (storagePath: string) =>
  storagePath.replace(/\.json$/, '.session.json');

/**
 * Restore the sessionStorage captured during authentication for the given app
 * origin. angular-oauth2-oidc keeps its tokens in sessionStorage, which
 * Playwright's storageState does not persist, so without this the app finds no
 * token on the first navigation and redirects to the login screen.
 *
 * Must be called before the first navigation of the page.
 */
export const restoreSessionStorage = async (
  page: Page,
  storagePath: string,
  appOrigin: string,
) => {
  const sessionFile = sessionStatePath(storagePath);
  if (!fs.existsSync(sessionFile)) {
    return;
  }

  const sessionStorageJson = await fs.promises.readFile(sessionFile, 'utf-8');

  await page.addInitScript(
    ({ json, origin }) => {
      if (window.location.origin !== origin) {
        return;
      }
      const entries: Record<string, string> = JSON.parse(json);
      for (const [key, value] of Object.entries(entries)) {
        window.sessionStorage.setItem(key, value);
      }
    },
    { json: sessionStorageJson, origin: appOrigin },
  );
};

/**
 * Perform a Keycloak UI login and persist the resulting storage state to disk.
 *
 * Intended to be called from a Playwright `setup` project so authentication
 * happens once, before the parallel workers start (no per-worker race).
 */
export const authenticateAndSaveStorageState = async (
  browser: Browser,
  options: {
    username: string;
    password: string;
    storagePath: string;
    baseURL?: string;
  },
) => {
  const { username, password, storagePath, baseURL } = options;

  const page = await browser.newPage({
    storageState: undefined,
    baseURL,
    ignoreHTTPSErrors: true,
  });

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

  const accessToken = await compress(body.access_token);
  const refreshToken = await compress(body.refresh_token);

  const unixTime = addSeconds(Date.now(), body.expires_in).getTime() / 1000;

  await page.context().addCookies([
    {
      name: BEARER_COOKIE,
      value: accessToken,
      domain: url.host,
      path: '/realms/bern/',
      expires: unixTime,
      httpOnly: false,
      secure: true,
      sameSite: 'Lax',
    },
    {
      name: REFRESH_COOKIE,
      value: refreshToken,
      domain: url.host,
      path: '/realms/bern/',
      expires: -1,
      httpOnly: false,
      secure: true,
      sameSite: 'Lax',
    },
  ]);

  // angular-oauth2-oidc keeps its tokens in sessionStorage, which Playwright's
  // storageState does not persist. Capture it so the fixtures can restore it and
  // avoid a silent-refresh/login redirect on the first navigation.
  await page.waitForFunction(
    () => !!window.sessionStorage.getItem('access_token'),
    undefined,
    { timeout: 30_000 },
  );
  const sessionStorageJson = await page.evaluate(() =>
    JSON.stringify(window.sessionStorage),
  );

  await fs.promises.mkdir(path.dirname(storagePath), { recursive: true });
  await page.context().storageState({ path: storagePath });
  await fs.promises.writeFile(
    sessionStatePath(storagePath),
    sessionStorageJson,
    'utf-8',
  );
  await page.close();
};

/**
 * Authenticate a user for the currently running e2e test.
 *
 * @see https://playwright.dev/docs/auth#moderate-one-account-per-parallel-worker
 */
// deprecated implementation of test creation with authentication
// authentication now happens in setup.
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
        const accessToken = await compress(body.access_token);
        const refreshToken = await compress(body.refresh_token);

        const unixTime =
          addSeconds(Date.now(), body.expires_in).getTime() / 1000;

        await page.context().addCookies([
          {
            name: BEARER_COOKIE,
            value: accessToken,
            domain: url.host,
            path: '/realms/bern/',
            expires: unixTime,
            httpOnly: false,
            secure: true,
            sameSite: 'Lax',
          },
          {
            name: REFRESH_COOKIE,
            value: refreshToken,
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
