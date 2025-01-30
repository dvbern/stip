import { Locator, Page } from '@playwright/test';

export class CockpitPO {
  public elems: {
    page: Page;
    adminLink: Locator;
    title: Locator;
    list: Locator;
    rows: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      adminLink: page.getByTestId('gesuch-step-nav-administration'),
      title: page.getByTestId('cockpit-title'),
      list: page.getByTestId('cockpit-table'),
      rows: page.getByTestId('cockpit-row-sv'),
    };
  }

  public async goToAdmin() {
    await this.elems.adminLink.click();
  }

  public async goToDashBoard() {
    await this.elems.page.goto('/sachbearbeitung-app-feature-cockpit');
  }
}
