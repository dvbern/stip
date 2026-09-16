import {
  APIRequestContext,
  Browser,
  BrowserContextOptions,
  Locator,
  Page,
  PlaywrightWorkerArgs,
  expect,
} from '@playwright/test';
import { addYears, format, getMonth } from 'date-fns';
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
  return expect(page.getByTestId('step-title').first()).toContainText(text, {
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
  return expect(
    page.getByTestId('dynamic-tranche-step-title').first(),
  ).toContainText(text, {
    ignoreCase: true,
    timeout: 10000,
  });
};

export const uploadFilesById = async (page: Page) => {
  const uploadButtons = page.getByTestId(/^button-document-upload-/);

  await expect(uploadButtons.first()).toBeVisible({ timeout: 10000 });

  // Resolve the stable, unique per-dokumentTyp testids up front.
  const testIds = await uploadButtons.evaluateAll((els) =>
    els
      .map((el) => el.getAttribute('data-testid'))
      .filter((id): id is string => !!id),
  );

  for (const testId of testIds) {
    const upload = page.getByTestId(testId);
    const uploadCall = page.waitForResponse(
      (response) =>
        response.url().includes('/api/v1/gesuchDokument') &&
        response.request().method() === 'POST',
    );
    await upload.scrollIntoViewIfNeeded();
    await upload.click();
    await page.getByTestId('file-input').setInputFiles(SmallImageFile);
    await uploadCall;
    await page.keyboard.press('Escape');
    await expect(page.getByTestId('file-input')).toHaveCount(0);
  }
};

export const uploadFiles = async (page: Page) => {
  const fristButton = page.getByTestId(/^button-document-upload-/).first();

  await expect(fristButton).toBeVisible({ timeout: 10000 });

  const uploads = await page
    .locator('[data-testid^="button-document-upload"]')
    .all();
  for (const upload of uploads) {
    await upload.click();
    const uploadCall = page.waitForResponse(
      async (response) =>
        response.url().includes('/api/v1/gesuchtranche') &&
        response.url().includes('/dokumenteToUpload/gs') &&
        response.request().method() === 'GET',
    );
    await page.getByTestId('file-input').setInputFiles(SmallImageFile);
    await uploadCall;
    await page.keyboard.press('Escape');
    await expect(page.getByTestId('file-input')).toHaveCount(0);
  }
};

export const acceptDocuments = async (page: Page) => {
  const acceptButtons = page.getByTestId('dokument-akzeptieren');

  await expect(acceptButtons.first()).toBeVisible({ timeout: 10000 });

  const count = await acceptButtons.count();

  for (let i = 0; i < count; i++) {
    const accept = page.getByTestId('dokument-akzeptieren').first();
    const documentsToUploadReq = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/sb',
    );
    const dokumenteReq = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumente/sb',
    );
    await accept.scrollIntoViewIfNeeded();
    await accept.click();
    await Promise.all([documentsToUploadReq, dokumenteReq]);
  }
};

export type ExplicitNull<T> = T extends object
  ? {
      [K in keyof T]-?: ExplicitNull<T[K]> | null;
    }
  : T | null;

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
  await locator.click();

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
    return x % 2 === 0;
  }

  let total = 0;

  for (let i = 0; i < 12; i += 1) {
    if (isEven(i)) total += ssn[i];
    else total += ssn[i] * 3;
  }

  let expectedCheckDigit = 0;
  if (total % 10 !== 0) {
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
export const fruehlingOrHerbst = () => {
  if (getMonth(new Date()) < 6) {
    return 1;
  }
  return 9;
};
export const specificMonthPlusYears = (month: number, years: number) =>
  `${month}.${format(addYears(new Date(), years), 'yyyy')}`;
export const specificYearsAgo = (years: number) =>
  format(addYears(new Date(), -years), 'yyyy');
export const secondTrancheStart = () =>
  `1.${fruehlingOrHerbst() + 2}.${thisYear}`;

export type SetupFn = (args: {
  contexts: TestContexts;
  seed: string;
  gesuchId: string;
  trancheId: string;
  fallId: string;
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
  const gsBearerCookie = gsCookies.find((c) => c.name === BEARER_COOKIE);
  const gsBearer = gsBearerCookie?.value
    ? await decompress(gsBearerCookie.value)
    : undefined;

  const gsApiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${gsBearer}`,
    },
  });

  // Create SB context
  const sbBrowserContext = await browser.newContext({
    storageState: sbStorageState,
  });
  const sbCookies = await sbBrowserContext.cookies();
  const sbBearerCookie = sbCookies.find((c) => c.name === BEARER_COOKIE);
  const sbBearer = sbBearerCookie?.value
    ? await decompress(sbBearerCookie.value)
    : undefined;
  const sbApiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${sbBearer}`,
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
