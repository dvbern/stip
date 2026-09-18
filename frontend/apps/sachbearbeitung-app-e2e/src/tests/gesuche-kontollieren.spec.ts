import { expect } from '@playwright/test';

import {
  extendedTest,
  initializeSingleUserTest,
} from '@dv/shared/util-fn/e2e-util';

import { SbCockpitPO } from '../po/cockpit.po';

const test = initializeSingleUserTest(extendedTest).extend<{
  cockpit: SbCockpitPO;
}>({
  cockpit: async ({ page }, use) => {
    const cockpit = new SbCockpitPO(page);

    await cockpit.goToDashBoard();

    await use(cockpit);
  },
});

test.describe('Sachbearbeiter App: Gesuche Kontrollieren', () => {
  test('Gesuch ist vorhanden', async ({ cockpit }) => {
    await expect(cockpit.elems.title).toHaveText('Gesuche');
  });
});
