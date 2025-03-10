import { Locator, Page } from '@playwright/test';

import { SteuererklaerungUpdate } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class SteruererklaerungPO {
  public elems: {
    page: Page;
    form: Locator;
    steuererklaerungInBernRadio: Locator;

    loading: Locator;
    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      form: page.getByTestId('form-eltern-steuererklaerung-form'),
      steuererklaerungInBernRadio: page.getByTestId(
        'form-eltern-steuererklaerung-steuererklaerungInBern',
      ),

      loading: page.getByTestId('loading'),
      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillSteuererklaerung(
    item: Omit<SteuererklaerungUpdate, 'steuerdatenTyp'>,
  ) {
    await selectMatRadio(
      this.elems.steuererklaerungInBernRadio,
      item.steuererklaerungInBern,
    );

    await expectFormToBeValid(this.elems.form);
  }
}
