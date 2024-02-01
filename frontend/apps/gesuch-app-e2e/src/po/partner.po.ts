import { Locator, Page } from '@playwright/test';

import { Partner } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';

export class PartnerPO {
  public elements: {
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
  };

  constructor(page: Page) {
    this.elements = {
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
    };
  }

  async fillPartnerForm(partner: Partner) {
    await this.elements.sozialversicherungsnummer.fill(
      partner.sozialversicherungsnummer,
    );

    await this.elements.nachname.fill(partner.nachname);
    await this.elements.vorname.fill(partner.vorname);

    await this.elements.adresse.fillAddressForm(partner.adresse);

    await this.elements.geburtsdatum.fill(partner.geburtsdatum);
  }
}
