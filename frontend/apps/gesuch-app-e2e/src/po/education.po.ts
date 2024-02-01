import { Locator, Page } from '@playwright/test';

import { Ausbildung } from '@dv/shared/model/gesuch';

export interface Education extends Ausbildung {
  ausbildungsstaette: string;
  ausbildungsgang: string;
  ausbildungsland: string;
}

export class EducationPO {
  public elements: {
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
  };

  constructor(page: Page) {
    this.elements = {
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
    };
  }

  public async fillEducationForm(ausbildung: Education) {
    await this.elements.ausbildungslandSelect.click();
    await this.elements.ausbildungslandSelect.selectOption(
      ausbildung.ausbildungsland,
    );

    await this.elements.ausbildungsstaetteSelect.click();
    await this.elements.ausbildungsstaetteSelect.selectOption(
      ausbildung.ausbildungsstaette,
    );

    await this.elements.ausbildungsgangSelect.click();
    await this.elements.ausbildungsgangSelect.selectOption(
      ausbildung.ausbildungsgang,
    );

    await this.elements.fachrichtung.fill(ausbildung.fachrichtung);

    await this.elements.ausbildungBegin.fill(ausbildung.ausbildungBegin);
    await this.elements.ausbildungEnd.fill(ausbildung.ausbildungEnd);

    await this.elements.pensumSelect.click();
    await this.elements.pensumSelect.selectOption(ausbildung.pensum);
  }
}
