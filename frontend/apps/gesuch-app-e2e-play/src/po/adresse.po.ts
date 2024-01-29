import { Page, Locator } from 'playwright';
import { Adresse } from '@dv/shared/model/gesuch';

export class AddressPO {
  public elements: {
    page: Page;
    strasse: Locator;
    hausnummer: Locator;
    plz: Locator;
    ort: Locator;
    coAdresse: Locator;
    landSelect: Locator;
  };

  constructor(page: Page) {
    this.elements = {
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
    await this.elements.strasse.fill(adresse.strasse);
    await this.elements.hausnummer.fill(adresse.hausnummer ?? '');

    await this.elements.plz.fill(adresse.plz);
    await this.elements.ort.fill(adresse.ort);

    // await this.elements.coAdresse().type(adresse.coAdresse ?? '');

    await this.elements.landSelect.click();
    await this.elements.page.getByTestId(adresse.land).first().click();
  }
}
