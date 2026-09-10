import { Locator, Page } from '@playwright/test';

export class StepsNavPO {
  public elems: {
    page: Page;
    expanderPersoenlichGroup: Locator;
    expanderFamilienGroup: Locator;
    info: Locator;
    ausbildung: Locator;
    person: Locator;
    lebenslauf: Locator;
    familiensituation: Locator;
    eltern: Locator;
    steuererklaerungFamilie: Locator;
    steuererklaerungMutter: Locator;
    steuererklaerungVater: Locator;
    steuerdatenFamilie: Locator;
    steuerdatenMutter: Locator;
    steuerdatenVater: Locator;
    geschwister: Locator;
    partner: Locator;
    kinder: Locator;
    auszahlung: Locator;
    einnahmenKosten: Locator;
    darlehen: Locator;
    dokumente: Locator;
    abschluss: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      expanderPersoenlichGroup: page.getByTestId(
        'step-nav-group-expander-PERSOENLICHE_ANGABEN',
      ),
      expanderFamilienGroup: page.getByTestId(
        'step-nav-group-expander-FAMILIENANGABEN',
      ),
      info: page.getByTestId('step-nav-info'),
      ausbildung: page.getByTestId('step-nav-ausbildung'),
      person: page.getByTestId('step-nav-person'),
      lebenslauf: page.getByTestId('step-nav-lebenslauf'),
      familiensituation: page.getByTestId('step-nav-familiensituation'),
      eltern: page.getByTestId('step-nav-eltern'),
      steuererklaerungFamilie: page.getByTestId(
        'step-nav-eltern-steuererklaerung/FAMILIE',
      ),
      steuererklaerungMutter: page.getByTestId(
        'step-nav-eltern-steuererklaerung/MUTTER',
      ),
      steuererklaerungVater: page.getByTestId(
        'step-nav-eltern-steuererklaerung/VATER',
      ),
      steuerdatenFamilie: page.getByTestId(
        'step-nav-eltern-steuerdaten/FAMILIE',
      ),
      steuerdatenMutter: page.getByTestId('step-nav-eltern-steuerdaten/MUTTER'),
      steuerdatenVater: page.getByTestId('step-nav-eltern-steuerdaten/VATER'),
      geschwister: page.getByTestId('step-nav-geschwister'),
      partner: page.getByTestId('step-nav-partner'),
      kinder: page.getByTestId('step-nav-kinder'),
      auszahlung: page.getByTestId('step-nav-auszahlung'),
      einnahmenKosten: page.getByTestId('step-nav-einnahmenkosten'),
      darlehen: page.getByTestId('step-nav-darlehen'),
      dokumente: page.getByTestId('step-nav-dokumente'),
      abschluss: page.getByTestId('step-nav-abschluss'),
    };
  }
}
