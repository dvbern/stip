import { Locator, Page } from '@playwright/test';

import { Ausbildung } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatOption,
} from '@dv/shared/util-fn/e2e-util';

export interface AusbildungValues extends Ausbildung {
  ausbildungsstaetteText: string;
  ausbildungsgangText: string;
  ausbildungsort: string;
  ausbildungsPLZ: string;
}

export class AusbildungPO {
  public elems: {
    page: Page;
    form: Locator;
    ausbildungsPLZ: Locator;
    ausbildungsOrt: Locator;
    isAusbildungAusland: Locator;
    ausbildungsstaetteSelect: Locator;
    alternativeAusbildungsstaette: Locator;
    ausbildungsgangSelect: Locator;
    alternativeAusbildungsgang: Locator;
    fachrichtung: Locator;
    ausbildungBegin: Locator;
    ausbildungEnd: Locator;
    pensumSelect: Locator;
    ausbildungNichtGefundenCheckbox: Locator;
    loading: Locator;
    buttonSave: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      form: page.getByTestId('form-education-form'),

      ausbildungsPLZ: page.getByTestId('form-education-ausbildungs-plz'),
      ausbildungsOrt: page.getByTestId('form-education-ausbildungs-ort'),
      isAusbildungAusland: page.getByTestId(
        'form-education-isAusbildungAusland',
      ),
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

      loading: page.getByTestId('education-form-loading'),

      buttonSave: page.getByTestId('button-save'),
    };
  }

  public async fillEducationForm(ausbildung: AusbildungValues) {
    await selectMatOption(
      this.elems.ausbildungsstaetteSelect,
      ausbildung.ausbildungsstaetteText,
    );

    await selectMatOption(
      this.elems.ausbildungsgangSelect,
      ausbildung.ausbildungsgangText,
    );

    if (ausbildung.fachrichtungBerufsbezeichnung) {
      await this.elems.fachrichtung.fill(
        ausbildung.fachrichtungBerufsbezeichnung,
      );
    }

    await this.elems.ausbildungsPLZ.fill(ausbildung.ausbildungsPLZ);
    await this.elems.ausbildungsOrt.fill(ausbildung.ausbildungsort);

    await this.elems.ausbildungBegin.fill(ausbildung.ausbildungBegin);
    await this.elems.ausbildungEnd.fill(ausbildung.ausbildungEnd);

    await selectMatOption(this.elems.pensumSelect, ausbildung.pensum);

    await expectFormToBeValid(this.elems.form);
  }
}
