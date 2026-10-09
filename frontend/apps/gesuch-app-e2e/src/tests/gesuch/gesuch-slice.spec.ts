import { expect } from '@playwright/test';

import {
  AuszahlungPO,
  EinnahmenKostenPO,
  ElternPO,
  FamilyPO,
  GeschwisterPO,
  GesuchProtokollPO,
  GesuchsTabNavPO,
  KinderPO,
  LebenslaufPO,
  PersonPO,
  SachbearbeiterGesuchHeaderPO,
  StepsNavPO,
  SteruererklaerungPO,
  SteuerdatenPO,
  expectStepTitleToContainText,
  getE2eUrls,
  initializeMultiUserTest,
  uploadFilesById,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildung,
  bruder,
  einnahmenKosten,
  einnhamenKostenSb,
  familienlsituation,
  mutter,
  person,
  steuerdaten,
  steuererklaerung,
  taetigkeit,
  zahlungsverbindungFn,
} from './gesuch-slice.data';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(ausbildung);
const urls = getE2eUrls();
test.describe.configure({ mode: 'serial' });
test.slow();

test.describe('Neues gesuch erstellen', () => {
  let seed: string;

  test('GS: Gesuch erstellen und einreichen', async ({ gsPage }, testInfo) => {
    seed = `${testInfo.title}-${testInfo.workerIndex}`;

    await gsPage.bringToFront();

    await test.step('Step 1: Person', async () => {
      await expect(gsPage.getByTestId('step-title').first()).toBeAttached({
        timeout: 10000,
      });

      await gsPage.locator('.mat-expansion-panel-header').first().click();

      await gsPage.getByTestId('step-nav-person').first().click();
      await expectStepTitleToContainText('Person in Ausbildung', gsPage);
      const personPO = new PersonPO(gsPage);
      await expect(personPO.elems.loading).toBeHidden();

      await personPO.fillPersonForm(person(seed));
      await personPO.elems.buttonSaveContinue.click();
    });

    await test.step('Step 2: Lebenslauf', async () => {
      await expectStepTitleToContainText('Lebenslauf', gsPage);
      const lebenslaufPO = new LebenslaufPO(gsPage);
      await expect(lebenslaufPO.elems.loading).toBeHidden();

      await lebenslaufPO.addTaetigkeit(taetigkeit);
      await lebenslaufPO.elems.buttonContinue.click();
    });

    await test.step('Step 3: Eigene Kinder', async () => {
      await expectStepTitleToContainText('Eigene Kinder', gsPage);

      const kinderPO = new KinderPO(gsPage);

      // hotfix for flaky test of Einnahmen & Kosten form
      const ausbildungPromise = gsPage.waitForResponse(
        '**/api/v1/ausbildungsstaette/slim',
      );
      await kinderPO.elems.buttonContinue.click();

      await test.step('Step 4: Einnahmen und Kosten', async () => {
        await expectStepTitleToContainText('Einnahmen & Kosten', gsPage);
        const einnahmenKostenPO = new EinnahmenKostenPO(gsPage);
        await expect(einnahmenKostenPO.elems.loading).toBeHidden();

        await ausbildungPromise;
        await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);
        await einnahmenKostenPO.elems.buttonSaveContinue.click();
      });
    });

    await test.step('Step 5: Familiensituation', async () => {
      await expectStepTitleToContainText('Familiensituation', gsPage);
      const familiyPO = new FamilyPO(gsPage);
      await expect(familiyPO.elems.loading).toBeHidden();

      await familiyPO.fillUnbekanntOderVerstorben(familienlsituation);
      await familiyPO.elems.buttonSaveContinue.click();
    });

    await test.step('Step 6.1: Eltern', async () => {
      await expectStepTitleToContainText('Eltern', gsPage);
      const elternPO = new ElternPO(gsPage);
      await expect(elternPO.elems.loading).toBeHidden();

      await elternPO.addMutter(mutter(seed));
      await elternPO.elems.buttonContinue.click();
    });

    await test.step('Step 6.2: Weitere Angaben Mutter', async () => {
      await expectStepTitleToContainText('Weitere Angaben Mutter', gsPage);
      const steuererklaerungPO = new SteruererklaerungPO(gsPage);
      await expect(steuererklaerungPO.elems.loading).toBeHidden();

      await steuererklaerungPO.fillSteuererklaerung(steuererklaerung);
      await steuererklaerungPO.elems.buttonSaveContinue.click();
    });

    await test.step('Step 7: Geschwister', async () => {
      await expectStepTitleToContainText('Geschwister', gsPage);
      const geschwisterPO = new GeschwisterPO(gsPage);
      await expect(geschwisterPO.elems.loading).toBeHidden();

      const patchResponse = gsPage.waitForResponse(
        (response) =>
          response.url().includes('/api/v1/gesuch/') &&
          response.request().method() === 'PATCH',
      );

      await geschwisterPO.addGeschwister(bruder);
      await patchResponse;

      await expect(gsPage.getByTestId('form-geschwister-loading')).toBeHidden();

      const requiredDokumenteResponse = gsPage.waitForResponse(
        '**/api/v1/gesuchtranche/*/dokumenteToUpload/gs',
      );

      await geschwisterPO.elems.buttonContinue.click();

      await test.step('Step 8: Dokumente', async () => {
        await expectStepTitleToContainText('Dokumente', gsPage);
        await requiredDokumenteResponse;
        await expect(
          gsPage.getByTestId('loading-required-dokumente'),
        ).toBeHidden();

        await uploadFilesById(gsPage);
        await gsPage.getByTestId('button-continue').click();
      });
    });

    await test.step('Step 9: Auszahlung', async () => {
      await expectStepTitleToContainText('Auszahlung', gsPage);
      const auszahlungPO = new AuszahlungPO(gsPage);
      await expect(auszahlungPO.elems.loading).toBeHidden();

      await auszahlungPO.elems.goToAuszahlungEdit.click();
      await auszahlungPO.fillAuszahlungEigenesKonto(zahlungsverbindungFn());

      await auszahlungPO.elems.buttonSaveContinue.click();
      await auszahlungPO.elems.buttonNext.click();
    });

    await test.step('Step 10: Freigabe', async () => {
      await expectStepTitleToContainText('Freigabe', gsPage);
      await gsPage.getByTestId('button-abschluss').click();
      const freigabeResponse = gsPage.waitForResponse(
        '**/api/v1/gesuch/*/einreichen/gs',
      );
      await gsPage.getByTestId('dialog-confirm').click();
      await freigabeResponse;
    });

    await gsPage.close();
  });

  test('SB: Gesuch bearbeiten und prüfen', async ({ createSbPage }) => {
    const sbPage = await createSbPage();
    await sbPage.bringToFront();
    await sbPage.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    await test.step('Gesuch öffnen und Status setzen', async () => {
      await expect(sbPage.getByRole('heading').nth(1)).toContainText(
        'Berechnung vom',
        {
          ignoreCase: true,
          timeout: 10000,
        },
      );

      const stepsNavPO = new StepsNavPO(sbPage);
      const icon = stepsNavPO.elems.steuerdatenMutter
        .locator('.validator-indicator')
        .first();
      await icon.scrollIntoViewIfNeeded();
      await expect(icon).toContainText('error');

      const headerNavPO = new SachbearbeiterGesuchHeaderPO(sbPage);

      await headerNavPO.elems.aktionMenu.click();
      await headerNavPO.elems
        .getAktionStatusUebergangItem('SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT')
        .click();

      await expect(headerNavPO.elems.actionLoading).toBeHidden();

      await sbPage
        .getByTestId('form-kommentar-dialog-kommentar')
        .fill('Test Kommentar');
      await sbPage.getByTestId('dialog-confirm').click();

      await headerNavPO.elems.aktionMenu.click();
      await headerNavPO.elems
        .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
        .click();

      await expect(headerNavPO.elems.actionLoading).toBeHidden();

      await headerNavPO.elems.aktionMenu.click();
      await headerNavPO.elems
        .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
        .click();

      await expect(headerNavPO.elems.actionLoading).toBeHidden();

      const status = sbPage.getByTestId('form-tranche-status');
      await expect(status).toHaveValue('In Bearbeitung');
    });

    await test.step('Einnahmen und Kosten bearbeiten', async () => {
      const stepsNavPO = new StepsNavPO(sbPage);

      await sbPage.locator('.mat-expansion-panel-header').nth(0).click();

      await stepsNavPO.elems.einnahmenKosten.first().click();
      await expectStepTitleToContainText('Einnahmen & Kosten', sbPage);
      const einnahmenKostenSbPO = new EinnahmenKostenPO(sbPage);
      await einnahmenKostenSbPO.fillEinnahmenKostenForm(einnhamenKostenSb);

      await einnahmenKostenSbPO.elems.buttonSaveContinue.click();
    });

    await test.step('Steuerdaten Mutter bearbeiten', async () => {
      const stepsNavPO = new StepsNavPO(sbPage);

      await expectStepTitleToContainText('Familiensituation', sbPage);

      await stepsNavPO.elems.steuerdatenMutter.first().click();
      await expectStepTitleToContainText('Steuerdaten Mutter', sbPage);
      const steuerDatenPO = new SteuerdatenPO(sbPage);
      await steuerDatenPO.fillSteuerdaten(steuerdaten);
      const steuerdatenResponse = sbPage.waitForResponse(
        '**/api/v1/steuerdaten/*',
      );
      await steuerDatenPO.elems.buttonSaveContinue.click();
      await steuerdatenResponse;
    });

    await test.step('Berechnung und Gesuchsverlauf prüfen', async () => {
      const gesuchsTabNavPO = new GesuchsTabNavPO(sbPage);
      await gesuchsTabNavPO.elems.verfuegungTab.click();

      await expect(sbPage.getByTestId('zusammenfassung-resultat')).toHaveClass(
        /accept/,
        { timeout: 10000 },
      );

      const headerNavPO = new SachbearbeiterGesuchHeaderPO(sbPage);
      await headerNavPO.elems.infosPageLink.click();

      await expectStepTitleToContainText('Gesuchsverlauf', sbPage);

      const protokollPO = new GesuchProtokollPO(sbPage);
      await protokollPO.checkAllStatusToCellVisible([
        'IN_BEARBEITUNG_GS',
        'EINGEREICHT',
        'ANSPRUCH_PRUEFEN',
        'DATENSCHUTZBRIEF_DRUCKBEREIT',
        'BEREIT_FUER_BEARBEITUNG',
        'IN_BEARBEITUNG_SB',
      ]);
    });

    await sbPage.close();
  });
});
