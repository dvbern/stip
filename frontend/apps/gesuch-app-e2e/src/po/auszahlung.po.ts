import { Locator, Page } from '@playwright/test';

import { Auszahlung } from '@dv/shared/model/gesuch';
import { expectFormToBeValid } from '@dv/shared/util-fn/e2e-util';

import { AddressPO } from './adresse.po';

export class AuszahlungPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    iban: Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-auszahlung-loading'),
      form: page.getByTestId('form-auszahlung-form'),
      nachname: page.getByTestId('form-auszahlung-nachname'),
      vorname: page.getByTestId('form-auszahlung-vorname'),

      adresse: new AddressPO(page),

      iban: page.getByTestId('form-auszahlung-iban'),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillAuszahlungEigenesKonto(auszahlung: Auszahlung) {
    await this.elems.iban.fill(
      auszahlung.value?.zahlungsverbindung?.iban ?? '',
    );

    await expectFormToBeValid(this.elems.form);
  }
}
