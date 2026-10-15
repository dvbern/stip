import { Locator, Page } from '@playwright/test';

export class ConfirmDialogPO {
  public elems: {
    page: Page;
    dialog: Locator;
    cancelButton: Locator;
    confirmButton: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      dialog: page.getByTestId('confirm-dialog'),
      cancelButton: page.getByTestId('dialog-cancel'),
      confirmButton: page.getByTestId('dialog-confirm'),
    };
  }
}
