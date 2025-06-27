import { Locator, Page } from '@playwright/test';

import { Zahlungsverbindung } from '@dv/shared/model/gesuch';
import { expectFormToBeValid } from '@dv/shared/util-fn/e2e-util';

import { AddressPO } from './adresse.po';

export class AuszahlungPO {
  public elems: {
    page: Page;
    loading: Locator;
    goToAuszahlungEdit: Locator;
    form: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    iban: Locator;

    buttonSave: Locator;
    buttonBack: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-auszahlung-loading'),
      goToAuszahlungEdit: page.getByTestId('gesuch-form-go-to-auszahlung'),

      form: page.getByTestId('form-auszahlung-form'),
      nachname: page.getByTestId('form-auszahlung-nachname'),
      vorname: page.getByTestId('form-auszahlung-vorname'),

      adresse: new AddressPO(page),

      iban: page.getByTestId('form-auszahlung-iban'),

      buttonSave: page.getByTestId('button-save'),
      buttonBack: page.getByTestId('button-back'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillAuszahlungEigenesKonto(zahlungsverbindung: Zahlungsverbindung) {
    await this.elems.vorname.fill(zahlungsverbindung.vorname);
    await this.elems.nachname.fill(zahlungsverbindung.nachname);
    await this.elems.adresse.fillAddressForm(zahlungsverbindung.adresse);
    await this.elems.iban.fill(zahlungsverbindung.iban);

    await expectFormToBeValid(this.elems.form);
  }
}
