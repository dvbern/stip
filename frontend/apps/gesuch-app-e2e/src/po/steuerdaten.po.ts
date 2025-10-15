import { Locator, Page } from '@playwright/test';

import { Steuerdaten } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class SteuerdatenPO {
  public elems: {
    page: Page;
    form: Locator;

    totalEinkuenfte: Locator;
    eigenmietwert: Locator;
    arbeitsverhaeltnis: Locator;
    saeule3a: Locator;
    saeule2: Locator;
    vermoegen: Locator;
    steuernBund: Locator;
    steuernKantonGemeinde: Locator;
    fahrkosten: Locator;
    fahrkostenPartner: Locator;
    verpflegung: Locator;
    verpflegungPartner: Locator;
    steuerjahr: Locator;
    veranlagungsStatus: Locator;

    loading: Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      form: page.getByTestId('form-eltern-steuerdaten-form'),

      totalEinkuenfte: page.getByTestId(
        'form-eltern-steuerdaten.totalEinkuenfte',
      ),
      eigenmietwert: page.getByTestId('form-eltern-steuerdaten.eigenmietwert'),
      arbeitsverhaeltnis: page.getByTestId(
        'form-eltern-steuerdaten-arbeitsverhaeltnis',
      ),
      saeule3a: page.getByTestId('form-eltern-steuerdaten.saeule3a'),
      saeule2: page.getByTestId('form-eltern-steuerdaten.saeule2'),

      vermoegen: page.getByTestId('form-eltern-steuerdaten.vermoegen'),
      steuernKantonGemeinde: page.getByTestId(
        'form-eltern-steuerdaten.steuernKantonGemeinde',
      ),
      steuernBund: page.getByTestId('form-eltern-steuerdaten.steuernBund'),
      fahrkosten: page.getByTestId('form-eltern-steuerdaten.fahrkosten'),
      fahrkostenPartner: page.getByTestId(
        'form-eltern-steuerdaten.fahrkostenPartner',
      ),
      verpflegung: page.getByTestId('form-eltern-steuerdaten.verpflegung'),
      verpflegungPartner: page.getByTestId(
        'form-eltern-steuerdaten.verpflegungPartner',
      ),
      steuerjahr: page.getByTestId('form-eltern-steuerdaten.steuerjahr'),
      veranlagungsStatus: page.getByTestId(
        'form-eltern-steuerdaten.veranlagungsStatus',
      ),

      loading: page.getByTestId('form-eltern-steuerdaten-loading'),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillSteuerdaten(item: Steuerdaten) {
    await this.elems.totalEinkuenfte.fill(`${item.totalEinkuenfte}`);
    await this.elems.eigenmietwert.fill(`${item.eigenmietwert}`);
    await selectMatRadio(
      this.elems.arbeitsverhaeltnis,
      item.isArbeitsverhaeltnisSelbstaendig ? 'selbstaendig' : 'unselbstaendig',
    );

    if (item.isArbeitsverhaeltnisSelbstaendig) {
      await this.elems.saeule3a.fill(`${item.saeule3a}`);
      await this.elems.saeule2.fill(`${item.saeule2}`);
    }

    await this.elems.vermoegen.fill(`${item.vermoegen}`);
    await this.elems.steuernKantonGemeinde.fill(
      `${item.steuernKantonGemeinde}`,
    );
    await this.elems.steuernBund.fill(`${item.steuernBund}`);
    await this.elems.fahrkosten.fill(`${item.fahrkosten}`);
    await this.elems.fahrkostenPartner.fill(`${item.fahrkostenPartner}`);
    await this.elems.verpflegung.fill(`${item.verpflegung}`);
    await this.elems.verpflegungPartner.fill(`${item.verpflegungPartner}`);
    await this.elems.steuerjahr.fill(`${item.steuerjahr}`);
    await this.elems.veranlagungsStatus.fill(`${item.veranlagungsStatus}`);

    await expectFormToBeValid(this.elems.form);
  }
}
