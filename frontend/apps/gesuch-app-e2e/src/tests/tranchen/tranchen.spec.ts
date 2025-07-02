import { expect } from '@playwright/test';

import {
  expectInfoTitleToContainText,
  expectStepTitleToContainText,
  getE2eUrls,
  secondTrancheStart,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { initializeMultiUserTest } from '../../initialize-test';
import { setupGesuchWithApi } from '../../initialize-test-api';
import { SachbearbeiterGesuchHeaderPO } from '../../po/sachbearbeiter-gesuch-header.po';
import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  gesuchFormularUpdateFn,
} from '../../test-data/tranchen-test-data';

// initialize the test with the user type and the gesuch-data to be used
const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);

test.describe('Tranche erstellen', () => {
  test('Tranche erstellen', async ({ gsPage, sbPage }) => {
    test.slow();

    const urls = getE2eUrls();

    const requiredDokumenteResponse = gsPage.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
    );

    // Upload all GS-Dokumente =================================================
    await gsPage.bringToFront();
    await gsPage.goto(
      `${urls.gs}/gesuch/dokumente/${getGesuchId()}/tranche/${getTrancheId()}`,
    );
    await expectStepTitleToContainText('Dokumente', gsPage);
    await requiredDokumenteResponse;

    await uploadFiles(gsPage);
    await gsPage.getByTestId('button-continue').click();

    // Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', gsPage);
    await gsPage.getByTestId('button-abschluss').click();
    const freigabeResponse = gsPage.waitForResponse(
      '**/api/v1/gesuch/*/einreichen/gs',
    );
    await gsPage.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    // Go to Info (SB-App) ===============================================
    await sbPage.bringToFront();
    await sbPage.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );
    await expectInfoTitleToContainText('Tranche 1', sbPage);

    const headerNav = new SachbearbeiterGesuchHeaderPO(sbPage);

    await headerNav.elems.trancheMenu.click();
    await expect(headerNav.elems.trancheMenuItems).toHaveCount(1);
    await sbPage.locator('.cdk-overlay-backdrop').click();

    // set tranche to bearbeitung ===============================================
    await headerNav.elems.aktionMenu.click();
    await headerNav.elems
      .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
      .click();
    // kommentar dialog
    await sbPage.getByTestId('dialog-confirm').click();

    await headerNav.elems.aktionMenu.click();
    await headerNav.elems
      .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
      .click();

    // tranche erstellen ========================================================
    await headerNav.elems.aktionMenu.click();
    await headerNav.elems.aktionTrancheErstellen.click();

    // Tranche erfassen dialog
    await sbPage
      .getByTestId('form-aenderung-melden-dialog-gueltig-ab')
      .fill(secondTrancheStart());
    await sbPage
      .getByTestId('form-aenderung-melden-dialog-kommentar')
      .fill('E2E Test ist Grund für Änderung');
    await sbPage.getByTestId('dialog-confirm').click();
    await expect(sbPage.locator('.mdc-snackbar')).toContainText(
      'Die Tranche wurde erfolgreich erstellt',
    );
    await headerNav.elems.trancheMenu.click();
    await expect(headerNav.elems.trancheMenuItems).toHaveCount(2);

    // tranche oeffnen ============================================================
    await sbPage.getByTestId('tranche-nav-menu-item').nth(1).click();
    await expectInfoTitleToContainText('Tranche 2', sbPage);

    sbPage.close();
    gsPage.close();

    // end of test
  });
});
