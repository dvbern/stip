import { expect } from '@playwright/test';

import { createTest } from '@dv/shared/util-fn/e2e-util';

import { CockpitPO } from '../po/cockpit.po';

const test = createTest('SACHBEARBEITER').extend<{ cockpit: CockpitPO }>({
  cockpit: async ({ page }, use) => {
    const cockpit = new CockpitPO(page);

    await cockpit.goToDashBoard();

    await use(cockpit);
  },
});

test.describe('Sachbearbeiter App: Gesuche Kontrollieren', () => {
  test('Gesuch ist vorhanden', async ({ cockpit }) => {
    await expect(cockpit.elems.title).toHaveText('Gesuche');
  });
});
