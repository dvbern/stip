import { Locator, Page } from '@playwright/test';

import { LebenslaufItem } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatOption,
} from '@dv/shared/util-fn/e2e-util';

export class LebenslaufEditorPO {
  public elems: {
    page: Page;

    form: Locator;

    ausbildungsartSelect: Locator;
    berufsbezeichnung: Locator;
    fachrichtung: Locator;
    titelDesAbschlusses: Locator;
    taetigkeitsartSelect: Locator;
    taetigkeitsBeschreibung: Locator;
    beginn: Locator;
    ende: Locator;
    wohnsitzSelect: Locator;
    ausbildungAbgeschlossenCheckbox: Locator;

    buttonDelete: Locator;
    buttonSave: Locator;
    buttonCancel: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,

      form: page.getByTestId('form-lebenslauf-form'),

      ausbildungsartSelect: page.getByTestId(
        'lebenslauf-editor-ausbildungsart-select',
      ),
      berufsbezeichnung: page.getByTestId(
        'lebenslauf-editor-berufsbezeichnung',
      ),
      fachrichtung: page.getByTestId('lebenslauf-editor-fachrichtung'),
      titelDesAbschlusses: page.getByTestId(
        'lebenslauf-editor-titelDesAbschlusses',
      ),
      taetigkeitsartSelect: page.getByTestId(
        'lebenslauf-editor-taetigkeitsart-select',
      ),
      taetigkeitsBeschreibung: page.getByTestId(
        'lebenslauf-editor-taetigkeitsBeschreibung',
      ),
      beginn: page.getByTestId('lebenslauf-editor-von'),
      ende: page.getByTestId('lebenslauf-editor-bis'),
      wohnsitzSelect: page.getByTestId('lebenslauf-editor-wohnsitz'),
      ausbildungAbgeschlossenCheckbox: page.getByTestId(
        'lebenslauf-editor-ausbildung-abgeschlossen',
      ),

      buttonDelete: page.getByTestId('lebenslauf-editor-delete'),
      buttonSave: page.getByTestId('button-save'),
      buttonCancel: page.getByTestId('button-cancel'),
    };
  }

  async addAusbildung(item: LebenslaufItem) {
    await selectMatOption(
      this.elems.ausbildungsartSelect,
      item.bildungsart ?? 'FACHMATURITAET',
    );

    await this.elems.beginn.fill(item.von);
    await this.elems.ende.fill(item.bis);

    await selectMatOption(this.elems.wohnsitzSelect, item.wohnsitz);

    await this.elems.ausbildungAbgeschlossenCheckbox.click();

    await expectFormToBeValid(this.elems.form);

    await this.elems.buttonSave.click();
  }

  async addTaetigkeit(item: LebenslaufItem) {
    await selectMatOption(
      this.elems.taetigkeitsartSelect,
      item.taetigkeitsart ?? 'ERWERBSTAETIGKEIT',
    );

    await this.elems.taetigkeitsBeschreibung.fill(
      item.taetigkeitsBeschreibung ?? '',
    );

    await this.elems.beginn.fill(item.von);
    await this.elems.ende.fill(item.bis);

    await selectMatOption(this.elems.wohnsitzSelect, item.wohnsitz);

    await expectFormToBeValid(this.elems.form);
    await this.elems.buttonSave.click();
  }
}
