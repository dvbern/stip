import test, { defineConfig } from '@playwright/test';
import { zstdCompress, zstdDecompress } from 'http-encoding';

export const BEARER_COOKIE = 'access_cookie';
export const REFRESH_COOKIE = 'refresh_cookie';

const gsURL = process.env['E2E_BASEURL_GS'];
const sbURL = process.env['E2E_BASEURL_SB'];
const sozURL = process.env['E2E_BASEURL_SOZ'];
export const getE2eUrls = () => {
  return {
    gs: gsURL ?? '',
    sb: sbURL ?? '',
    soz: sozURL ?? '',
  };
};

export interface KeycloakResponse {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
  token_type: string;
  id_token: string;
  'not-before-policy': number;
  session_state: string;
  scope: string;
}

/**
 * Available users for e2e tests, ensure that the environment variables are set in CI
 *
 * In order to add new users, the following steps are required:
 * 1. Create the password and add the desired username to the Stip Group in LastPass
 * 2. Add the user to the environment variables in the local .env file
 * 3. Add the user to the Keycloak realm https://dev-auth-stip.apps.mercury.ocp.dvbern.ch/admin/master/console/
 * 4. Add the user to the environment variables in the CI configuration https://gitlab.dvbern.ch/stip/stip/-/settings/ci_cd -> Variables
 */
export type E2eUser =
  | 'GESUCHSTELLER'
  | 'SACHBEARBEITER'
  | 'SACHBEARBEITER_ADMIN'
  | 'SOZIALDIENST'
  | 'SOZIALDIENST_ADMIN';

export type CustomTestOptions = {
  testUser: E2eUser;
};

export const extendedTest = test.extend<CustomTestOptions>({
  testUser: ['GESUCHSTELLER', { option: true }],
});

export type ExtendedTest = typeof extendedTest;

/**
 * Some default configuration for e2e tests
 */
export const baseConfig = defineConfig({
  use: {
    trace: 'on-first-retry',
    ignoreHTTPSErrors: true,
    screenshot: {
      mode: 'only-on-failure',
    },
    // viewport: { width: 1440, height: 980 },
    video: {
      mode: 'off',
      size: { width: 1280, height: 720 },
    },
    contextOptions: {
      ignoreHTTPSErrors: true,
    },
    actionTimeout: !process.env.CI ? 5_000 : undefined,
  },
  expect: {
    timeout: process.env.CI ? 20_000 : undefined,
  },
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: 1,
});

export const compress = async (value: string) => {
  const compressed = await zstdCompress(new TextEncoder().encode(value));
  return Buffer.from(compressed).toString('base64');
};

export const decompress = async (value: string) => {
  const decompressed = await zstdDecompress(Buffer.from(value, 'base64'));
  return new TextDecoder().decode(decompressed);
};
