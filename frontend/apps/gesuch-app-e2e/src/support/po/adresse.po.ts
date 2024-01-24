import { Adresse } from '@dv/shared/model/gesuch';
import { getSelectOption } from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  strasse: () => cy.getBySel('form-address-strasse'),
  hausnummer: () => cy.getBySel('form-address-hausnummer'),
  plz: () => cy.getBySel('form-address-plz'),
  ort: () => cy.getBySel('form-address-ort'),
  coAdresse: () => cy.getBySel('form-address-coAdresse'),
  landSelect: () => cy.getBySel('form-address-land'),
};

const fillAddressForm = (adresse: Adresse) => {
  elements.strasse().clear().type(adresse.strasse);
  elements
    .hausnummer()
    .clear()
    .type(adresse.hausnummer ?? '');

  elements.plz().clear().type(adresse.plz);
  elements.ort().clear().type(adresse.ort);

  // elements.coAdresse().type(adresse.coAdresse ?? '');

  elements.landSelect().click();
  getSelectOption(adresse.land).click();
};

export const AddressPO = {
  elements,
  fillAddressForm,
};
