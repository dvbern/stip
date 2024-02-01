import { expect, test as setup } from '@playwright/test';

import { STIP_STORAGE_STATE } from '../playwright.config';

setup('authenticate', async ({ page }) => {
  const username = process.env['E2E_USERNAME'] || 'e2e';
  const password = process.env['E2E_PASSWORD'] || 'NKsA.pu*ros6LmDu';

  // Perform authentication steps. Replace these actions with your own.
  await page.goto('');
  // await page.waitForURL('https://dev-auth-stip.apps.mercury.ocp.dvbern.ch');
  await page.getByLabel('Username or email').fill(username);
  await page.getByLabel('Password', { exact: true }).fill(password);
  await page.getByRole('button', { name: 'Sign In' }).click();
  // Wait until the page receives the cookies.
  //
  // Sometimes login flow sets cookies in the process of several redirects.
  // Wait for the final URL to ensure that the cookies are actually set.
  // await page.waitForURL('https://github.com/');
  // await page.waitForURL('/gesuch-app-feature-cockpit');
  // Alternatively, you can wait until the page reaches a state where all cookies are set.
  await expect(page.locator('h1')).toContainText('Guten Morgen');
  // End of authentication steps.
  await page.context().storageState({ path: STIP_STORAGE_STATE });
});
