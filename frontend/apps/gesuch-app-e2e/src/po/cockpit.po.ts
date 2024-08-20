import { Page } from '@playwright/test';

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

  public getGesuchNew() {
    return this.page.getByTestId('cockpit-gesuch-new').first();
  }
}
