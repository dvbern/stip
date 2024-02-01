import { Locator, Page, expect } from '@playwright/test';

import { LebenslaufItem } from '@dv/shared/model/gesuch';

export class LebenslaufPO {
  public elements: {
    page: Page;
    addAusbildung: Locator;
    addTaetigkeit: Locator;

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

    timelineGap: Locator;

    loading: () => Locator;
    getButtonDelete: Locator;
    getButtonSave: Locator;
    getButtonBack: Locator;
  };

  constructor(page: Page) {
    this.elements = {
      page,
      addAusbildung: page.getByTestId('lebenslauf-add-ausbildung'),
      addTaetigkeit: page.getByTestId('lebenslauf-add-taetigkeit'),

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

      timelineGap: page.getByTestId('timeline-gap-block'),

      loading: () => page.getByTestId('lebenslauf-editor-loading'),
      getButtonDelete: page.getByTestId('lebenslauf-editor-delete'),
      getButtonSave: page.getByTestId('button-save'),
      getButtonBack: page.getByTestId('button-back'),
    };
  }

  async addAusbildung(item: LebenslaufItem) {
    await this.elements.addAusbildung.click();

    await this.elements.ausbildungsartSelect.click();
    await this.elements.ausbildungsartSelect.selectOption(
      item.bildungsart ?? 'FACHMATURITAET',
    );

    await this.elements.beginn.fill(item.von);
    await this.elements.ende.fill(item.bis);

    await this.elements.wohnsitzSelect.click();
    await this.elements.page.getByTestId(item.wohnsitz).first().click();

    await this.elements.ausbildungAbgeschlossenCheckbox.click();

    await expect(this.elements.form).toHaveClass(/ng-valid/);
    await this.elements.getButtonSave.click();
  }

  async addTaetigkeit(item: LebenslaufItem) {
    await this.elements.addTaetigkeit.click();

    await this.elements.taetigkeitsartSelect.click();
    await this.elements.taetigkeitsartSelect.selectOption(
      item.taetigskeitsart ?? 'ERWERBSTAETIGKEIT',
    );

    await this.elements.taetigkeitsBeschreibung.fill(
      item.taetigkeitsBeschreibung ?? '',
    );

    await this.elements.beginn.fill(item.von);
    await this.elements.ende.fill(item.bis);

    await this.elements.wohnsitzSelect.click();
    await this.elements.page.getByTestId(item.wohnsitz).first().click();

    await expect(this.elements.form).toHaveClass(/ng-valid/);

    await this.elements.getButtonSave.click();
  }
}
