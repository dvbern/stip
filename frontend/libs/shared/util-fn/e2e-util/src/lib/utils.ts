import {
  APIRequestContext,
  Browser,
  BrowserContextOptions,
  Locator,
  Page,
  PlaywrightWorkerArgs,
  Project,
  defineConfig,
  devices,
  expect,
  test,
} from '@playwright/test';
import { addSeconds } from 'date-fns';

import {
  AuthenticatedTest,
  BEARER_COOKIE,
  KeycloakResponse,
  REFRESH_COOKIE,
  SetupTest,
  authenticatedTestOptions,
  setupTestOptions,
} from './playwright.config.base';

export const getStepTitle = async (page: Page) => {
  return page.getByTestId('step-title');
};

export const expectStepTitleToContainText = async (
  text: string,
  page: Page,
) => {
  return expect(await getStepTitle(page)).toContainText(text);
};

export const selectMatOption = async (locator: Locator, value: string) => {
  locator.click();

  return locator.page().getByTestId(value).first().click();
};

export const selectMatRadio = async (
  locator: Locator,
  value: string | boolean,
) => {
  if (typeof value === 'boolean') {
    value = value ? 'yes' : 'no';
  }

  return locator.getByTestId(value).getByRole('radio').click();
};

export const expectFormToBeValid = async (form: Locator) => {
  return expect(form).toHaveClass(/ng-valid/);
};

export const deleteGesuch = async (
  apiContext: APIRequestContext,
  gesuchId: string,
) => {
  await apiContext.delete(`/api/v1/gesuch/${gesuchId}`);
};

const usedStorageStatePaths = new Set<string>();
/**
 * Create a test configuration with setup and test project
 */
export const createTestConfigWithSetup = (config: {
  dir: string;
  name: string;
  storageStatePath: string;
  fixtures: AuthenticatedTest;
}): [
  ReturnType<typeof defineConfig<SetupTest<AuthenticatedTest>>>,
  Project,
] => {
  if (usedStorageStatePaths.has(config.storageStatePath)) {
    throw new Error(
      `Storage state path must be unique, ${config.storageStatePath} is already has already been used`,
    );
  }
  usedStorageStatePaths.add(config.storageStatePath);
  const { dir, name, storageStatePath, fixtures } = config;
  return [
    {
      name: `${name}-setup`,
      testMatch: /.*\.setup\.ts/,
      use: {
        ...fixtures,
        storageStatePath,
      },
    },
    {
      name,
      testDir: `src/tests/${dir}`,
      use: {
        ...devices['Desktop Chrome'],
        ...fixtures,
        storageState: storageStatePath,
      },
      dependencies: [`${name}-setup`],
    },
  ];
};

/**
 * Create test contexts for browser and API requests.
 *
 * @see https://playwright.dev/docs/api-testing#sending-api-requests-from-ui-tests
 */
export const createTestContexts = async (options: {
  playwright: PlaywrightWorkerArgs['playwright'];
  browser: Browser;
  storageState?: BrowserContextOptions['storageState'];
  baseURL?: string;
}) => {
  const { storageState, playwright, browser, baseURL } = options;
  const browserContext = await browser.newContext({
    storageState,
  });

  const cookies = await browserContext.cookies();
  const bearer = cookies.find((c) => c.name === BEARER_COOKIE);

  const apiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${bearer?.value}`,
    },
  });

  return {
    browser: browserContext,
    api: apiContext,
    dispose: async () => {
      try {
        await apiContext.dispose();
        await browserContext.close();
      } catch (e) {
        console.warn('Failed to dispose e2e contexts', e);
      }
    },
  };
};
export type TestContexts = Awaited<ReturnType<typeof createTestContexts>>;

/**
 * Authenticate a user with Keycloak using given fixture options
 */
export const authenticateUser = () => {
  test.extend<SetupTest<AuthenticatedTest>>(
    setupTestOptions(authenticatedTestOptions),
  )('authenticate', async ({ page, authentication, storageStatePath }) => {
    const username = process.env[`E2E_${authentication}_USERNAME`];
    const password = process.env[`E2E_${authentication}_PASSWORD`];
    if (!username || !password) {
      throw new Error(
        `E2E_${authentication}_USERNAME and E2E_${authentication}_PASSWORD environment variables are required`,
      );
    }

    await page.goto('');
    await page.getByLabel('Username or email').fill(username);
    await page.getByLabel('Password', { exact: true }).fill(password);

    const responsePromise = page.waitForResponse(
      '**/realms/bern/protocol/openid-connect/token',
    );

    await page.getByRole('button', { name: 'Sign In' }).click();

    const response = await responsePromise;
    const url = new URL(response.url());
    const body: KeycloakResponse = await response.json();

    const unixTime = addSeconds(Date.now(), body.expires_in).getTime() / 1000;

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
    await page.context().storageState({ path: storageStatePath });
  });
};
