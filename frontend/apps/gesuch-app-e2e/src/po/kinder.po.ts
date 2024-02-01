import { Locator, Page, expect } from '@playwright/test';

import { Kind } from '@dv/shared/model/gesuch';

export class KinderPO {
  public elements: {
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
    this.elements = {
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
    await this.elements.addKind.click();

    await this.elements.nachname.fill(item.nachname);
    await this.elements.vorname.fill(item.vorname);
    await this.elements.geburtsdatum.fill(item.geburtsdatum);

    await this.elements.wohnsitzSelect.click();
    await this.elements.wohnsitzSelect.selectOption(item.wohnsitz);

    await this.elements.ausbildungssituationRadio.click();
    await this.elements.ausbildungssituationRadio.selectOption(
      item.ausbildungssituation,
    );

    await expect(this.elements.form).toHaveClass(/ng-valid/);

    await this.elements.getButtonSaveContinue.click();
  }
}
