import { Locator, Page, expect } from '@playwright/test';

import { AusbildungPO, AusbildungValues } from './ausbildung.po';

export class CockpitPO {
  public elems: {
    page: Page;
    periodeTitle: Locator;
    gesuchEdit: Locator;
    createAusbildung: Locator;
    createAenderung: Locator;
    buttonCancel: Locator;
    buttonAcceptULA: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      periodeTitle: page.getByTestId('cockpit-periode-title'),
      gesuchEdit: page.getByTestId('cockpit-gesuch-edit'),
      createAusbildung: page.getByTestId('cockpit-create-ausbildung'),
      createAenderung: page.getByTestId('cockpit-gesuch-aenderung-create'),
      buttonCancel: page.getByTestId('cancel-button'),
      buttonAcceptULA: page.getByTestId('accept-button'),
    };
  }

  public async goToDashBoard() {
    await this.elems.page.goto('/dashboard');
  }

  public async createNewStipendium(ausbildung: AusbildungValues) {
    await this.elems.createAusbildung.click();

    // todo: why is accepting ULA not required here?

    const ausbildungPO = new AusbildungPO(this.elems.page);

    expect(ausbildungPO.elems.form).toBeVisible();

    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSave.click();
  }
}
