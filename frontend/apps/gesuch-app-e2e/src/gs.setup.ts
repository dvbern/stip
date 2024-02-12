import { test as setup } from '@playwright/test';
import { addSeconds } from 'date-fns';

import {
  BEARER_COOKIE,
  GS_STORAGE_STATE,
  KeycloakResponse,
  REFRESH_COOKIE,
} from '@dv/shared/util-fn/e2e-util';

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
  await page.context().storageState({ path: GS_STORAGE_STATE });
});
