import { Locator, Page, expect } from '@playwright/test';

import { Kind } from '@dv/shared/model/gesuch';

export class KinderPO {
  public elems: {
    loading: () => Locator;

    addKind: Locator;

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
      loading: () => page.getByTestId('form-kinder-loading'),

      addKind: page.getByTestId('button-add-kind'),

      form: page.getByTestId('form-kind-form'),

      nachname: page.getByTestId('form-kind-nachname'),
      vorname: page.getByTestId('form-kind-vorname'),
      geburtsdatum: page.getByTestId('form-kind-geburtsdatum'),
      wohnsitzSelect: page.getByTestId('form-kind-wohnsitz'),
      ausbildungssituationRadio: page.getByTestId(
        'form-kind-ausbildungssituation',
      ),

      getButtonSaveContinue: page.getByTestId('button-save-continue'),
      getButtonCancelBack: page.getByTestId('button-cancel-back'),
    };
  }

  async addKind(item: Kind) {
    await this.elems.addKind.click();

    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);
    await this.elems.geburtsdatum.fill(item.geburtsdatum);

    await this.elems.wohnsitzSelect.click();
    await this.elems.wohnsitzSelect.selectOption(item.wohnsitz);

    await this.elems.ausbildungssituationRadio.click();
    await this.elems.ausbildungssituationRadio.selectOption(
      item.ausbildungssituation,
    );

    await expect(this.elems.form).toHaveClass(/ng-valid/);

    await this.elems.getButtonSaveContinue.click();
  }
}
