import { Locator, Page } from '@playwright/test';

import { Partner } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';
import { expectFormToBeValid } from '../helpers/helpers';

export class PartnerPO {
  public elems: {
    page: Page;
    loading: () => Locator;
    form: Locator;
    sozialversicherungsnummer: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    geburtsdatum: Locator;
    ausbildungMitEinkommenOderErwerbstaetigCheckbox: Locator;
    jahreseinkommen: Locator;
    verpflegungskosten: Locator;
    fahrkosten: Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: () => page.getByTestId('form-partner-loading'),
      form: page.getByTestId('form-partner-form'),
      sozialversicherungsnummer: page.getByTestId(
        'form-partner-sozialversicherungsnummer',
      ),
      nachname: page.getByTestId('form-partner-nachname'),
      vorname: page.getByTestId('form-partner-vorname'),

      adresse: new AddressPO(page),

      geburtsdatum: page.getByTestId('form-partner-geburtsdatum'),

      ausbildungMitEinkommenOderErwerbstaetigCheckbox: page.getByTestId(
        'form-partner-ausbildungMitEinkommenOderErwerbstaetig',
      ),
      jahreseinkommen: page.getByTestId('form-partner-jahreseinkommen'),
      verpflegungskosten: page.getByTestId('form-partner-verpflegungskosten'),
      fahrkosten: page.getByTestId('form-partner-fahrkosten'),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillPartnerForm(partner: Partner) {
    await this.elems.sozialversicherungsnummer.fill(
      partner.sozialversicherungsnummer,
    );

    await this.elems.nachname.fill(partner.nachname);
    await this.elems.vorname.fill(partner.vorname);

    await this.elems.adresse.fillAddressForm(partner.adresse);

    await this.elems.geburtsdatum.fill(partner.geburtsdatum);

    await expectFormToBeValid(this.elems.form);
  }
}
