import { Locator, Page } from '@playwright/test';

export class GesuchsTabNavPO {
  readonly page: Page;
  readonly elems: {
    gesuchTab: Locator;
    verfuegungTab: Locator;
  };

  constructor(page: Page) {
    this.page = page;
    this.elems = {
      gesuchTab: page.getByTestId('gesuchs-tab-nav-gesuch-tab'),
      verfuegungTab: page.getByTestId('gesuchs-tab-nav-verfuegung-tab'),
    };
  }
}
