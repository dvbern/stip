import type { Page } from '@playwright/test';
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
  generateRandomName,
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

test.describe.configure({ mode: 'serial' });
test.slow();
const urls = getE2eUrls();

test.describe('Aenderung', () => {
  let gsPage1: Page;
  let sbPage1: Page;
  let sbPage2: Page;
  let originalNachname: string;
  let newNachname: string;

  test.beforeAll(async ({ gsPage, createSbPage }) => {
    gsPage1 = gsPage;
    sbPage1 = await createSbPage();
    sbPage2 = await createSbPage(1);
  });

  test('GS: Gesuch einreichen', async () => {
    await gsPage1.bringToFront();

    await test.step('Upload all GS-Dokumente', async () => {
      const requiredDokumenteResponse = gsPage1.waitForResponse(
        '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
      );
      await gsPage1.goto(
        `${urls.gs}/gesuch/dokumente/${getGesuchId()}/tranche/${getTrancheId()}`,
      );
      await expectStepTitleToContainText('Dokumente', gsPage1);
      await requiredDokumenteResponse;
      await uploadFiles(gsPage1);
      await gsPage1.getByTestId('step-nav-abschluss').first().click();
    });

    await test.step('Freigabe', async () => {
      await expectStepTitleToContainText('Freigabe', gsPage1);
      await gsPage1.getByTestId('button-abschluss').click();
      const freigabeResponse = gsPage1.waitForResponse(
        '**/api/v1/gesuch/*/einreichen/gs',
      );
      await gsPage1.getByTestId('dialog-confirm').click();
      await freigabeResponse;
    });
  });

  test('SB: Dokumente Akzeptieren', async () => {
    const sbGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage1);
    const sbStepsNavPO = new StepsNavPO(sbPage1);

    await sbPage1.bringToFront();
    await test.step('Go to Info (SB-App)', async () => {
      await sbPage1.goto(
        `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
      );
    });

    await test.step('Set tranche to bearbeitung', async () => {
      await sbGesuchHeader.elems.aktionMenu.click();
      await sbGesuchHeader.elems
        .getAktionStatusUebergangItem('SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT')
        .click();

      const kommentarField = sbPage1.getByTestId(
        'form-kommentar-dialog-kommentar',
      );
      await kommentarField.fill('E2E Antrag genemigen kommentar');
      await sbPage1.getByTestId('dialog-confirm').click();

      await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

      await sbGesuchHeader.elems.aktionMenu.click();
      await sbGesuchHeader.elems
        .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
        .click();
    });

    await test.step('Accept all documents', async () => {
      const requiredDokumenteResp = sbPage1.waitForResponse(
        '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
      );
      await sbStepsNavPO.elems.dokumente.first().click();
      await expectStepTitleToContainText('Dokumente', sbPage1);
      await requiredDokumenteResp;

      await acceptDocuments(sbPage1);
    });

    await test.step('Bearbeitung abschliessen', async () => {
      const abschliesenPromise = sbPage1.waitForResponse(
        '**/api/v1/gesuch/*/bearbeitungAbschliessen',
      );
      await sbGesuchHeader.elems.aktionMenu.click();
      await sbGesuchHeader.elems
        .getAktionStatusUebergangItem('BEARBEITUNG_ABSCHLIESSEN')
        .click();
      await abschliesenPromise;
    });
  });

  test('SB2: Verfuegen und Versenden', async () => {
    const verfuegtGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage2);

    await test.step('Open gesuch in SB2', async () => {
      await sbPage2.bringToFront();
      await sbPage2.goto(
        `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
      );
    });

    await test.step('Set status to VERFUEGT', async () => {
      const verfuegtPromise = sbPage2.waitForResponse(
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

      await verfuegtGesuchHeader.elems.actionLoading.waitFor({
        state: 'hidden',
      });
    });

    await test.step('Set status to VERSENDET', async () => {
      const versendetPromise = sbPage2.waitForResponse(
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
    });

    await sbPage2.close();
  });

  test('GS: Aenderung melden und freigeben', async () => {
    const gsStepsNavPO = new StepsNavPO(gsPage1);
    const gsPersonPO = new PersonPO(gsPage1);

    await test.step('Melde Aenderung im GS Dashboard', async () => {
      await gsPage1.bringToFront();
      await gsPage1.goto(`${urls.gs}/dashboard`);
      await gsPage1.getByTestId('cockpit-gesuch-aenderung-create').click();

      await gsPage1
        .getByTestId('form-aenderung-melden-dialog-gueltig-ab')
        .fill(secondTrancheStart());
      await gsPage1
        .getByTestId('form-aenderung-melden-dialog-kommentar')
        .fill('E2E Testkommentar');

      await gsPage1.getByTestId('dialog-confirm').click();
      await expectStepTitleToContainText('Person in Ausbildung', gsPage1);
    });

    await test.step('Aendere Nachname in Person in Ausbildung', async () => {
      originalNachname = await gsPersonPO.elems.nachname.inputValue();
      newNachname = generateRandomName();
      await gsPersonPO.elems.nachname.fill(newNachname);
      await expectFormToBeValid(gsPersonPO.elems.form);

      const personGsSaveResponse = gsPage1.waitForResponse(
        (r) =>
          r.url().includes('/api/v1/gesuch') &&
          r.request().method() === 'PATCH',
      );
      await gsPersonPO.elems.buttonSaveContinue.click();
      await gsPersonPO.elems.loading.waitFor({ state: 'hidden' });
      await personGsSaveResponse;

      const lebenslaufPO = new LebenslaufPO(gsPage1);
      await lebenslaufPO.elems.loading.waitFor({ state: 'hidden' });
    });

    await test.step('Verifiziere Aenderung und Indicator im StepNav', async () => {
      const personStepNav = gsStepsNavPO.elems.person.first();
      await personStepNav.scrollIntoViewIfNeeded();
      await personStepNav.click();
      await gsPersonPO.elems.loading.waitFor({ state: 'hidden' });
      await expectStepTitleToContainText('Person in Ausbildung', gsPage1);
      await expect(gsPersonPO.elems.nachname).toHaveValue(newNachname);
      await expect(
        gsPage1.getByTestId('form-person-nachname-zuvor-hint'),
      ).toHaveText(originalNachname);

      await expect(
        personStepNav.locator('dv-shared-ui-change-indicator'),
      ).toBeVisible();
    });

    await test.step('Reiche Aenderung ein', async () => {
      await gsStepsNavPO.elems.abschluss.first().click();
      await expectStepTitleToContainText('Freigabe', gsPage1);
      const freigapePO = new FreigabePO(gsPage1);

      await freigapePO.elems.buttonAbschluss.click();
      const freigabeAenderungResponse = gsPage1.waitForResponse(
        '**/api/v1/gesuchtranche/*/aenderung/einreichen',
      );
      await gsPage1.getByTestId('dialog-confirm').click();
      const response = await freigabeAenderungResponse;
      expect(response.status()).toBe(204);
    });

    await gsPage1.close();
  });

  test('SB: Aenderung akzeptieren', async () => {
    const sbTrancheInfoPO = new TrancheInfoPO(sbPage1);
    const sbGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage1);
    const sbStepsNavPO = new StepsNavPO(sbPage1);

    await test.step('Open Aenderung in SB App', async () => {
      await sbPage1.bringToFront();

      const gesuchHeaderResponse = sbPage1.waitForResponse(
        '**/api/v1/gesuch/sb/header/*',
      );

      await sbPage1.goto(
        `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
      );

      await gesuchHeaderResponse;
      await sbTrancheInfoPO.elems.loading.waitFor({ state: 'hidden' });

      await sbGesuchHeader.elems.aenderungenLink.click();
      await sbTrancheInfoPO.elems.loading.waitFor({ state: 'hidden' });

      await sbGesuchHeader.elems.aenderungenMenu.click();
      // todo-e2e-next: make more specific by aenderung status reject / accept / manually change?
      await expect(sbGesuchHeader.elems.aenderungenMenuItems).toHaveCount(1);
      await sbPage1.locator('.cdk-overlay-backdrop').click();
      await expectInfoTitleToContainText('Änderung vom', sbPage1);
    });

    await test.step('Verify changed person data', async () => {
      await sbStepsNavPO.elems.expanderPersoenlichGroup.first().click();
      await sbStepsNavPO.elems.person.first().click();
      const sbPersonPO = new PersonPO(sbPage1);
      await sbPersonPO.elems.loading.waitFor({ state: 'hidden' });
      await expectStepTitleToContainText('Person in Ausbildung', sbPage1);
      await expect(sbPersonPO.elems.nachname).toHaveValue(newNachname);
      await expect(
        sbPage1.getByTestId('form-person-nachname-zuvor-hint'),
      ).toHaveText(originalNachname);
    });

    await test.step('Add Geschwister and verify step indicator', async () => {
      await sbStepsNavPO.elems.expanderFamilienGroup.first().click();
      await sbStepsNavPO.elems.geschwister.first().click();
      const geschwisterPO = new GeschwisterPO(sbPage1);
      await geschwisterPO.elems.loading.waitFor({ state: 'hidden' });
      await expectStepTitleToContainText('Geschwister', sbPage1);

      await geschwisterPO.addGeschwister(bruder);
      await geschwisterPO.elems.loading.waitFor({ state: 'hidden' });
      await expect(geschwisterPO.elems.geschwisterRow).toHaveCount(1);
      await geschwisterPO.elems.buttonContinue.click();

      // todo-e2e-next: more generic approach for spinners hidden (form and action menu)
      const sbLoading = await sbPage1
        .getByRole('status', { name: 'Loading' })
        .all();
      for (const loading of sbLoading) {
        await loading.waitFor({ state: 'hidden' });
      }

      await expect(
        sbStepsNavPO.elems.geschwister
          .first()
          .locator('dv-shared-ui-change-indicator'),
      ).toBeVisible();
    });

    await test.step('Accept Aenderung and verify second tranche', async () => {
      await sbStepsNavPO.elems.info.first().click();
      await expectInfoTitleToContainText('Änderung vom', sbPage1);

      const aenderungAcceptResponse = sbPage1.waitForResponse(
        '**/api/v1/gesuchtranche/*/aenderung/akzeptieren',
      );
      await sbGesuchHeader.elems.aktionMenu.click();
      await sbGesuchHeader.elems.aenderungAccept.click();
      const acceptResponse = await aenderungAcceptResponse;
      expect(acceptResponse.status()).toBe(200);

      await sbGesuchHeader.elems.trancheMenu.click();

      // todo-e2e-next: more specific test to verify tranche was created correctly?
      await expect(sbGesuchHeader.elems.trancheMenuItems).toHaveCount(2);
    });

    await sbPage1.close();
  });
});
