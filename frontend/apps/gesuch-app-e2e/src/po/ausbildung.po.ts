import { Locator, Page } from '@playwright/test';

import { Ausbildung } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatOption,
} from '@dv/shared/util-fn/e2e-util';

export interface AusbildungValues extends Ausbildung {
  ausbildungsstaette: string;
  ausbildungsgang: string;
  ausbildungsland: string;
}

export class AusbildungPO {
  public elems: {
    page: Page;
    form: Locator;
    ausbildungslandSelect: Locator;
    ausbildungsstaetteSelect: Locator;
    alternativeAusbildungsstaette: Locator;
    ausbildungsgangSelect: Locator;
    alternativeAusbildungsgang: Locator;
    fachrichtung: Locator;
    ausbildungBegin: Locator;
    ausbildungEnd: Locator;
    pensumSelect: Locator;
    ausbildungNichtGefundenCheckbox: Locator;
    loading: () => Locator;
    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      form: page.getByTestId('form-education-form'),

      ausbildungslandSelect: page.getByTestId('form-education-ausbildungsland'),
      ausbildungsstaetteSelect: page.getByTestId(
        'form-education-ausbildungsstaette',
      ),
      alternativeAusbildungsstaette: page.getByTestId(
        'form-education-alternativeAusbildungsstaette',
      ),
      ausbildungsgangSelect: page.getByTestId('form-education-ausbildungsgang'),
      alternativeAusbildungsgang: page.getByTestId(
        'form-education-alternativeAusbildungsgang',
      ),
      fachrichtung: page.getByTestId('form-education-fachrichtung'),
      ausbildungBegin: page.getByTestId('form-education-beginn-der-ausbildung'),
      ausbildungEnd: page.getByTestId('form-education-ende-der-ausbildung'),
      pensumSelect: page.getByTestId('form-education-pensum'),

      ausbildungNichtGefundenCheckbox: page.getByTestId(
        'form-education-ausbidungNichtGefunden',
      ),

      loading: () => page.getByTestId('education-form-loading'),
      // todo: now page objects to do:
      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  public async fillEducationForm(ausbildung: AusbildungValues) {
    await selectMatOption(
      this.elems.ausbildungslandSelect,
      ausbildung.ausbildungsland,
    );

    await selectMatOption(
      this.elems.ausbildungsstaetteSelect,
      ausbildung.ausbildungsstaette,
    );

    await selectMatOption(
      this.elems.ausbildungsgangSelect,
      ausbildung.ausbildungsgang,
    );

    await this.elems.fachrichtung.fill(ausbildung.fachrichtung);

    await this.elems.ausbildungBegin.fill(ausbildung.ausbildungBegin);
    await this.elems.ausbildungEnd.fill(ausbildung.ausbildungEnd);

    await selectMatOption(this.elems.pensumSelect, ausbildung.pensum);

    await expectFormToBeValid(this.elems.form);
  }
}
