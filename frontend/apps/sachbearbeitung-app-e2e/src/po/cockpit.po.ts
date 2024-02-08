import { Locator, Page } from '@playwright/test';

export class CockpitPO {
  public elems: {
    page: Page;
    title: Locator;
    list: Locator;
    rows: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      title: page.getByTestId('cockpit-title'),
      list: page.getByTestId('cockpit-table'),
      rows: page.getByTestId('cockpit-table'),
    };
  }

  async openGesuch() {}
}
