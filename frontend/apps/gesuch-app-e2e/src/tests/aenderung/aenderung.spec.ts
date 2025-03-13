import { expect } from '@playwright/test';

import {
  SmallImageFile,
  expectStepTitleToContainText,
  getE2eUrls,
  today,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
import { setupGesuchWithApi } from '../../initialize-test-api';
import { FreigabePO } from '../../po/freigabe.po';
import { PersonPO } from '../../po/person.po';
import { SachbearbeiterGesuchHeaderPO } from '../../po/sachbearbeiter-gesuch-header.po';
import { StepsNavPO } from '../../po/steps-nav.po';
import {
  ausbildungValues,
  gesuchFormularUpdateFn,
} from '../../test-data/tranchen-test-data';

const { test, getGesuchId, getTrancheId } = initializeTest(
  'GESUCHSTELLER',
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn),
);

test('Aenderung erstellen', async ({ page, cockpit }) => {
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

  // accept all documents =================================================
  const stepsNavPO = new StepsNavPO(page);

  const requiredDokumenteResp = page.waitForResponse(
    '**/api/v1/gesuchtranche/*/dokumenteToUpload',
  );

  await stepsNavPO.elems.dokumente.first().click();

  await expectStepTitleToContainText('Dokumente', page);
  await requiredDokumenteResp;

  const acceptDocumentsButtons = await page
    .locator('[data-testid="dokument-akzeptieren"]')
    .count();

  for (let i = 0; i < acceptDocumentsButtons; i++) {
    const acceptCall = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload',
    );
    await page.getByTestId('dokument-akzeptieren').first().click();
    await acceptCall;
    await page.waitForTimeout(200);
  }

  // bearbeitung abschliessen ===============================================
  const abschliesenPromise = page.waitForResponse(
    '**/api/v1/gesuch/*/bearbeitungAbschliessen',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems
    .getAktionStatusUebergangItem('BEARBEITUNG_ABSCHLIESSEN')
    .click();
  await abschliesenPromise;

  const verfuegtPromise = page.waitForResponse(
    '**/api/v1/gesuch/status/verfuegt/*',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems.getAktionStatusUebergangItem('VERFUEGT').click();
  await verfuegtPromise;

  const versendetPromise = page.waitForResponse(
    '**/api/v1/gesuch/status/versendet/*',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems.getAktionStatusUebergangItem('VERSENDET').click();
  await versendetPromise;

  // // Go to GS App ===============================================================

  await page.goto(`${urls.gs}/gesuch-app-feature-cockpit`);
  await cockpit.elems.cereateAaederung.click();

  await page.getByTestId('form-aenderung-melden-dialog-gueltig-ab').fill(
    // today
    today(),
  );
  await page
    .getByTestId('form-aenderung-melden-dialog-kommentar')
    .fill('E2E Testkommentar');

  await page.getByTestId('dialog-confirm').click();

  await expectStepTitleToContainText('Person in Ausbildung', page);

  // make a change in the form
  const personPO = new PersonPO(page);
  await personPO.elems.nachname.fill('E2E-Changed');
  await personPO.elems.buttonSaveContinue.click();

  // verify the change
  await stepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', page);
  await expect(personPO.elems.nachname).toHaveValue('E2E-Changed');
  await expect(page.locator('.mat-mdc-form-field-hint').first()).toHaveText(
    'Sanchez',
  );

  // check changes on stepNav
  await expect(
    stepsNavPO.elems.person.first().locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

  // could add more complex changes here, vater or mutter etc.

  // submit the change
  await stepsNavPO.elems.abschluss.first().click();
  await expectStepTitleToContainText('Freigabe', page);
  const freigapePO = new FreigabePO(page);

  await freigapePO.elems.buttonAbschluss.click();
  const freigabeAenderungResponse = page.waitForResponse(
    '**/api/v1/gesuchtranche/*/aenderung/einreichen',
  );
  await page.getByTestId('dialog-confirm').click();
  const response = await freigabeAenderungResponse;
  expect(response.status()).toBe(204);

  // could add switch to SB app and check and accept the change
});
