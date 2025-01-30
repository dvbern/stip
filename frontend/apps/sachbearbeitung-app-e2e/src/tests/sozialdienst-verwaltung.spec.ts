import { expect } from '@playwright/test';

import { createTest } from '@dv/shared/util-fn/e2e-util';

import { AdminPO } from '../po/admin.po';
import { CockpitPO } from '../po/cockpit.po';

const E2E_TEST_SOZIALDIENST = 'e2e-test-sozialdienst';

const test = createTest('SACHBEARBEITER').extend<{ cockpit: CockpitPO }>({
  cockpit: async ({ page }, use) => {
    const cockpit = new CockpitPO(page);

    await cockpit.goToDashBoard();

    await use(cockpit);
  },
});

test.describe('Sachbearbeiter App: Sozialdienst-Administration', () => {
  test('Sozialdienst erstellen und löschen', async ({ cockpit, page }) => {
    cockpit.goToAdmin();

    const admin = new AdminPO(page);
    await admin.goToAdmin();
    await admin.sozialdienste.goToCreate();
    await admin.sozialdienste.fillOutNewSozialdienstForm({
      name: E2E_TEST_SOZIALDIENST,
    });
    await admin.sozialdienste.save();
    await admin.sozialdienste.goToOverview();
    await admin.sozialdienste.filter(E2E_TEST_SOZIALDIENST);

    await expect(admin.elems.sozialdienst.rows, {
      message: 'Create Sozialdienst probably was not successfull',
    }).toHaveCount(2);

    await admin.sozialdienste.deleteSozialdienst(E2E_TEST_SOZIALDIENST);
    await page.getByTestId('dialog-confirm').click();

    await expect(admin.elems.sozialdienst.rows, {
      message: 'Delete Sozialdienst probably was not successfull',
    }).toHaveCount(2);
  });
});
