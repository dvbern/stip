import { Locator, Page } from '@playwright/test';

import { Geschwister } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatOption,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class GeschwisterEditorPO {
  public elems: {
    page: Page;
    form: Locator;
    nachname: Locator;
    vorname: Locator;
    geburtsdatum: Locator;
    wohnsitzSelect: Locator;
    wohnsitzMutter: Locator;
    wohnsitzVater: Locator;
    ausbildungssituationRadio: Locator;

    buttonSave: Locator;
    buttonCancel: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,

      form: page.getByTestId('form-geschwister-form'),

      nachname: page.getByTestId('form-geschwister-nachname'),
      vorname: page.getByTestId('form-geschwister-vorname'),
      geburtsdatum: page.getByTestId('form-geschwister-geburtsdatum'),
      wohnsitzSelect: page.getByTestId('form-geschwister-wohnsitz'),
      wohnsitzMutter: page.getByTestId('component-percentage-splitter-a'),
      wohnsitzVater: page.getByTestId('component-percentage-splitter-b'),
      ausbildungssituationRadio: page.getByTestId(
        'form-geschwister-ausbildungssituation',
      ),

      buttonSave: page.getByTestId('button-save'),
      buttonCancel: page.getByTestId('button-cancel-back'),
    };
  }

  async addGeschwister(item: Geschwister) {
    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);
    await this.elems.geburtsdatum.fill(item.geburtsdatum);

    await selectMatOption(this.elems.wohnsitzSelect, item.wohnsitz);

    if (item.wohnsitzAnteilMutter) {
      await this.elems.wohnsitzMutter.fill(`${item.wohnsitzAnteilMutter}`);
    }
    if (item.wohnsitzAnteilVater) {
      await this.elems.wohnsitzVater.fill(`${item.wohnsitzAnteilVater}`);
    }

    await selectMatRadio(
      this.elems.ausbildungssituationRadio,
      item.ausbildungssituation,
    );

    await expectFormToBeValid(this.elems.form);

    await this.elems.buttonSave.click();
  }
}
