import {
  APIRequestContext,
  Browser,
  BrowserContextOptions,
  Locator,
  Page,
  PlaywrightWorkerArgs,
  expect,
} from '@playwright/test';
import { addYears, format } from 'date-fns';
import seedRandom from 'seedrandom';

import { SmallImageFile } from './files';
import { BEARER_COOKIE, decompress } from './playwright.config.base';

/**
 * works for all steptitles exept for "info" route (tranche component)
 */
export const expectStepTitleToContainText = async (
  text: string,
  page: Page,
) => {
  return expect(page.getByTestId('step-title')).toContainText(text, {
    timeout: 10000,
  });
};

/**
 * works for "info" route (tranche component)
 */
export const expectInfoTitleToContainText = async (
  text: string,
  page: Page,
) => {
  return expect(page.getByTestId('dynamic-tranche-step-title')).toContainText(
    text,
    {
      ignoreCase: true,
      timeout: 10000,
    },
  );
};

export const uploadFiles = async (page: Page) => {
  const uploads = await page
    .locator('[data-testid^="button-document-upload"]')
    .all();
  for (const upload of uploads) {
    const uploadCall = page.waitForResponse(
      (response) =>
        response.url().includes('/api/v1/gesuchDokument') &&
        response.request().method() === 'POST',
    );
    await upload.click();
    await page.getByTestId('file-input').setInputFiles(SmallImageFile);
    await page.keyboard.press('Escape');
    await expect(page.getByTestId('file-input')).toHaveCount(0);
    await uploadCall;
  }
};

export type DeepNullable<T> = {
  [K in keyof T]: DeepNullable<T[K]> | null;
};

export const handleCheckbox = async (
  matCheckboxComp: Locator,
  value: boolean | undefined,
  options: {
    force?: boolean;
    noWaitAfter?: boolean;
    position?: {
      x: number;
      y: number;
    };
    timeout?: number;
    trial?: boolean;
  } = {},
) => {
  if (value === undefined) {
    return;
  }

  const checkbox = matCheckboxComp.locator('input[type="checkbox"]');

  if (value) {
    return checkbox.check(options);
  }

  return checkbox.uncheck(options);
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

export const fillLandAutoComplete = async (
  autocomplete: Locator,
  value: string,
  page: Page,
) => {
  await autocomplete.fill(value);

  const option = page.getByTestId(value);

  await option.click();
};

export const expectFormToBeValid = async (form: Locator) => {
  return expect(form).toHaveClass(/ng-valid/);
};

export const deleteGesuch = async (
  apiContext: APIRequestContext,
  gesuchId: string,
) => {
  return await apiContext.delete(`/api/v1/gesuch/${gesuchId}`);
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
  const bearerCookie = cookies.find((c) => c.name === BEARER_COOKIE);
  const bearer = bearerCookie?.value
    ? await decompress(bearerCookie.value)
    : undefined;

  const apiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${bearer}`,
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
 * Generates a random SVN number in the format 756.4849.0227.44
 */
export const generateSVN = (seed: string) => {
  const countryCode = [7, 5, 6];

  const rng = seedRandom(seed);
  const randomNumbers = Array.from({ length: 9 }, () => Math.floor(rng() * 10));
  const ssnWithoutCheckDigit = countryCode.concat(randomNumbers);
  const checkDigit = getCheckDigit(ssnWithoutCheckDigit);
  const unformattedSSN = ssnWithoutCheckDigit.concat(checkDigit);
  const ssn = ssnFormatter(unformattedSSN);
  return ssn;
};

/**
 * https://github.com/teaddict/swiss-ssn-avs-ahv/blob/master/src/swiss-ssn.js
 */

const getCheckDigit = (ssn: number[]) => {
  function isEven(x: number) {
    return x % 2 == 0;
  }

  let total = 0;

  for (let i = 0; i < 12; i += 1) {
    if (isEven(i)) total += ssn[i];
    else total += ssn[i] * 3;
  }

  let expectedCheckDigit = 0;
  if (total % 10 != 0) {
    const roundTen = Math.floor(total / 10) * 10 + 10;
    expectedCheckDigit = roundTen - total;
  }

  return expectedCheckDigit;
};

const ssnFormatter = (ssn: number[]) => {
  const ssnString = ssn.map(String);
  const formattedSSN =
    '756.' +
    ssnString.slice(3, 7).join('') +
    '.' +
    ssnString.slice(7, 11).join('') +
    '.' +
    ssnString.slice(11, 13).join('');
  return formattedSSN;
};

export const thisYear = format(new Date(), 'yyyy');
export const specificMonth = (month: number) =>
  `${month}.${format(new Date(), 'yyyy')}`;
export const specificMonthPlusYears = (month: number, years: number) =>
  `${month}.${format(addYears(new Date(), years), 'yyyy')}`;
export const specificYearsAgo = (years: number) =>
  format(addYears(new Date(), -years), 'yyyy');
export const today = () => format(new Date(), 'dd.MM.yyyy');

export type SetupFn = (args: {
  contexts: TestContexts;
  seed: string;
  gesuchId: string;
  trancheId: string;
}) => Promise<void>;

/**
 * Create test contexts for multiple user types
 */
export const createMultiUserTestContexts = async (options: {
  playwright: PlaywrightWorkerArgs['playwright'];
  browser: Browser;
  gsStorageState: string;
  sbStorageState: string;
  baseURL?: string;
}) => {
  const { playwright, browser, gsStorageState, sbStorageState, baseURL } =
    options;

  // Create GS context
  const gsBrowserContext = await browser.newContext({
    storageState: gsStorageState,
  });
  const gsCookies = await gsBrowserContext.cookies();
  const gsBearer = gsCookies.find((c) => c.name === BEARER_COOKIE);
  const gsApiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${gsBearer?.value}`,
    },
  });

  // Create SB context
  const sbBrowserContext = await browser.newContext({
    storageState: sbStorageState,
  });
  const sbCookies = await sbBrowserContext.cookies();
  const sbBearer = sbCookies.find((c) => c.name === BEARER_COOKIE);
  const sbApiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${sbBearer?.value}`,
    },
  });

  return {
    gs: {
      browser: gsBrowserContext,
      api: gsApiContext,
    },
    sb: {
      browser: sbBrowserContext,
      api: sbApiContext,
    },
    dispose: async () => {
      try {
        await Promise.all([
          gsApiContext.dispose(),
          sbApiContext.dispose(),
          gsBrowserContext.close(),
          sbBrowserContext.close(),
        ]);
      } catch (e) {
        console.warn('Failed to dispose multi-user e2e contexts', e);
      }
    },
  };
};

export type MultiUserTestContexts = Awaited<
  ReturnType<typeof createMultiUserTestContexts>
>;
