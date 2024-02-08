import { test as setup } from '@playwright/test';
import { addSeconds } from 'date-fns';
import * as dotenv from 'dotenv';

import {
  BEARER_COOKIE,
  REFRESH_COOKIE,
  SB_STORAGE_STATE,
} from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: '../../.env' });

interface KeycloakResponse {
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

setup('authenticate', async ({ page }) => {
  const username = process.env['E2E_USERNAME'];
  const password = process.env['E2E_PASSWORD'];

  if (!username || !password) {
    throw new Error(
      'E2E_USERNAME and E2E_PASSWORD environment variables are required',
    );
  }

  await page.goto('');
  await page.getByLabel('Username or email').fill(username);
  await page.getByLabel('Password', { exact: true }).fill(password);

  // todo: get from environment
  const responsePromise = page.waitForResponse(
    'https://dev-auth-stip.apps.mercury.ocp.dvbern.ch/realms/bern/protocol/openid-connect/token',
  );

  await page.getByRole('button', { name: 'Sign In' }).click();

  const response = await responsePromise;
  const body: KeycloakResponse = await response.json();

  const unixTime = addSeconds(Date.now(), body.expires_in).getTime() / 1000;

  await page.context().addCookies([
    {
      name: BEARER_COOKIE,
      value: body.access_token,
      domain: 'dev-auth-stip.apps.mercury.ocp.dvbern.ch',
      path: '/realms/bern/',
      expires: unixTime,
      httpOnly: false,
      secure: true,
      sameSite: 'Lax',
    },
    {
      name: REFRESH_COOKIE,
      value: body.refresh_token,
      domain: 'dev-auth-stip.apps.mercury.ocp.dvbern.ch',
      path: '/realms/bern/',
      expires: -1,
      httpOnly: false,
      secure: true,
      sameSite: 'Lax',
    },
  ]);

  // End of authentication steps.
  await page.context().storageState({ path: SB_STORAGE_STATE });
});
