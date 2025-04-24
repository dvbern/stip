import { expect } from '@playwright/test';

import {
  expectInfoTitleToContainText,
  expectStepTitleToContainText,
  getE2eUrls,
  today,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
import { setupGesuchWithApi } from '../../initialize-test-api';
import { FreigabePO } from '../../po/freigabe.po';
import { GeschwisterPO } from '../../po/geschwister.po';
import { PersonPO } from '../../po/person.po';
import { SachbearbeiterGesuchHeaderPO } from '../../po/sachbearbeiter-gesuch-header.po';
import { StepsNavPO } from '../../po/steps-nav.po';
import { TrancheInfoPO } from '../../po/tranche-info.po';
import { bruder } from '../../test-data/aenderung-test-data';
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
    '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
  );

  // Upload all GS-Dokumente =================================================
  await page.goto(
    `${urls.gs}/gesuch/dokumente/${getGesuchId()}/tranche/${getTrancheId()}`,
  );
  await expectStepTitleToContainText('Dokumente', page);
  await requiredDokumenteResponse;
  await uploadFiles(page);
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
  await page.getByTestId('dialog-confirm').click();

  await headerNav.elems.aktionMenu.click();
  await headerNav.elems
    .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
    .click();

  // accept all documents =================================================
  const stepsNavPO = new StepsNavPO(page);
  const requiredDokumenteResp = page.waitForResponse(
    '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
  );
  await stepsNavPO.elems.dokumente.first().click();
  await expectStepTitleToContainText('Dokumente', page);
  await requiredDokumenteResp;

  await expect(page.getByTestId('dokument-akzeptieren').first()).toBeVisible();
  const acceptDocumentsButtons = await page
    .getByTestId('dokument-akzeptieren')
    .count();
  expect(acceptDocumentsButtons).toBeGreaterThan(0);
  for (let i = 0; i < acceptDocumentsButtons; i++) {
    const documentsToUploadReq = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
    );
    const dokumenteReq = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumente/*',
    );
    await page.getByTestId('dokument-akzeptieren').first().click();
    await Promise.all([documentsToUploadReq, dokumenteReq]);
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
  await expect(page.getByTestId('form-person-nachname-zuvor-hint')).toHaveText(
    'Sanchez',
  );

  // check changes on stepNav
  await expect(
    stepsNavPO.elems.person.first().locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

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

  // Go to Aenderung on SB App ==========================================================
  await page.goto(
    `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
  );
  await headerNav.elems.aenderungenMenu.click();
  await expect(headerNav.elems.aenderungenMenuItems).toHaveCount(2);
  await headerNav.elems.aenderungenMenuItems.first().click();
  await expectInfoTitleToContainText('Änderung 1', page);

  // change the nachname again on SB App
  await stepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', page);
  await personPO.elems.nachname.fill('E2E-Changed-2');

  const personSaveResponse = page.waitForResponse(
    (r) =>
      r.url().includes('/api/v1/gesuch') && r.request().method() === 'PATCH',
  );
  await personPO.elems.buttonSaveContinue.click();
  await personSaveResponse;

  // verify the change
  await stepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', page);
  await expect(personPO.elems.nachname).toHaveValue('E2E-Changed-2');
  await expect(page.getByTestId('form-person-nachname-zuvor-hint')).toHaveText(
    'E2E-Changed',
  );

  await stepsNavPO.elems.geschwister.first().click();
  await expectStepTitleToContainText('Geschwister', page);
  const geschwisterPO = new GeschwisterPO(page);
  await expect(geschwisterPO.elems.loading).toBeHidden();
  await geschwisterPO.addGeschwister(bruder);
  await expect(geschwisterPO.elems.geschwisterRow).toHaveCount(1);
  await geschwisterPO.elems.buttonContinue.click();

  // verify step nav indicators
  await expect(
    stepsNavPO.elems.geschwister
      .first()
      .locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

  // Accept the Aenderung ==========================================================
  await stepsNavPO.elems.info.first().click();
  await expectInfoTitleToContainText('Änderung 1', page);
  const trancheInfoPO = new TrancheInfoPO(page);
  const aenderungAcceptResponse = page.waitForResponse(
    '**/api/v1/gesuchtranche/*/aenderung/akzeptieren',
  );
  await trancheInfoPO.elems.aenderungAccept.click();
  const acceptResponse = await aenderungAcceptResponse;
  expect(acceptResponse.status()).toBe(200);

  // assert that a second tranche was created
  await headerNav.elems.trancheMenu.click();
  await expect(headerNav.elems.trancheMenuItems).toHaveCount(2);
});
