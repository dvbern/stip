import { Locator, Page } from '@playwright/test';

export class TrancheInfoPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    buttonSaveContinue: Locator;
    buttonNext: Locator;
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
    alertWarning: Locator;
    alertDanger: Locator;
    aenderungReject: Locator;
    aenderungAccept: Locator;
    aenderungManuallyChange: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-tranche-info-loading'),
      form: page.getByTestId('form-tranche-info-form'),
      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
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
      alertWarning: page.getByTestId('alert-warning'),
      alertDanger: page.getByTestId('alert-danger'),
      aenderungReject: page.getByTestId('aenderung-reject'),
      aenderungAccept: page.getByTestId('aenderung-accept'),
      aenderungManuallyChange: page.getByTestId('aenderung-manually-change'),
    };
  }
}
