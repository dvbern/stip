import {
  APIRequestContext,
  Browser,
  BrowserContextOptions,
  Locator,
  Page,
  PlaywrightWorkerArgs,
  expect,
} from '@playwright/test';
import seedRandom from 'seedrandom';

import { BEARER_COOKIE } from './playwright.config.base';

// Don't know why it fails, because the package is referenced in the package.json

export const getStepTitle = async (page: Page) => {
  return page.getByTestId('step-title');
};

export const expectStepTitleToContainText = async (
  text: string,
  page: Page,
) => {
  return expect(await getStepTitle(page)).toContainText(text);
};

export const handleCheckbox = async (
  checkbox: Locator,
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

  if (value) {
    return checkbox.check(options);
  }

  return checkbox.uncheck();
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
