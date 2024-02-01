import { Locator, Page, expect } from '@playwright/test';

import { Geschwister } from '@dv/shared/model/gesuch';

export class GeschwisterPO {
  public elements: {
    page: Page;
    loading: () => Locator;
    addGeschwister: Locator;
    form: Locator;
    nachname: Locator;
    vorname: Locator;
    geburtsdatum: Locator;
    wohnsitzSelect: Locator;
    ausbildungssituationRadio: Locator;
    getButtonSaveContinue: Locator;
    getButtonCancelBack: Locator;
  };

  constructor(page: Page) {
    this.elements = {
      page,
      loading: () => page.getByTestId('form-geschwister-loading'),
      addGeschwister: page.getByTestId('button-add-geschwister'),
      form: page.getByTestId('form-geschwister-form'),

      nachname: page.getByTestId('form-geschwister-nachname'),
      vorname: page.getByTestId('form-geschwister-vorname'),
      geburtsdatum: page.getByTestId('form-geschwister-geburtsdatum'),
      wohnsitzSelect: page.getByTestId('form-geschwister-wohnsitz'),
      ausbildungssituationRadio: page.getByTestId(
        'form-geschwister-ausbildungssituation',
      ),

      getButtonSaveContinue: page.getByTestId('button-save-continue'),
      getButtonCancelBack: page.getByTestId('button-cancel-back'),
    };
  }

  async addGeschwister(item: Geschwister) {
    await this.elements.addGeschwister.click();

    await this.elements.nachname.fill(item.nachname);
    await this.elements.vorname.fill(item.vorname);
    await this.elements.geburtsdatum.fill(item.geburtsdatum);

    await this.elements.wohnsitzSelect.click();
    await this.elements.page.getByTestId(item.wohnsitz).first().click();

    await this.elements.ausbildungssituationRadio.click();
    await this.elements.page
      .getByTestId(item.ausbildungssituation)
      .first()
      .click();

    await expect(this.elements.form).toHaveClass(/ng-valid/);
    await this.elements.getButtonSaveContinue.click();
  }
}
