import { Locator, Page } from '@playwright/test';

import { Auszahlung } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';

export class AuszahlungPO {
  public elements: {
    loading: () => Locator;
    form: Locator;
    kontoinhaberSelect: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    iban: Locator;
  };

  constructor(page: Page) {
    this.elements = {
      loading: () => page.getByTestId('form-auszahlung-loading'),
      form: page.getByTestId('form-auszahlung-form'),
      kontoinhaberSelect: page.getByTestId('form-auszahlung-kontoinhaber'),
      nachname: page.getByTestId('form-auszahlung-nachname'),
      vorname: page.getByTestId('form-auszahlung-vorname'),

      adresse: new AddressPO(page),

      iban: page.getByTestId('form-auszahlung-iban'),
    };
  }

  async fillAuszahlungEigenesKonto(auszahlung: Auszahlung) {
    await this.elements.kontoinhaberSelect.click();
    await this.elements.kontoinhaberSelect.selectOption(
      auszahlung.kontoinhaber,
    );

    await this.elements.iban.fill(auszahlung.iban);
  }
}
