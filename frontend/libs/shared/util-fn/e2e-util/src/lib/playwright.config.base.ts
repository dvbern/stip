import { defineConfig } from '@playwright/test';

export const BEARER_COOKIE = 'access_cookie';
export const REFRESH_COOKIE = 'refresh_cookie';

const gsURL = process.env['E2E_BASEURL_GS'];
const sbURL = process.env['E2E_BASEURL_SB'];
export const getE2eUrls = () => {
  return {
    gs: gsURL ?? '',
    sb: sbURL ?? '',
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
export type E2eUser = 'GESUCHSTELLER' | 'SACHBEARBEITER' | 'ADMIN';

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
    video: {
      mode: 'retain-on-failure',
      // size: { width: 1920, height: 1080 },
      // smaller video size to reduce file size
      size: { width: 1280, height: 720 },
    },
    contextOptions: {
      ignoreHTTPSErrors: true,
    },
  },
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: 1,
  // workers: 2,
});
