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
  uploadFiles,
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
  zahlungsverbindung,
} from '../../test-data/slice-test-data';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(ausbildung);

test.describe('Neues gesuch erstellen', () => {
  test.skip('Neues gesuch erstellen', async ({
    gsPage,
    createSbPage,
  }, testInfo) => {
    test.slow();
    const seed = `${testInfo.title}-${testInfo.workerIndex}`;

    await gsPage.bringToFront();

    // Step 1: Person ============================================================
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

    // Step 2: Lebenslauf ========================================================
    await expectStepTitleToContainText('Lebenslauf', gsPage);
    const lebenslaufPO = new LebenslaufPO(gsPage);
    await expect(lebenslaufPO.elems.loading).toBeHidden();

    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 3 Eigene Kinder =========================================================
    await expectStepTitleToContainText('Eigene Kinder', gsPage);

    const kinderPO = new KinderPO(gsPage);

    // // hotfix for flaky test of Einnahmen & Kosten form
    const ausbildungPromise = gsPage.waitForResponse(
      '**/api/v1/ausbildungsstaette/slim',
    );
    await kinderPO.elems.buttonContinue.click();

    // Step 4: Einnahmen und Kosten =================================================

    await expectStepTitleToContainText('Einnahmen & Kosten', gsPage);
    const einnahmenKostenPO = new EinnahmenKostenPO(gsPage);
    await expect(einnahmenKostenPO.elems.loading).toBeHidden();

    await ausbildungPromise;

    await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    await einnahmenKostenPO.elems.buttonSaveContinue.click();

    // Step 5: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', gsPage);
    const familiyPO = new FamilyPO(gsPage);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillUnbekanntOderVerstorben(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 6.1: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', gsPage);
    const elternPO = new ElternPO(gsPage);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addMutter(mutter(seed));

    await elternPO.elems.buttonContinue.click();

    // Step 6.2: Weitere Angaben Mutter  ==========================================
    await expectStepTitleToContainText('Weitere Angaben Mutter', gsPage);
    const steuererklaerungPO = new SteruererklaerungPO(gsPage);
    await expect(steuererklaerungPO.elems.loading).toBeHidden();

    await steuererklaerungPO.fillSteuererklaerung(steuererklaerung);

    await steuererklaerungPO.elems.buttonSaveContinue.click();

    // Step 7: Geschwister  ========================================================
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

    // Step 8: Dokumente ===========================================================
    await expectStepTitleToContainText('Dokumente', gsPage);
    await requiredDokumenteResponse;
    await expect(gsPage.getByTestId('loading-required-dokumente')).toBeHidden();

    await uploadFiles(gsPage);

    await gsPage.getByTestId('button-continue').click();

    // Step 9: Auszahlung ===========================================================

    await expectStepTitleToContainText('Auszahlung', gsPage);
    const auszahlungPO = new AuszahlungPO(gsPage);
    await expect(auszahlungPO.elems.loading).toBeHidden();

    // go to Auszahlung edit
    await auszahlungPO.elems.goToAuszahlungEdit.click();

    await auszahlungPO.fillAuszahlungEigenesKonto(zahlungsverbindung);

    await auszahlungPO.elems.buttonSaveContinue.click();
    await auszahlungPO.elems.buttonNext.click();

    // Step 10: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', gsPage);
    await gsPage.getByTestId('button-abschluss').click();
    const freigabeResponse = gsPage.waitForResponse(
      '**/api/v1/gesuch/*/einreichen/gs',
    );
    await gsPage.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    const urls = getE2eUrls();

    // Go to SB App ===============================================================
    const sbPage = await createSbPage();
    await sbPage.bringToFront();
    // replace with find in search table?
    await sbPage.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    // add testid to dynamic title later
    await expect(sbPage.getByRole('heading').nth(1)).toContainText(
      'Berechnung vom',
      {
        ignoreCase: true,
        timeout: 10000,
      },
    );

    // check if the i element in steuerdaten steps has the correct icon
    const stepsNavPO = new StepsNavPO(sbPage);
    const icon = stepsNavPO.elems.steuerdatenMutter
      .locator('.validator-indicator')
      .first();
    await icon.scrollIntoViewIfNeeded();
    await expect(icon).toContainText('error');

    // set to bearbeiten
    const headerNavPO = new SachbearbeiterGesuchHeaderPO(sbPage);

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

    // create trancheInfoPO later
    const status = sbPage.getByTestId('form-tranche-status');
    await expect(status).toHaveValue('In Bearbeitung');

    // fill E&K Sb part ===========================================================

    await sbPage.locator('.mat-expansion-panel-header').nth(0).click();

    await stepsNavPO.elems.einnahmenKosten.first().click();
    await expectStepTitleToContainText('Einnahmen & Kosten', sbPage);
    const einnahmenKostenSbPO = new EinnahmenKostenPO(sbPage);
    await einnahmenKostenSbPO.fillEinnahmenKostenForm(einnhamenKostenSb);

    await einnahmenKostenSbPO.elems.buttonSaveContinue.click();

    // fill steuerdaten ===========================================================
    await expectStepTitleToContainText('Familiensituation', sbPage);

    // expand second
    // await sbPage.locator('.mat-expansion-panel-header').nth(1).click();

    await stepsNavPO.elems.steuerdatenMutter.first().click();
    await expectStepTitleToContainText('Steuerdaten Mutter', sbPage);
    const steuerDatenPO = new SteuerdatenPO(sbPage);
    await steuerDatenPO.fillSteuerdaten(steuerdaten);
    const steuerdatenResponse = sbPage.waitForResponse(
      '**/api/v1/steuerdaten/*',
    );
    await steuerDatenPO.elems.buttonSaveContinue.click();
    await steuerdatenResponse;

    // Go to Berechnung ===========================================================

    const gesuchsTabNavPO = new GesuchsTabNavPO(sbPage);
    await gesuchsTabNavPO.elems.verfuegungTab.click();

    await expect(sbPage.getByTestId('zusammenfassung-resultat')).toHaveClass(
      /accept/,
      { timeout: 10000 },
    );

    // Go to Gesuch infos =========================================================
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

    sbPage.close();
    gsPage.close();
  });
});
