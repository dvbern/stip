import { expect } from '@playwright/test';

import {
  expectFormToBeValid,
  expectInfoTitleToContainText,
  expectStepTitleToContainText,
  getE2eUrls,
  secondTrancheStart,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { initializeMultiUserTest } from '../../initialize-test';
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
  createZahlungsverbindungUpdateFn,
  gesuchFormularUpdateFn,
} from '../../test-data/tranchen-test-data';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);

test('Aenderung erstellen', async ({ gsPage, sbPage }) => {
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

  // SB User Actions - Switch to SB page
  await sbPage.bringToFront();
  await sbPage.goto(
    `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
  );

  const headerNav = new SachbearbeiterGesuchHeaderPO(sbPage);
  await headerNav.elems.trancheMenu.click();
  await expect(headerNav.elems.trancheMenuItems).toHaveCount(1);
  await sbPage.locator('.cdk-overlay-backdrop').click();

  // set tranche to bearbeitung ===============================================
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems
    .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
    .click();
  await sbPage.getByTestId('dialog-confirm').click();

  await headerNav.elems.aktionMenu.click();
  await headerNav.elems
    .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
    .click();

  // accept all documents =================================================
  const sbStepsNavPO = new StepsNavPO(sbPage);
  const requiredDokumenteResp = sbPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
  );
  await sbStepsNavPO.elems.dokumente.first().click();
  await expectStepTitleToContainText('Dokumente', sbPage);
  await requiredDokumenteResp;

  await expect(
    sbPage.getByTestId('dokument-akzeptieren').first(),
  ).toBeVisible();
  const acceptDocumentsButtons = await sbPage
    .getByTestId('dokument-akzeptieren')
    .count();
  expect(acceptDocumentsButtons).toBeGreaterThan(0);
  for (let i = 0; i < acceptDocumentsButtons; i++) {
    const documentsToUploadReq = sbPage.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
    );
    const dokumenteReq = sbPage.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumente/*',
    );
    await sbPage.getByTestId('dokument-akzeptieren').first().click();
    await Promise.all([documentsToUploadReq, dokumenteReq]);
  }

  // bearbeitung abschliessen ===============================================
  const abschliesenPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/*/bearbeitungAbschliessen',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems
    .getAktionStatusUebergangItem('BEARBEITUNG_ABSCHLIESSEN')
    .click();
  await abschliesenPromise;
  const verfuegtPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/status/verfuegt/*',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems.getAktionStatusUebergangItem('VERFUEGT').click();
  await verfuegtPromise;
  const versendetPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/status/versendet/*',
  );
  await headerNav.elems.aktionMenu.click();
  await headerNav.elems.getAktionStatusUebergangItem('VERSENDET').click();
  const versendetResponse = await versendetPromise;

  expect(versendetResponse.ok(), {
    message: `Versendet response failed with status ${await versendetResponse.text()}`,
  }).toBeTruthy();

  // // Go to GS App ===============================================================
  await gsPage.bringToFront();
  await gsPage.goto(`${urls.gs}/gesuch-app-feature-cockpit`);
  await gsPage.getByTestId('cockpit-gesuch-aenderung-create').click();

  await gsPage
    .getByTestId('form-aenderung-melden-dialog-gueltig-ab')
    .fill(secondTrancheStart());
  await gsPage
    .getByTestId('form-aenderung-melden-dialog-kommentar')
    .fill('E2E Testkommentar');

  await gsPage.getByTestId('dialog-confirm').click();

  await expectStepTitleToContainText('Person in Ausbildung', gsPage);

  // make a change in the form
  const gsPersonPO = new PersonPO(gsPage);
  await expect(gsPersonPO.elems.loading).toBeHidden();
  await gsPersonPO.elems.nachname.fill('E2E-Changed');
  await expectFormToBeValid(gsPersonPO.elems.form);
  await gsPersonPO.elems.buttonSaveContinue.click();

  // verify the change
  // navigate back, because of save and continue
  const gsStepsNavPO = new StepsNavPO(gsPage);
  await gsStepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', gsPage);
  await expect(gsPersonPO.elems.nachname).toHaveValue('E2E-Changed');
  await expect(
    gsPage.getByTestId('form-person-nachname-zuvor-hint'),
  ).toHaveText('Sanchez');

  // check changes on stepNav
  await expect(
    gsStepsNavPO.elems.person.first().locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

  // submit the change
  await gsStepsNavPO.elems.abschluss.first().click();
  await expectStepTitleToContainText('Freigabe', gsPage);
  const freigapePO = new FreigabePO(gsPage);

  await freigapePO.elems.buttonAbschluss.click();
  const freigabeAenderungResponse = gsPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/aenderung/einreichen',
  );
  await gsPage.getByTestId('dialog-confirm').click();
  const response = await freigabeAenderungResponse;
  expect(response.status()).toBe(204);

  // Go to Aenderung on SB App ==========================================================
  await sbPage.bringToFront();
  await sbPage.goto(
    `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
  );
  await headerNav.elems.aenderungenMenu.click();
  await expect(headerNav.elems.aenderungenMenuItems).toHaveCount(2);
  await headerNav.elems.aenderungenMenuItems.first().click();
  await expectInfoTitleToContainText('Änderung 1', sbPage);

  // change the nachname again on SB App
  await sbStepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', sbPage);
  const sbPersonPO = new PersonPO(sbPage);
  await sbPersonPO.elems.nachname.fill('E2E-Changed-2');

  const personSaveResponse = sbPage.waitForResponse(
    (r) =>
      r.url().includes('/api/v1/gesuch') && r.request().method() === 'PATCH',
  );
  await sbPersonPO.elems.buttonSaveContinue.click();
  await personSaveResponse;

  // verify the change
  await sbStepsNavPO.elems.person.first().click();
  await expectStepTitleToContainText('Person in Ausbildung', sbPage);
  await expect(sbPersonPO.elems.nachname).toHaveValue('E2E-Changed-2');
  await expect(
    sbPage.getByTestId('form-person-nachname-zuvor-hint'),
  ).toHaveText('E2E-Changed');

  await sbStepsNavPO.elems.geschwister.first().click();
  await expectStepTitleToContainText('Geschwister', sbPage);
  const geschwisterPO = new GeschwisterPO(sbPage);
  await expect(geschwisterPO.elems.loading).toBeHidden();
  await geschwisterPO.addGeschwister(bruder);
  await expect(geschwisterPO.elems.geschwisterRow).toHaveCount(1);
  await geschwisterPO.elems.buttonContinue.click();

  // verify step nav indicators
  await expect(
    sbStepsNavPO.elems.geschwister
      .first()
      .locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

  // Accept the Aenderung ==========================================================
  await sbStepsNavPO.elems.info.first().click();
  await expectInfoTitleToContainText('Änderung 1', sbPage);
  const trancheInfoPO = new TrancheInfoPO(sbPage);
  const aenderungAcceptResponse = sbPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/aenderung/akzeptieren',
  );
  await trancheInfoPO.elems.aenderungAccept.click();
  const acceptResponse = await aenderungAcceptResponse;
  expect(acceptResponse.status()).toBe(200);

  // assert that a second tranche was created
  await headerNav.elems.trancheMenu.click();
  await expect(headerNav.elems.trancheMenuItems).toHaveCount(2);

  sbPage.close();
  gsPage.close();
});
