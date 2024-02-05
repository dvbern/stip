import { Locator, Page } from '@playwright/test';

import { Kind } from '@dv/shared/model/gesuch';

import { expectFormToBeValid, selectMatOption } from '../helpers/helpers';

export class KinderEditorPO {
  public elems: {
    form: Locator;

    nachname: Locator;
    vorname: Locator;
    geburtsdatum: Locator;
    wohnsitzSelect: Locator;
    ausbildungssituationRadio: Locator;

    buttonSave: Locator;
    buttonCancel: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      form: page.getByTestId('form-kind-form'),

      nachname: page.getByTestId('form-kind-nachname'),
      vorname: page.getByTestId('form-kind-vorname'),
      geburtsdatum: page.getByTestId('form-kind-geburtsdatum'),
      wohnsitzSelect: page.getByTestId('form-kind-wohnsitz'),
      ausbildungssituationRadio: page.getByTestId(
        'form-kind-ausbildungssituation',
      ),

      buttonSave: page.getByTestId('button-save'),
      buttonCancel: page.getByTestId('button-cance'),
    };
  }

  async addKind(item: Kind) {
    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);
    await this.elems.geburtsdatum.fill(item.geburtsdatum);

    await selectMatOption(this.elems.wohnsitzSelect, item.wohnsitz);

    await selectMatOption(
      this.elems.ausbildungssituationRadio,
      item.ausbildungssituation,
    );

    await expectFormToBeValid(this.elems.form);

    await this.elems.buttonSave.click();
  }
}
