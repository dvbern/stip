import { Locator, Page } from '@playwright/test';

export class FreigabePO {
  public elems: {
    page: Page;
    buttonAbschluss: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      buttonAbschluss: page.getByTestId('button-abschluss'),
    };
  }

  async abschluss() {
    await this.elems.buttonAbschluss.click();
  }
}
