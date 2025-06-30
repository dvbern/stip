import { expect } from '@playwright/test';

import {
  expectStepTitleToContainText,
  getE2eUrls,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { initializeMultiUserTest } from '../../initialize-test';
import { AuszahlungPO } from '../../po/auszahlung.po';
import { DarlehenPO } from '../../po/darlehen.po';
import { EinnahmenKostenPO } from '../../po/einnahmen-kosten.po';
import { ElternPO } from '../../po/eltern.po';
import { FamilyPO } from '../../po/familiy.po';
import { GeschwisterPO } from '../../po/geschwister.po';
import { KinderPO } from '../../po/kinder.po';
import { LebenslaufPO } from '../../po/lebenslauf.po';
import { PersonPO } from '../../po/person.po';
import { SachbearbeiterGesuchHeaderPO } from '../../po/sachbearbeiter-gesuch-header.po';
import { StepsNavPO } from '../../po/steps-nav.po';
import { SteuerdatenPO } from '../../po/steuerdaten.po';
import { SteruererklaerungPO } from '../../po/steuererklaerung.po';
import {
  ausbildung,
  bruder,
  darlehen,
  einnahmenKosten,
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
  test('Neues gesuch erstellen', async ({ gsPage, sbPage }, testInfo) => {
    test.slow();
    const seed = `${testInfo.title}-${testInfo.workerIndex}`;

    await gsPage.bringToFront();
    await gsPage.getByTestId('cockpit-gesuch-edit').click();

    // Step 1: Person ============================================================
    await expect(gsPage.getByTestId('step-title')).toBeAttached({
      timeout: 10000,
    });
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

    // Step 3: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', gsPage);
    const familiyPO = new FamilyPO(gsPage);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillUnbekanntOderVerstorben(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 4.1: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', gsPage);
    const elternPO = new ElternPO(gsPage);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addMutter(mutter(seed));

    await elternPO.elems.buttonContinue.click();

    // Step 4.2: Steuererklaerung Mutter  ==========================================
    await expectStepTitleToContainText('Steuererklärung Mutter', gsPage);
    const steuererklaerungPO = new SteruererklaerungPO(gsPage);
    await expect(steuererklaerungPO.elems.loading).toBeHidden();

    await steuererklaerungPO.fillSteuererklaerung(steuererklaerung);

    await steuererklaerungPO.elems.buttonSaveContinue.click();

    // Step 5: Geschwister  ========================================================
    await expectStepTitleToContainText('Geschwister', gsPage);
    const geschwisterPO = new GeschwisterPO(gsPage);
    await expect(geschwisterPO.elems.loading).toBeHidden();

    await geschwisterPO.addGeschwister(bruder);

    await geschwisterPO.elems.buttonContinue.click();

    // Step 6: Kinder =============================================================
    await expectStepTitleToContainText('Kinder', gsPage);
    const kinderPO = new KinderPO(gsPage);
    await kinderPO.elems.buttonContinue.click();

    // Step 7: Auszahlung ===========================================================

    await expectStepTitleToContainText('Auszahlung', gsPage);
    const auszahlungPO = new AuszahlungPO(gsPage);
    await expect(auszahlungPO.elems.loading).toBeHidden();

    // go to Auszahlung edit
    await auszahlungPO.elems.goToAuszahlungEdit.click();

    await auszahlungPO.fillAuszahlungEigenesKonto(zahlungsverbindung);

    // // hotfix for flaky test of Einnahmen & Kosten form
    const ausbildungPromise = gsPage.waitForResponse(
      '**/api/v1/ausbildungsstaette',
    );

    await auszahlungPO.elems.buttonSave.click();
    await auszahlungPO.elems.buttonBack.click();
    await auszahlungPO.elems.buttonNext.click();

    // Step 8: Einnahmen und Kosten =================================================
    await expectStepTitleToContainText('Einnahmen & Kosten', gsPage);
    const einnahmenKostenPO = new EinnahmenKostenPO(gsPage);
    await expect(einnahmenKostenPO.elems.loading).toBeHidden();

    await ausbildungPromise;

    await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    await einnahmenKostenPO.elems.buttonSaveContinue.click();

    // Step 9: Darlehen =============================================================
    await expectStepTitleToContainText('Darlehen', gsPage);
    const darlehenPO = new DarlehenPO(gsPage);
    await expect(darlehenPO.elems.loading).toBeHidden();

    await darlehenPO.fillDarlehenForm(darlehen);

    const requiredDokumenteResponse = gsPage.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
    );
    await darlehenPO.elems.buttonSaveContinue.click();

    // Step 10: Dokumente ===========================================================
    await expectStepTitleToContainText('Dokumente', gsPage);
    await requiredDokumenteResponse;
    await uploadFiles(gsPage);
    await gsPage.getByTestId('button-continue').click();

    // Step 11: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', gsPage);
    await gsPage.getByTestId('button-abschluss').click();
    const freigabeResponse = gsPage.waitForResponse(
      '**/api/v1/gesuch/*/einreichen/gs',
    );
    await gsPage.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    const urls = getE2eUrls();

    // Go to SB App ===============================================================
    await sbPage.bringToFront();
    await sbPage.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    // add testid to dynamic title later
    await expect(sbPage.getByRole('heading').nth(1)).toContainText(
      'Tranche 1',
      {
        ignoreCase: true,
        timeout: 10000,
      },
    );

    // check if the i element in steuerdaten steps has the correct icon
    const stepsNavPO = new StepsNavPO(sbPage);
    const icon = stepsNavPO.elems.steuerdatenMutter
      .locator('.text-danger')
      .first();
    await expect(icon).toContainText('error');

    // set to bearbeiten
    const headerNavPO = new SachbearbeiterGesuchHeaderPO(sbPage);

    await headerNavPO.elems.aktionMenu.click();
    await headerNavPO.elems
      .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
      .click();
    await sbPage.getByTestId('dialog-confirm').click();

    await headerNavPO.elems.aktionMenu.click();
    await headerNavPO.elems
      .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
      .click();

    // create trancheInfoPO later
    const status = sbPage.getByTestId('form-tranche-status');
    await expect(status).toHaveValue('In Bearbeitung');

    // fill steuerdaten ===========================================================
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
    // will log the user out if verfuegung is not available yet!
    await sbPage.goto(`${urls.sb}/verfuegung/${getGesuchId()}/zusammenfassung`);
    await expect(sbPage.getByTestId('zusammenfassung-resultat')).toHaveClass(
      /accept/,
      { timeout: 10000 },
    );
    await sbPage.goto(`${urls.sb}/verfuegung/${getGesuchId()}/berechnung/1`);
    await expect(
      sbPage.getByTestId('berechnung-persoenlich-total'),
    ).toContainText("- 14'974");
    await expect(sbPage.getByTestId('berechnung-familien-total')).toContainText(
      "- 55'492",
    );

    // Go to Gesuch infos =========================================================
    await sbPage.getByTestId('sb-gesuch-header-infos-link').click();
    await expect(sbPage.getByTestId('step-title')).toContainText(
      'Gesuchsverlauf',
    );
    await expect(
      sbPage.getByRole('cell', { name: 'Bereit für Bearbeitung' }),
    ).toBeVisible();
    await expect(
      sbPage.getByRole('cell', { name: 'Eingereicht' }),
    ).toBeVisible();
    await expect(
      sbPage.getByRole('cell', { name: 'Bearbeitung durch Gesuchsteller' }),
    ).toBeVisible();

    sbPage.close();
    gsPage.close();
  });
});
