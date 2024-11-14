import { Page, expect } from '@playwright/test';

import { AusbildungPO, AusbildungValues } from './ausbildung.po';

export class CockpitPO {
  private page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  public getPeriodeTitle() {
    return this.page.getByTestId('cockpit-periode-title');
  }

  public getGesuchEdit() {
    return this.page.getByTestId('cockpit-gesuch-edit');
  }

  public async goToDashBoard() {
    await this.page.goto('/gesuch-app-feature-cockpit');
  }

  public async createNewStipendium(ausbildung: AusbildungValues) {
    await this.page.getByTestId('cockpit-create-ausbildung').click();

    const ausbildungPO = new AusbildungPO(this.page);
    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSave.click();
  }
}
