import { Locator, Page, expect } from '@playwright/test';

import { Geschwister } from '@dv/shared/model/gesuch';

import { GeschwisterEditorPO } from './geschwister-editor.po';

export class GeschwisterPO {
  public elems: {
    page: Page;
    loading: Locator;
    addGeschwister: Locator;
    geschwisterRow: Locator;
    buttonContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-geschwister-loading'),
      addGeschwister: page.getByTestId('button-add-geschwister'),
      geschwisterRow: page.getByTestId('geschwister-row'),
      buttonContinue: page.getByTestId('button-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async addGeschwister(item: Geschwister) {
    await this.elems.addGeschwister.click();

    const editorPO = new GeschwisterEditorPO(this.elems.page);

    await editorPO.addGeschwister(item);

    await expect(this.elems.loading).toBeHidden();
  }
}
