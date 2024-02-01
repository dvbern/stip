import { Locator, Page, expect } from '@playwright/test';

import { Eltern } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';

export class ElternPO {
  public elements: {
    addVater: Locator;
    addMutter: Locator;
    form: Locator;
    sozialversicherungsnummer: Locator;
    nachname: Locator;
    vorname: Locator;
    adresse: AddressPO;
    identischerZivilrechtlicherWohnsitzCheckbox: Locator;
    identischerZivilrechtlicherWohnsitzPLZ: Locator;
    identischerZivilrechtlicherWohnsitzOrt: Locator;
    geburtsdatum: Locator;
    telefonnummer: Locator;
    ausweisbFluechtlingRadio: Locator;
    ergaenzungsleistungAusbezahltRadio: Locator;
    sozialhilfebeitraegeAusbezahltRadio: Locator;
    loading: () => Locator;
    getButtonDelete: Locator;
    getButtonSave: Locator;
    getButtonBack: Locator;
  };

  constructor(page: Page) {
    this.elements = {
      addVater: page.getByTestId('button-add-vater'),
      addMutter: page.getByTestId('button-add-mutter'),
      form: page.getByTestId('form-eltern-form'),
      sozialversicherungsnummer: page.getByTestId(
        'form-eltern-sozialversicherungsnummer',
      ),
      nachname: page.getByTestId('form-eltern-nachname'),
      vorname: page.getByTestId('form-eltern-vorname'),
      adresse: new AddressPO(page),
      identischerZivilrechtlicherWohnsitzCheckbox: page.getByTestId(
        'form-eltern-identischerZivilrechtlicherWohnsitz',
      ),
      identischerZivilrechtlicherWohnsitzPLZ: page.getByTestId(
        'form-eltern-identischerZivilrechtlicherWohnsitzPLZ',
      ),
      identischerZivilrechtlicherWohnsitzOrt: page.getByTestId(
        'form-eltern-identischerZivilrechtlicherWohnsitzOrt',
      ),
      geburtsdatum: page.getByTestId('form-eltern-geburtsdatum'),
      telefonnummer: page.getByTestId('form-eltern-telefonnummer'),
      ausweisbFluechtlingRadio: page.getByTestId(
        'form-eltern-ausweisFluechtling',
      ),
      ergaenzungsleistungAusbezahltRadio: page.getByTestId(
        'form-eltern-ergaenzungsleistungAusbezahlt',
      ),
      sozialhilfebeitraegeAusbezahltRadio: page.getByTestId(
        'form-eltern-sozialhilfebeitraegeAusbezahlt',
      ),
      loading: () => page.getByTestId('form-eltern-loading'),
      getButtonDelete: page.getByTestId('form-eltern-delete'),
      getButtonSave: page.getByTestId('button-save'),
      getButtonBack: page.getByTestId('button-back'),
    };
  }

  async addVater(item: Eltern) {
    await this.elements.addVater.click();

    await this.fillElternTeil(item);
  }

  async addMutter(item: Eltern) {
    await this.elements.addMutter.click();

    await this.fillElternTeil(item);
  }

  async fillElternTeil(item: Eltern) {
    await this.elements.sozialversicherungsnummer.fill(
      item.sozialversicherungsnummer,
    );
    await this.elements.nachname.fill(item.nachname);
    await this.elements.vorname.fill(item.vorname);

    await this.elements.adresse.fillAddressForm(item.adresse);

    await this.elements.identischerZivilrechtlicherWohnsitzCheckbox.click();
    await this.elements.identischerZivilrechtlicherWohnsitzPLZ.fill(
      item.identischerZivilrechtlicherWohnsitzPLZ ?? '',
    );
    await this.elements.identischerZivilrechtlicherWohnsitzOrt.fill(
      item.identischerZivilrechtlicherWohnsitzOrt ?? '',
    );

    await this.elements.geburtsdatum.fill(item.geburtsdatum);
    await this.elements.telefonnummer.fill(item.telefonnummer);

    await this.elements.ausweisbFluechtlingRadio
      .getByTestId(item.ausweisbFluechtling ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elements.ergaenzungsleistungAusbezahltRadio
      .getByTestId(item.ergaenzungsleistungAusbezahlt ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elements.sozialhilfebeitraegeAusbezahltRadio
      .getByTestId(item.sozialhilfebeitraegeAusbezahlt ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await expect(this.elements.form).toHaveClass(/ng-valid/);

    await this.elements.getButtonSave.click();
  }
}
