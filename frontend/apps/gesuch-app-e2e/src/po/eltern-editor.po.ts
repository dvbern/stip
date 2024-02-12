import { Locator, Page } from '@playwright/test';

import { Eltern } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

import { AddressPO } from './adresse.po';

export class ElternEditorPO {
  public elems: {
    page: Page;
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

    buttonSave: Locator;
    buttonCancel: Locator;
    buttonDelete: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
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

      buttonSave: page.getByTestId('button-save'),
      buttonCancel: page.getByTestId('button-cancel'),
      buttonDelete: page.getByTestId('button-delete'),
    };
  }

  async fillElternTeilH(item: Eltern) {
    await this.elems.sozialversicherungsnummer.fill(
      item.sozialversicherungsnummer,
    );
    await this.elems.nachname.fill(item.nachname);
    await this.elems.vorname.fill(item.vorname);

    await this.elems.adresse.fillAddressForm(item.adresse);

    await this.elems.geburtsdatum.fill(item.geburtsdatum);
    await this.elems.telefonnummer.fill(item.telefonnummer);

    await selectMatRadio(
      this.elems.ausweisbFluechtlingRadio,
      item.ausweisbFluechtling,
    );

    await selectMatRadio(
      this.elems.ergaenzungsleistungAusbezahltRadio,
      item.ergaenzungsleistungAusbezahlt,
    );

    await selectMatRadio(
      this.elems.sozialhilfebeitraegeAusbezahltRadio,
      item.sozialhilfebeitraegeAusbezahlt,
    );

    await expectFormToBeValid(this.elems.form);

    await this.elems.buttonSave.click();
  }
}
