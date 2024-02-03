import { test as setup } from '@playwright/test';
import { addMilliseconds } from 'date-fns';

import { BEARER_COOKIE, REFRESH_COOKIE } from './helpers/types';
import { STIP_STORAGE_STATE } from '../playwright.config';

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
  const username = process.env['E2E_USERNAME'] || 'e2e';
  const password = process.env['E2E_PASSWORD'] || 'NKsA.pu*ros6LmDu';

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

  // cookies want expiry in seconds as unix time
  const unixTime =
    addMilliseconds(Date.now(), body.expires_in).getTime() / 1000;

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
  await page.context().storageState({ path: STIP_STORAGE_STATE });
});
