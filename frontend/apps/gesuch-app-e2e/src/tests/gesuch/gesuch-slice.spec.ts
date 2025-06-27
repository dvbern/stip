import { expect } from '@playwright/test';

import {
  expectStepTitleToContainText,
  getE2eUrls,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
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

const { test, getGesuchId, getTrancheId } = initializeTest(
  'GESUCHSTELLER',
  ausbildung,
);

test.describe('Neues gesuch erstellen', () => {
  test('Neues gesuch erstellen', async ({ page, cockpit }, testInfo) => {
    test.slow();
    const seed = `${testInfo.title}-${testInfo.workerIndex}`;

    await cockpit.elems.gesuchEdit.click();

    // Step 1: Person ============================================================
    await expect(page.getByTestId('step-title')).toBeAttached({
      timeout: 10000,
    });
    await page.getByTestId('step-nav-person').first().click();
    await expectStepTitleToContainText('Person in Ausbildung', page);
    const personPO = new PersonPO(page);
    await expect(personPO.elems.loading).toBeHidden();

    await personPO.fillPersonForm(person(seed));

    await personPO.elems.buttonSaveContinue.click();

    // Step 2: Lebenslauf ========================================================
    await expectStepTitleToContainText('Lebenslauf', page);
    const lebenslaufPO = new LebenslaufPO(page);
    await expect(lebenslaufPO.elems.loading).toBeHidden();

    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 3: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', page);
    const familiyPO = new FamilyPO(page);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillUnbekanntOderVerstorben(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 4.1: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', page);
    const elternPO = new ElternPO(page);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addMutter(mutter(seed));

    await elternPO.elems.buttonContinue.click();

    // Step 4.2: Steuererklaerung Mutter  ==========================================
    await expectStepTitleToContainText('Steuererklärung Mutter', page);
    const steuererklaerungPO = new SteruererklaerungPO(page);
    await expect(steuererklaerungPO.elems.loading).toBeHidden();

    await steuererklaerungPO.fillSteuererklaerung(steuererklaerung);

    await steuererklaerungPO.elems.buttonSaveContinue.click();

    // Step 5: Geschwister  ========================================================
    await expectStepTitleToContainText('Geschwister', page);
    const geschwisterPO = new GeschwisterPO(page);
    await expect(geschwisterPO.elems.loading).toBeHidden();

    await geschwisterPO.addGeschwister(bruder);

    await geschwisterPO.elems.buttonContinue.click();

    // Step 6: Kinder =============================================================
    await expectStepTitleToContainText('Kinder', page);
    const kinderPO = new KinderPO(page);
    await kinderPO.elems.buttonContinue.click();

    // Step 7: Auszahlung ===========================================================

    // todo => Zahlungsverbindung
    await expectStepTitleToContainText('Auszahlung', page);
    const auszahlungPO = new AuszahlungPO(page);
    await expect(auszahlungPO.elems.loading).toBeHidden();

    // go to Auszahlung edit
    await auszahlungPO.elems.goToAuszahlungEdit.click();

    await auszahlungPO.fillAuszahlungEigenesKonto(zahlungsverbindung);

    // // hotfix for flaky test of Einnahmen & Kosten form
    const ausbildungPromise = page.waitForResponse(
      '**/api/v1/ausbildungsstaette',
    );

    await auszahlungPO.elems.buttonSave.click();
    await auszahlungPO.elems.buttonBack.click();
    await auszahlungPO.elems.buttonNext.click();

    // Step 8: Einnahmen und Kosten =================================================
    await expectStepTitleToContainText('Einnahmen & Kosten', page);
    const einnahmenKostenPO = new EinnahmenKostenPO(page);
    await expect(einnahmenKostenPO.elems.loading).toBeHidden();

    await ausbildungPromise;

    await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    await einnahmenKostenPO.elems.buttonSaveContinue.click();

    // Step 9: Darlehen =============================================================
    await expectStepTitleToContainText('Darlehen', page);
    const darlehenPO = new DarlehenPO(page);
    await expect(darlehenPO.elems.loading).toBeHidden();

    await darlehenPO.fillDarlehenForm(darlehen);

    const requiredDokumenteResponse = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
    );
    await darlehenPO.elems.buttonSaveContinue.click();

    // Step 10: Dokumente ===========================================================
    await expectStepTitleToContainText('Dokumente', page);
    await requiredDokumenteResponse;
    await uploadFiles(page);
    await page.getByTestId('button-continue').click();

    // Step 11: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);
    await page.getByTestId('button-abschluss').click();
    const freigabeResponse = page.waitForResponse(
      '**/api/v1/gesuch/*/einreichen/gs',
    );
    await page.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    const urls = getE2eUrls();

    // Go to SB App ===============================================================
    await page.goto(
      `${urls.sb}/gesuch/info/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    // add testid to dynamic title later
    await expect(page.getByRole('heading').nth(1)).toContainText('Tranche 1', {
      ignoreCase: true,
      timeout: 10000,
    });

    // check if the i element in steuerdaten steps has the correct icon
    const stepsNavPO = new StepsNavPO(page);
    const icon = stepsNavPO.elems.steuerdatenMutter
      .locator('.text-danger')
      .first();
    await expect(icon).toContainText('error');

    // set to bearbeiten
    const headerNavPO = new SachbearbeiterGesuchHeaderPO(page);

    await headerNavPO.elems.aktionMenu.click();
    await headerNavPO.elems
      .getAktionStatusUebergangItem('BEREIT_FUER_BEARBEITUNG')
      .click();
    await page.getByTestId('dialog-confirm').click();

    await headerNavPO.elems.aktionMenu.click();
    await headerNavPO.elems
      .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
      .click();

    // create trancheInfoPO later
    const status = page.getByTestId('form-tranche-status');
    await expect(status).toHaveValue('In Bearbeitung');

    // fill steuerdaten ===========================================================
    await stepsNavPO.elems.steuerdatenMutter.first().click();
    await expectStepTitleToContainText('Steuerdaten Mutter', page);
    const steuerDatenPO = new SteuerdatenPO(page);
    await steuerDatenPO.fillSteuerdaten(steuerdaten);
    const steuerdatenResponse = page.waitForResponse('**/api/v1/steuerdaten/*');
    await steuerDatenPO.elems.buttonSaveContinue.click();
    await steuerdatenResponse;

    // Go to Berechnung ===========================================================
    // will log the user out if verfuegung is not available yet!
    await page.goto(`${urls.sb}/verfuegung/${getGesuchId()}/zusammenfassung`);
    await expect(page.getByTestId('zusammenfassung-resultat')).toHaveClass(
      /accept/,
      { timeout: 10000 },
    );
    await page.goto(`${urls.sb}/verfuegung/${getGesuchId()}/berechnung/1`);
    await expect(
      page.getByTestId('berechnung-persoenlich-total'),
    ).toContainText("- 14'974");
    await expect(page.getByTestId('berechnung-familien-total')).toContainText(
      "- 55'492",
    );

    // Go to Gesuch infos =========================================================
    await page.getByTestId('sb-gesuch-header-infos-link').click();
    await expect(page.getByTestId('step-title')).toContainText(
      'Gesuchsverlauf',
    );
    await expect(
      page.getByRole('cell', { name: 'Bereit für Bearbeitung' }),
    ).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Eingereicht' })).toBeVisible();
    await expect(
      page.getByRole('cell', { name: 'Bearbeitung durch Gesuchsteller' }),
    ).toBeVisible();
  });
});
