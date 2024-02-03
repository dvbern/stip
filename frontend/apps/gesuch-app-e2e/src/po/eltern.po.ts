import { Locator, Page, expect } from '@playwright/test';

import { Eltern } from '@dv/shared/model/gesuch';

import { AddressPO } from './adresse.po';

export class ElternPO {
  public elems: {
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
    this.elems = {
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
    await this.elems.addVater.click();

    await this.fillElternTeil(item);
  }

  async addMutter(item: Eltern) {
    await this.elems.addMutter.click();

    await this.fillElternTeil(item);
  }

  async fillElternTeil(item: Eltern) {
    await this.elems.sozialversicherungsnummer.fill(
      item.sozialversicherungsnummer,
    );
    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);

    await this.elems.adresse.fillAddressForm(item.adresse);

    await this.elems.identischerZivilrechtlicherWohnsitzCheckbox.click();
    await this.elems.identischerZivilrechtlicherWohnsitzPLZ.fill(
      item.identischerZivilrechtlicherWohnsitzPLZ ?? '',
    );
    await this.elems.identischerZivilrechtlicherWohnsitzOrt.fill(
      item.identischerZivilrechtlicherWohnsitzOrt ?? '',
    );

    await this.elems.geburtsdatum.fill(item.geburtsdatum);
    await this.elems.telefonnummer.fill(item.telefonnummer);

    await this.elems.ausweisbFluechtlingRadio
      .getByTestId(item.ausweisbFluechtling ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elems.ergaenzungsleistungAusbezahltRadio
      .getByTestId(item.ergaenzungsleistungAusbezahlt ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elems.sozialhilfebeitraegeAusbezahltRadio
      .getByTestId(item.sozialhilfebeitraegeAusbezahlt ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await expect(this.elems.form).toHaveClass(/ng-valid/);

    await this.elems.getButtonSave.click();
  }
}
