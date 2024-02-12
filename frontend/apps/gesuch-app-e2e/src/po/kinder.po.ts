import { Locator, Page, expect } from '@playwright/test';

import { Kind } from '@dv/shared/model/gesuch';

import { KinderEditorPO } from './kinder-editor.po';

export class KinderPO {
  public elems: {
    page: Page;
    loading: Locator;

    addKind: Locator;

    buttonContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-kinder-loading'),

      addKind: page.getByTestId('button-add-kind'),

      buttonContinue: page.getByTestId('button-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async addKind(item: Kind) {
    await this.elems.addKind.click();

    const editorPO = new KinderEditorPO(this.elems.page);

    await editorPO.addKind(item);

    await expect(this.elems.loading).toBeHidden();
  }
}
