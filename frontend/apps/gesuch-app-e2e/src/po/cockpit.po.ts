import { Locator, Page, expect } from '@playwright/test';

import { AusbildungPO, AusbildungValues } from './ausbildung.po';

export class CockpitPO {
  public elems: {
    page: Page;
    periodeTitle: Locator;
    gesuchEdit: Locator;
    createAusbildung: Locator;
    createAenderung: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      periodeTitle: page.getByTestId('cockpit-periode-title'),
      gesuchEdit: page.getByTestId('cockpit-gesuch-edit'),
      createAusbildung: page.getByTestId('cockpit-create-ausbildung'),
      createAenderung: page.getByTestId('cockpit-gesuch-aenderung-create'),
    };
  }

  public async goToDashBoard() {
    await this.elems.page.goto('/gesuch-app-feature-cockpit');
  }

  public async createNewStipendium(ausbildung: AusbildungValues) {
    await this.elems.createAusbildung.click();

    const ausbildungPO = new AusbildungPO(this.elems.page);
    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSave.click();
  }
}
