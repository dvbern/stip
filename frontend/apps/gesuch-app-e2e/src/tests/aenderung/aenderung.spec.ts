import { expect } from '@playwright/test';

import {
  FreigabePO,
  GeschwisterPO,
  LebenslaufPO,
  PersonPO,
  SachbearbeiterGesuchHeaderPO,
  StepsNavPO,
  TrancheInfoPO,
  acceptDocuments,
  expectFormToBeValid,
  expectInfoTitleToContainText,
  expectStepTitleToContainText,
  getE2eUrls,
  initializeMultiUserTest,
  secondTrancheStart,
  setupGesuchWithApi,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

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

test('Aenderung erstellen', async ({ gsPage, createSbPage }) => {
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

  const kommentarField = sbPage.getByTestId('form-kommentar-dialog-kommentar');
  await kommentarField.fill('E2E Antrag genemigen kommentar');
  await sbPage.getByTestId('dialog-confirm').click();

  await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems
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

  await acceptDocuments(sbPage);

  // bearbeitung abschliessen ===============================================
  const abschliesenPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/*/bearbeitungAbschliessen',
  );
  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems
    .getAktionStatusUebergangItem('BEARBEITUNG_ABSCHLIESSEN')
    .click();
  await abschliesenPromise;

  // Verfuegen und Versenden durch Sb 2 =============================================================
  const sbVerfuegtPage = await createSbPage(1);
  await sbVerfuegtPage.bringToFront();
  await sbVerfuegtPage.goto(
    `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
  );
  const verfuegtGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbVerfuegtPage);
  const verfuegtPromise = sbVerfuegtPage.waitForResponse(
    '**/api/v1/gesuch/status/verfuegt/*',
  );
  await verfuegtGesuchHeader.elems.aktionMenu.click();
  await verfuegtGesuchHeader.elems
    .getAktionStatusUebergangItem('VERFUEGT')
    .click();
  const verfuegtResponse = await verfuegtPromise;

  if (!verfuegtResponse.ok()) {
    const text = await verfuegtResponse.text();
    throw new Error(`Verfuegt response failed with status ${text}`);
  }

  await verfuegtGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

  const versendetPromise = sbVerfuegtPage.waitForResponse(
    '**/api/v1/gesuch/status/versendet/*',
  );
  await verfuegtGesuchHeader.elems.aktionMenu.click();
  await verfuegtGesuchHeader.elems
    .getAktionStatusUebergangItem('VERSENDET')
    .click();
  const versendetResponse = await versendetPromise;

  if (!versendetResponse.ok()) {
    const text = await versendetResponse.text();
    throw new Error(`Versendet response failed with status ${text}`);
  }

  // // Go to GS App ===============================================================
  await gsPage.bringToFront();
  await gsPage.goto(`${urls.gs}/dashboard`);
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
  await gsPersonPO.elems.nachname.fill('E2E-Changed');
  await expectFormToBeValid(gsPersonPO.elems.form);
  const personGsSaveResponse = gsPage.waitForResponse(
    (r) =>
      r.url().includes('/api/v1/gesuch') && r.request().method() === 'PATCH',
  );
  await gsPersonPO.elems.buttonSaveContinue.click();
  await gsPersonPO.elems.loading.waitFor({ state: 'hidden' });
  await personGsSaveResponse;

  const lebenslaufPO = new LebenslaufPO(gsPage);
  await lebenslaufPO.elems.loading.waitFor({ state: 'hidden' });

  // verify the change
  // navigate back, because of save and continue
  const gsStepsNavPO = new StepsNavPO(gsPage);
  const personStepNav = gsStepsNavPO.elems.person.first();
  await personStepNav.scrollIntoViewIfNeeded();
  await personStepNav.click();
  await gsPersonPO.elems.loading.waitFor({ state: 'hidden' });
  await expectStepTitleToContainText('Person in Ausbildung', gsPage);
  await expect(gsPersonPO.elems.nachname).toHaveValue('E2E-Changed');
  await expect(
    gsPage.getByTestId('form-person-nachname-zuvor-hint'),
  ).toHaveText('Sanchez');

  // check changes on stepNav
  await expect(
    personStepNav.locator('dv-shared-ui-change-indicator'),
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

  const gesuchHeaderResponse = sbPage.waitForResponse(
    '**/api/v1/gesuch/sb/header/*',
  );

  await sbPage.goto(
    `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
  );

  await gesuchHeaderResponse;

  const sbTrancheInfoPO = new TrancheInfoPO(sbPage);

  await sbTrancheInfoPO.elems.loading.waitFor({ state: 'hidden' });

  await sbGesuchHeader.elems.aenderungenLink.click();

  await sbTrancheInfoPO.elems.loading.waitFor({ state: 'hidden' });

  await sbGesuchHeader.elems.aenderungenMenu.click();
  // todo-e2e-next: make more specific by aenderung status reject / accept / manually change?
  await expect(sbGesuchHeader.elems.aenderungenMenuItems).toHaveCount(1);
  await sbPage.locator('.cdk-overlay-backdrop').click();
  await expectInfoTitleToContainText('Änderung vom', sbPage);

  // change the nachname again on SB App
  await sbStepsNavPO.elems.expanderPersoenlichGroup.first().click();
  await sbStepsNavPO.elems.person.first().click();

  await expectStepTitleToContainText('Person in Ausbildung', sbPage);
  const sbPersonPO = new PersonPO(sbPage);
  await sbPersonPO.elems.loading.waitFor({ state: 'hidden' });
  await sbPersonPO.elems.nachname.fill('E2E-Changed-2');

  // todo-e2e-next: could all these waitForResponse calls be replaced with awaiting loading?
  const personSaveResponse = sbPage.waitForResponse(
    (r) =>
      r.url().includes('/api/v1/gesuch') && r.request().method() === 'PATCH',
  );
  await sbPersonPO.elems.buttonSaveContinue.click();
  await personSaveResponse;

  // verify the change
  await sbStepsNavPO.elems.person.first().click();
  await sbPersonPO.elems.loading.waitFor({ state: 'hidden' });
  await expectStepTitleToContainText('Person in Ausbildung', sbPage);
  await expect(sbPersonPO.elems.nachname).toHaveValue('E2E-Changed-2');
  await expect(
    sbPage.getByTestId('form-person-nachname-zuvor-hint'),
  ).toHaveText('E2E-Changed');

  await sbStepsNavPO.elems.expanderFamilienGroup.first().click();
  await sbStepsNavPO.elems.geschwister.first().click();
  const geschwisterPO = new GeschwisterPO(sbPage);
  await geschwisterPO.elems.loading.waitFor({ state: 'hidden' });
  await expectStepTitleToContainText('Geschwister', sbPage);

  await geschwisterPO.addGeschwister(bruder);
  await geschwisterPO.elems.loading.waitFor({ state: 'hidden' });
  await expect(geschwisterPO.elems.geschwisterRow).toHaveCount(1);
  await geschwisterPO.elems.buttonContinue.click();

  // todo-e2e-next: more generic approach for spinners hidden (form and action menu)
  const sbLoading = await sbPage.getByRole('status', { name: 'Loading' }).all();
  for (const loading of sbLoading) {
    await loading.waitFor({ state: 'hidden' });
  }

  // verify step nav indicators
  await expect(
    sbStepsNavPO.elems.geschwister
      .first()
      .locator('dv-shared-ui-change-indicator'),
  ).toBeVisible();

  // Accept the Aenderung ==========================================================
  await sbStepsNavPO.elems.info.first().click();
  await expectInfoTitleToContainText('Änderung vom', sbPage);

  const aenderungAcceptResponse = sbPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/aenderung/akzeptieren',
  );
  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems.aenderungAccept.click();
  const acceptResponse = await aenderungAcceptResponse;
  expect(acceptResponse.status()).toBe(200);

  // assert that a second tranche was created
  await sbGesuchHeader.elems.trancheMenu.click();

  // todo-e2e-next: more specific test to verify tranche was created correctly?
  await expect(sbGesuchHeader.elems.trancheMenuItems).toHaveCount(2);

  sbVerfuegtPage.close();
  sbPage.close();
  gsPage.close();
});
