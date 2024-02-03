import { Locator, Page, expect } from '@playwright/test';

import { Geschwister } from '@dv/shared/model/gesuch';

export class GeschwisterPO {
  public elems: {
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
    this.elems = {
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
    await this.elems.addGeschwister.click();

    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);
    await this.elems.geburtsdatum.fill(item.geburtsdatum);

    await this.elems.wohnsitzSelect.click();
    await this.elems.page.getByTestId(item.wohnsitz).first().click();

    await this.elems.ausbildungssituationRadio.click();
    await this.elems.page
      .getByTestId(item.ausbildungssituation)
      .first()
      .click();

    await expect(this.elems.form).toHaveClass(/ng-valid/);
    await this.elems.getButtonSaveContinue.click();
  }
}
