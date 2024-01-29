import { PersonInAusbildung } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';
import { Page, Locator } from 'playwright';

export class PersonPO {
  public elements: {
    page: Page;
    form: Locator;
    sozialversicherungsnummer: Locator;
    anredeSelect: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    identischerZivilrechtlicherWohnsitz: Locator;
    email: Locator;
    telefonnummer: Locator;
    geburtsdatum: Locator;
    nationalitaetSelect: Locator;
    heimatort: Locator;
    vorumundschaftCheckbox: Locator;
    zivilstandSelect: Locator;
    wohnsitzSelect: Locator;
    quellenbesteuertRadio: Locator;
    sozialhilfeBeitraegeRadio: Locator;
    korrespondenzSpracheRadio: Locator;
    digitaleKommunikation: Locator;
    niederlassungsstatusSelect: Locator;
    infoNiederlassungsstatus: Locator;
    loading: () => Locator;
  };

  constructor(page: Page) {
    this.elements = {
      page,
      form: page.getByTestId('form-person-form'),
      sozialversicherungsnummer: page.getByTestId(
        'form-person-sozialversicherungsnummer',
      ),
      anredeSelect: page.getByTestId('form-person-anrede'),
      nachname: page.getByTestId('form-person-nachname'),
      vorname: page.getByTestId('form-person-vorname'),

      adresse: new AddressPO(page),

      identischerZivilrechtlicherWohnsitz: page.getByTestId(
        'form-person-identischerZivilrechtlicherWohnsitz',
      ),
      email: page.getByTestId('form-person-email'),
      telefonnummer: page.getByTestId('form-person-telefonnummer'),
      geburtsdatum: page.getByTestId('form-person-geburtsdatum'),
      nationalitaetSelect: page.getByTestId('form-person-nationalitaet'),
      heimatort: page.getByTestId('form-person-heimatort'),
      vorumundschaftCheckbox: page.getByTestId('form-person-vorumundschaft'),
      zivilstandSelect: page.getByTestId('form-person-zivilstand'),
      wohnsitzSelect: page.getByTestId('form-person-wohnsitz'),
      quellenbesteuertRadio: page.getByTestId('form-person-quellenbesteuert'),
      sozialhilfeBeitraegeRadio: page.getByTestId(
        'form-person-sozialhilfeBeitraege',
      ),
      korrespondenzSpracheRadio: page.getByTestId(
        'form-person-korrespondenzSprache',
      ),
      digitaleKommunikation: page.getByTestId(
        'form-person-digitaleKommunikation',
      ),
      niederlassungsstatusSelect: page.getByTestId(
        'form-person-niederlassungsstatus',
      ),
      infoNiederlassungsstatus: page.getByTestId(
        'info-person-niederlassungsstatus',
      ),

      loading: () => page.getByTestId('form-person-loading'),
    };
  }

  async fillPersonForm(person: PersonInAusbildung) {
    await this.elements.sozialversicherungsnummer.fill(
      person.sozialversicherungsnummer,
    );

    await this.elements.anredeSelect.click();
    await this.elements.page.getByTestId(person.anrede).click();

    await this.elements.nachname.fill(person.nachname);
    await this.elements.vorname.fill(person.vorname);

    await this.elements.adresse.fillAddressForm(person.adresse);

    await this.elements.email.fill(person.email);
    await this.elements.telefonnummer.fill(person.telefonnummer);
    await this.elements.geburtsdatum.fill(person.geburtsdatum);

    await this.elements.nationalitaetSelect.click();
    await this.elements.page.getByTestId(person.nationalitaet).first().click();

    // todo: check if this default works
    await this.elements.heimatort.fill(person.heimatort ?? 'Bern');

    await this.elements.zivilstandSelect.click();
    await this.elements.page.getByTestId(person.zivilstand ?? 'LEDIG').click();

    await this.elements.wohnsitzSelect.click();
    await this.elements.page.getByTestId(person.wohnsitz).click();

    await this.elements.quellenbesteuertRadio
      .getByTestId(person.quellenbesteuert ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elements.sozialhilfeBeitraegeRadio
      .getByTestId(person.sozialhilfebeitraege ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elements.korrespondenzSpracheRadio
      .getByTestId(person.korrespondenzSprache)
      .getByRole('radio')
      .click();
  }
}
