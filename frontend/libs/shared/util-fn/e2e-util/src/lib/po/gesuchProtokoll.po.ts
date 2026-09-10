import { Locator, Page, expect } from '@playwright/test';

import { Gesuchstatus } from '@dv/shared/model/gesuch';

export class GesuchProtokollPO {
  readonly page: Page;
  readonly elems: {
    getStatusToCell: (statusTo: Gesuchstatus) => Locator;
  };

  constructor(page: Page) {
    this.page = page;
    this.elems = {
      getStatusToCell: (statusTo: Gesuchstatus) =>
        page.getByTestId(`status-to-${statusTo}`),
    };
  }

  public async checkAllStatusToCellVisible(
    statusTo: Gesuchstatus[],
  ): Promise<void> {
    for (const status of statusTo) {
      await expect(this.elems.getStatusToCell(status)).toBeVisible();
    }
  }
}
