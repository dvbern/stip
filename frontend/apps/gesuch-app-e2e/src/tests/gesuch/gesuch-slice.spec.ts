import { expect } from '@playwright/test';

import {
  SmallImageFile,
  expectStepTitleToContainText,
  getE2eUrls,
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
import { SteuerdatenPO } from '../../po/steuerdaten.po';
import {
  ausbildung,
  auszahlung,
  bruder,
  darlehen,
  einnahmenKosten,
  familienlsituation,
  mutter,
  person,
  steuerdaten,
  taetigkeit,
} from '../../test-data/slice-test-data';

const { test, getGesuchId } = initializeTest('GESUCHSTELLER', ausbildung);

test.describe('Neues gesuch erstellen', () => {
  test('Neues gesuch erstellen', async ({ page, cockpit }, testInfo) => {
    test.slow();
    const seed = `${testInfo.title}-${testInfo.workerIndex}`;

    await cockpit.getGesuchEdit().click();

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

    // Step 4.2: Steuerdaten Eltern =================================================
    await expectStepTitleToContainText('Steuerdaten', page);
    const steuerdatenPO = new SteuerdatenPO(page);
    await expect(steuerdatenPO.elems.loading).toBeHidden();

    await steuerdatenPO.fillSteuerdaten(steuerdaten);

    await steuerdatenPO.elems.buttonSaveContinue.click();

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
    await expectStepTitleToContainText('Auszahlung', page);
    const auszahlungPO = new AuszahlungPO(page);
    await expect(auszahlungPO.elems.loading).toBeHidden();

    await auszahlungPO.fillAuszahlungEigenesKonto(auszahlung);

    // hotfix for flaky test of Einnahmen & Kosten form
    // fix by changing the initialization of the form in a separate task
    const ausbildungPromise = page.waitForResponse(
      '**/api/v1/ausbildungsstaette',
    );

    await auszahlungPO.elems.buttonSaveContinue.click();

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
      '**/api/v1/gesuchtranche/*/requiredDokumente',
    );
    await darlehenPO.elems.buttonSaveContinue.click();

    // Step 10: Dokumente ===========================================================

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

    // Step 11: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);

    await page.getByTestId('button-abschluss').click();
    const freigabeResponse = page.waitForResponse(
      '**/api/v1/gesuch/*/einreichen',
    );
    await page.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    // Go to Berechnung (SB-App) ===================================================
    const urls = getE2eUrls();
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

    // Go to Gesuch infos (SB-App) ===================================================
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
