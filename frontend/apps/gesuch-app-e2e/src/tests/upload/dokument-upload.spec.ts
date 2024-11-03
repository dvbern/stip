import { expect } from '@playwright/test';

import { SmallImageFile, selectMatRadio } from '@dv/shared/util-fn/e2e-util';

import { PersonPO } from '../../po/person.po';
import { initializeTest } from '../../utils';

const { test } = initializeTest('GESUCHSTELLER');

test.describe('Dokument upload', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Person in Ausbildung', async ({ page, cockpit }) => {
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
