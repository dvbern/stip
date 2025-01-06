import { expect } from '@playwright/test';

import {
  SmallImageFile,
  selectMatRadio,
  specificMonth,
  specificMonthPlusYears,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
import { AusbildungValues } from '../../po/ausbildung.po';
import { PersonPO } from '../../po/person.po';

const ausbildung: AusbildungValues = {
  fallId: '',
  status: 'AKTIV',
  editable: true,
  ausbildungsort: 'Bern',
  ausbildungsstaetteText: 'Universität Bern',
  ausbildungsgangText: 'Master',
  fachrichtung: 'Kunstgeschichte',
  ausbildungBegin: specificMonth(9),
  ausbildungEnd: specificMonthPlusYears(8, 3),
  pensum: 'VOLLZEIT',
};

const { test } = initializeTest('GESUCHSTELLER', ausbildung);

test.describe('Dokument upload', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Dokument upload', async ({ page, cockpit }) => {
    await expect(page.getByTestId('step-title')).toBeAttached();
    await page.getByTestId('step-nav-person').first().click();
    const person = new PersonPO(page);

    await selectMatRadio(person.elems.sozialhilfeBeitraegeRadio, true);

    await page
      .getByTestId('button-document-upload-PERSON_SOZIALHILFEBUDGET')
      .click();
    await page.getByTestId('file-input').setInputFiles(SmallImageFile);
    await expect(page.getByTestId('document-download-small.png')).toBeEnabled();
    await page.keyboard.press('Escape');
  });
});
