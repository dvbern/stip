import { expect } from '@playwright/test';

import { createTest } from '@dv/shared/util-fn/e2e-util';

import { AdminPO } from '../po/admin.po';

const E2E_TEST_SOZIALDIENST_MITARBEITER = 'e2e-test-soz-mitarbeiter';

const test = createTest('SOZIALDIENST_ADMIN', { contextPerTest: true });

test.describe('Sachbearbeiter App: Sozialdienst-Mitarbeiter', () => {
  test('Sozialdienst mitarbeiter erstellen, bearbeiten und löschen', async ({
    page,
  }) => {
    page.goto('/');
    const admin = new AdminPO(page);

    await admin.goToAdmin();
    // Step 1: Erstellen ========================================================
    await admin.sozialdienstMitarbeiter.goToCreate();
    await admin.sozialdienstMitarbeiter.fillOutNewSozialdienstMitarbeiterForm({
      nachname: E2E_TEST_SOZIALDIENST_MITARBEITER,
      vorname: 'Max',
      email: 'e2e-test-soz-benutzer@mailbucket.dvbern.ch',
    });

    await admin.sozialdienstMitarbeiter.save();
    await admin.sozialdienstMitarbeiter.goToOverview();
    await admin.sozialdienstMitarbeiter.filter(
      E2E_TEST_SOZIALDIENST_MITARBEITER,
    );
    // Erstellen check
    await expect(admin.elems.sozialdienstMitarbeiter.rows, {
      message: 'Create Sozialdienst-Mitarbeiter probably was not successfull',
    }).toHaveCount(2);

    // Step 2: Editieren ========================================================
    await admin.sozialdienstMitarbeiter.goToEdit(
      E2E_TEST_SOZIALDIENST_MITARBEITER,
    );
    await admin.elems.sozialdienstMitarbeiter.detail.vorname.fill('Maximilian');
    await admin.sozialdienstMitarbeiter.save();
    await admin.sozialdienstMitarbeiter.goToOverview();
    await admin.sozialdienstMitarbeiter.filter(
      'Maximilian ' + E2E_TEST_SOZIALDIENST_MITARBEITER,
    );
    // Editieren Check
    await expect(admin.elems.sozialdienstMitarbeiter.rows, {
      message: 'Create Sozialdienst-Mitarbeiter probably was not successfull',
    }).toHaveCount(2);

    // Step 3: Löschen ==========================================================
    await admin.sozialdienstMitarbeiter.deleteSozialdienstMitarbeiter(
      E2E_TEST_SOZIALDIENST_MITARBEITER,
    );
    await page.getByTestId('dialog-confirm').click();
    // Löschen Check
    await expect(admin.elems.sozialdienstMitarbeiter.rows, {
      message: 'Delete Sozialdienst-Mitarbeiter probably was not successfull',
    }).toHaveCount(2);
  });
});
