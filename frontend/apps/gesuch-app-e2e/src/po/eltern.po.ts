import { Locator, Page, expect } from '@playwright/test';

import { Eltern } from '@dv/shared/model/gesuch';

import { ElternEditorPO } from './eltern-editor.po';

export class ElternPO {
  public elems: {
    page: Page;
    addVater: Locator;
    addMutter: Locator;

    loading: () => Locator;

    buttonContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      addVater: page.getByTestId('button-add-vater'),
      addMutter: page.getByTestId('button-add-mutter'),

      loading: () => page.getByTestId('form-eltern-loading'),

      buttonContinue: page.getByTestId('button-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async addVater(item: Eltern) {
    await this.elems.addVater.click();

    const editorPO = new ElternEditorPO(this.elems.page);

    await editorPO.fillElternTeilH(item);

    await expect(this.elems.loading()).toBeHidden();
  }

  async addMutter(item: Eltern) {
    await this.elems.addMutter.click();

    const editorPO = new ElternEditorPO(this.elems.page);

    await editorPO.fillElternTeilH(item);

    await expect(this.elems.loading()).toBeHidden();
  }
}
