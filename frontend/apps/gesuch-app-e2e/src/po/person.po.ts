import { Locator, Page } from '@playwright/test';

import { PersonInAusbildung } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

import { AddressPO } from './adresse.po';

export class PersonPO {
  public elems: {
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
    wohnsitzMutter: Locator;
    wohnsitzVater: Locator;
    sozialhilfeBeitraegeRadio: Locator;
    korrespondenzSpracheRadio: Locator;
    niederlassungsstatusSelect: Locator;
    infoNiederlassungsstatus: Locator;
    einreisedatum: Locator;

    loading: Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
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
      einreisedatum: page.getByTestId('form-person-einreisedatum'),
      wohnsitzSelect: page.getByTestId('form-person-wohnsitz'),
      wohnsitzMutter: page.getByTestId('component-percentage-splitter-a'),
      wohnsitzVater: page.getByTestId('component-percentage-splitter-b'),
      sozialhilfeBeitraegeRadio: page.getByTestId(
        'form-person-sozialhilfeBeitraege',
      ),
      korrespondenzSpracheRadio: page.getByTestId(
        'form-person-korrespondenzSprache',
      ),
      niederlassungsstatusSelect: page.getByTestId(
        'form-person-niederlassungsstatus',
      ),
      infoNiederlassungsstatus: page.getByTestId(
        'info-person-niederlassungsstatus',
      ),

      loading: page.getByTestId('form-person-loading'),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillPersonForm(person: PersonInAusbildung) {
    await this.elems.sozialversicherungsnummer.fill(
      person.sozialversicherungsnummer,
    );

    await this.elems.anredeSelect.click();
    await this.elems.page.getByTestId(person.anrede).click();

    await this.elems.nachname.fill(person.nachname);
    await this.elems.vorname.fill(person.vorname);

    await this.elems.adresse.fillAddressForm(person.adresse);

    await this.elems.email.fill(person.email);
    await this.elems.telefonnummer.fill(person.telefonnummer);
    await this.elems.geburtsdatum.fill(person.geburtsdatum);

    await this.elems.nationalitaetSelect.click();
    await this.elems.page.getByTestId(person.nationalitaet).first().click();

    await this.elems.heimatort.fill(person.heimatort ?? 'Bern');

    await this.elems.zivilstandSelect.click();
    await this.elems.page.getByTestId(person.zivilstand ?? 'LEDIG').click();

    await this.elems.wohnsitzSelect.click();
    await this.elems.page.getByTestId(person.wohnsitz).click();

    if (person.wohnsitzAnteilMutter) {
      await this.elems.wohnsitzMutter.fill(`${person.wohnsitzAnteilMutter}`);
    }
    if (person.wohnsitzAnteilVater) {
      await this.elems.wohnsitzVater.fill(`${person.wohnsitzAnteilVater}`);
    }

    await selectMatRadio(
      this.elems.sozialhilfeBeitraegeRadio,
      person.sozialhilfebeitraege,
    );

    await selectMatRadio(
      this.elems.korrespondenzSpracheRadio,
      person.korrespondenzSprache,
    );

    await expectFormToBeValid(this.elems.form);
  }
}
