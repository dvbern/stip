import { Locator, Page, expect } from '@playwright/test';

import { LebenslaufItem } from '@dv/shared/model/gesuch';

import {
  LebenslaufEditorPO,
  LebenslaufItemValues,
} from './lebenslauf-editor.po';

export class LebenslaufPO {
  public elems: {
    page: Page;
    addAusbildung: Locator;
    addTaetigkeit: Locator;

    timelineGap: Locator;

    loading: Locator;

    buttonContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      addAusbildung: page.getByTestId('lebenslauf-add-ausbildung'),
      addTaetigkeit: page.getByTestId('lebenslauf-add-taetigkeit'),

      timelineGap: page.getByTestId('timeline-gap-block'),

      loading: page.getByTestId('lebenslauf-editor-loading'),

      buttonContinue: page.getByTestId('button-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async addAusbildung(item: LebenslaufItemValues) {
    await this.elems.addAusbildung.click();

    const editorPO = new LebenslaufEditorPO(this.elems.page);

    await editorPO.addAusbildung(item);

    await expect(this.elems.loading).toBeHidden();
  }

  async addTaetigkeit(item: LebenslaufItem) {
    await this.elems.addTaetigkeit.click();

    const editorPO = new LebenslaufEditorPO(this.elems.page);

    await editorPO.addTaetigkeit(item);

    await expect(this.elems.loading).toBeHidden();
  }
}
