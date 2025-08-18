import { Locator, Page } from '@playwright/test';

import { Adresse } from '@dv/shared/model/gesuch';
import { selectMatOption } from '@dv/shared/util-fn/e2e-util';

export class AddressPO {
  public elems: {
    page: Page;
    strasse: Locator;
    hausnummer: Locator;
    plz: Locator;
    ort: Locator;
    coAdresse: Locator;
    landSelect: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      strasse: page.getByTestId('form-address-strasse'),
      hausnummer: page.getByTestId('form-address-hausnummer'),
      plz: page.getByTestId('form-address-plz'),
      ort: page.getByTestId('form-address-ort'),
      coAdresse: page.getByTestId('form-address-coAdresse'),
      landSelect: page.getByTestId('form-address-land'),
    };
  }

  public async fillAddressForm(adresse: Adresse) {
    await this.elems.strasse.fill(adresse.strasse);
    await this.elems.hausnummer.fill(adresse.hausnummer ?? '');

    await this.elems.plz.fill(adresse.plz);
    await this.elems.ort.fill(adresse.ort);

    await selectMatOption(this.elems.landSelect, adresse.landId);
  }
}
