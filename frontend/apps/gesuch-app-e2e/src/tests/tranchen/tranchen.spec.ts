import { expect } from '@playwright/test';

import {
  SmallImageFile,
  expectStepTitleToContainText,
  getE2eUrls,
  specificMonth,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
import { setupGesuchWithApi } from '../../initialize-test-api';
import { SachbearbeiterGesuchHeaderPO } from '../../po/sachbearbeiter-gesuch-header.po';
import {
  ausbildungValues,
  gesuchFormularUpdateFn,
} from '../../test-data/tranchen-test-data';

// initialize the test with the user type and the gesuch-data to be used
const { test, getGesuchId, getTrancheId } = initializeTest(
  'GESUCHSTELLER',
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn),
);

test.describe('Tranche erstellen', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Tranche erstellen', async ({ page, cockpit: _ }) => {
    test.slow();

    const urls = getE2eUrls();

    const requiredDokumenteResponse = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload',
    );

    // Upload all GS-Dokumente =================================================
    await page.goto(
      `${urls.gs}/gesuch/dokumente/${getGesuchId()}/tranche/${getTrancheId()}`,
    );
    await expectStepTitleToContainText('Dokumente', page);
    await requiredDokumenteResponse;

    const uploads = await page
      .locator('[data-testid^="button-document-upload"]')
      .all();
    for (const upload of uploads) {
      const uploadCall = page.waitForResponse(
        (response) =>
          response.url().includes('/api/v1/gesuchDokument') &&
          response.request().method() === 'POST',
      );
      await upload.click();
      await page.getByTestId('file-input').setInputFiles(SmallImageFile);
      await page.keyboard.press('Escape');
      await expect(page.getByTestId('file-input')).toHaveCount(0);
      await uploadCall;
    }
    await page.getByTestId('button-continue').click();

    // Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);
    await page.getByTestId('button-abschluss').click();
    const freigabeResponse = page.waitForResponse(
      '**/api/v1/gesuch/*/einreichen',
    );
    await page.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    // Go to Info (SB-App) ===============================================
    await page.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    // add testid to dynamic title later
    await expect(page.getByRole('heading').nth(1)).toContainText('Tranche 1', {
      ignoreCase: true,
      timeout: 10000,
    });

    const headerNav = new SachbearbeiterGesuchHeaderPO(page);

    await headerNav.elems.trancheMenu.click();
    await expect(headerNav.elems.trancheMenuItems).toHaveCount(1);
    await page.locator('.cdk-overlay-backdrop').click();

    // set tranche to bearbeitung ===============================================
    await headerNav.elems.aktionMenu.click();
    await headerNav.elems
      .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
      .click();
    // kommentar dialog
    await page.getByTestId('dialog-confirm').click();

    await headerNav.elems.aktionMenu.click();
    await headerNav.elems
      .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
      .click();

    // tranche erstellen ========================================================
    await headerNav.elems.aktionMenu.click();
    await headerNav.elems.aktionTrancheErstellen.click();

    // Tranche erfassen dialog
    await page
      .getByTestId('form-aenderung-melden-dialog-gueltig-ab')
      .fill(`1.${specificMonth(1)}`);
    await page
      .getByTestId('form-aenderung-melden-dialog-kommentar')
      .fill('E2E Test ist Grund für Änderung');
    await page.getByTestId('dialog-confirm').click();
    await expect(page.locator('.mdc-snackbar')).toContainText(
      'Die Tranche wurde erfolgreich erstellt',
    );
    await headerNav.elems.trancheMenu.click();
    await expect(headerNav.elems.trancheMenuItems).toHaveCount(2);

    // tranche oeffnen ============================================================
    await page.getByTestId('tranche-nav-menu-item-2').click();

    await expect(page.getByRole('heading').nth(1)).toContainText('Tranche 2', {
      ignoreCase: true,
      timeout: 10000,
    });

    // end of test
  });
});
