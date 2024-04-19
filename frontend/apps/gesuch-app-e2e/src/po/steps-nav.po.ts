import { Locator, Page } from '@playwright/test';

export class StepsNavPO {
  public elems: {
    page: Page;
    person: Locator;
    ausbildung: Locator;
    lebenslauf: Locator;
    familiensituation: Locator;
    eltern: Locator;
    geschwister: Locator;
    partner: Locator;
    kinder: Locator;
    auszahlung: Locator;
    einnahmenKosten: Locator;
    abschluss: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      person: page.getByTestId('step-nav-person'),
      ausbildung: page.getByTestId('step-nav-education'),
      lebenslauf: page.getByTestId('step-nav-lebenslauf'),
      familiensituation: page.getByTestId('step-nav-familiensituation'),
      eltern: page.getByTestId('step-nav-eltern'),
      geschwister: page.getByTestId('step-nav-geschwister'),
      partner: page.getByTestId('step-nav-partner'),
      kinder: page.getByTestId('step-nav-kinder'),
      auszahlung: page.getByTestId('step-nav-auszahlung'),
      einnahmenKosten: page.getByTestId('step-nav-einkommenkosten'),
      abschluss: page.getByTestId('step-nav-abschluss'),
    };
  }
}
