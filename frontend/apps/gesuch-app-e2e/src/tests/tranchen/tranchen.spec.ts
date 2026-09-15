import { expect } from '@playwright/test';

import {
  SachbearbeiterGesuchHeaderPO,
  expectStepTitleToContainText,
  getE2eUrls,
  initializeMultiUserTest,
  secondTrancheStart,
  setupGesuchWithApi,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

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
  test('Tranche erstellen', async ({ gsPage, createSbPage }) => {
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
    await gsPage.getByTestId('step-nav-abschluss').first().click();

    // Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', gsPage);
    await gsPage.getByTestId('button-abschluss').click();
    const freigabeResponse = gsPage.waitForResponse(
      '**/api/v1/gesuch/*/einreichen/gs',
    );
    await gsPage.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    // Go to Info (SB-App) ===============================================
    const sbPage = await createSbPage();
    await sbPage.bringToFront();
    await sbPage.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    // set tranche to bearbeitung ===============================================
    const sbGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage);
    await sbGesuchHeader.elems.aktionMenu.click();
    await sbGesuchHeader.elems
      .getAktionStatusUebergangItem('SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT')
      .click();

    const kommentarField = sbPage.getByTestId(
      'form-kommentar-dialog-kommentar',
    );
    await kommentarField.fill('E2E Antrag genemigen kommentar');
    await sbPage.getByTestId('dialog-confirm').click();

    await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

    await sbGesuchHeader.elems.aktionMenu.click();
    await sbGesuchHeader.elems
      .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
      .click();

    await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

    // tranche erstellen ========================================================
    await sbGesuchHeader.elems.aktionMenu.click();
    await sbGesuchHeader.elems.aktionTrancheErstellen.click();

    // Tranche erfassen dialog
    await sbPage
      .getByTestId('form-aenderung-melden-dialog-gueltig-ab')
      .fill(secondTrancheStart());
    await sbPage
      .getByTestId('form-aenderung-melden-dialog-kommentar')
      .fill('E2E Test ist Grund für Änderung');
    await sbPage.getByTestId('dialog-confirm').click();
    await expect(sbPage.locator('.mdc-snackbar').first()).toContainText(
      'Die Tranche wurde erfolgreich erstellt',
    );
    await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });
    await sbGesuchHeader.elems.trancheMenu.click();

    // todo-e2e-next: more specific assertion for tranche created
    await expect(sbGesuchHeader.elems.trancheMenuItems).toHaveCount(2);

    sbPage.close();
    gsPage.close();
  });
});
