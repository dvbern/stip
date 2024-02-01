import { Locator, Page } from '@playwright/test';

export class PercentageSplitterPO {
  public elements: {
    page: Page;
    percentA: Locator;
    percentB: Locator;
  };

  constructor(page: Page) {
    this.elements = {
      page,
      percentA: page.getByTestId('component-percentage-splitter-a'),
      percentB: page.getByTestId('component-percentage-splitter-b'),
    };
  }

  async fillPercentageSplitterForm(value: {
    percentA: string;
    percentB: string;
  }) {
    await this.elements.percentA.fill(value.percentA);

    await this.elements.percentB.fill(value.percentB);
  }
}
