import { Page } from 'playwright';
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

  // public getGesuchRemove() {
  //   return this.page.getByTestId('cockpit-gesuch-remove');
  // }

  public async goToDashBoard() {
    await this.page.goto('/');
  }

  public getGesuchNew() {
    return this.page.getByTestId('cockpit-gesuch-new');
  }
}
