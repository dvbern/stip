import { Locator, Page } from '@playwright/test';

export class PercentageSplitterPO {
  public elems: {
    page: Page;
    percentA: Locator;
    percentB: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      percentA: page.getByTestId('component-percentage-splitter-a'),
      percentB: page.getByTestId('component-percentage-splitter-b'),
    };
  }

  async fillPercentageSplitterForm(value: {
    percentA: string;
    percentB: string;
  }) {
    await this.elems.percentA.fill(value.percentA);

    await this.elems.percentB.fill(value.percentB);
  }
}
