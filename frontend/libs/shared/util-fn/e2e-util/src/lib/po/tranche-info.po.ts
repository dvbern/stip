import { Locator, Page } from '@playwright/test';

export class TrancheInfoPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    buttonContinue: Locator;
    title: Locator;
    status: Locator;
    pia: Locator;
    gesuchsnummer: Locator;
    fallnummer: Locator;
    einreichedatum: Locator;
    gesuchsperiode: Locator;
    einreichefrist: Locator;
    von: Locator;
    bis: Locator;
    bemerkung: Locator;
    sachbearbeiter: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-tranche-loading'),
      form: page.getByTestId('form-tranche-form'),
      buttonContinue: page.getByTestId('button-continue'),
      title: page.getByTestId('dynamic-tranche-step-title'),
      status: page.getByTestId('form-tranche-status'),
      pia: page.getByTestId('form-tranche-pia'),
      gesuchsnummer: page.getByTestId('form-tranche-gesuchsnummer'),
      fallnummer: page.getByTestId('form-tranche-fallnummer'),
      einreichedatum: page.getByTestId('form-tranche-einreichedatum'),
      gesuchsperiode: page.getByTestId('form-tranche-gesuchsperiode'),
      einreichefrist: page.getByTestId('form-tranche-einreichefrist'),
      von: page.getByTestId('form-tranche-von'),
      bis: page.getByTestId('form-tranche-bis'),
      bemerkung: page.getByTestId('form-tranche-bemerkung'),
      sachbearbeiter: page.getByTestId('form-tranche-sachbearbeiter'),
    };
  }
}
