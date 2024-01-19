import { Auszahlung } from '@dv/shared/model/gesuch';
import { AddressPO } from './adresse.po';
import { getSelectOption } from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  loading: () => cy.getBySel('form-auszahlung-loading'),
  form: () => cy.getBySel('form-auszahlung-form'),

  kontoinhaberSelect: () => cy.getBySel('form-auszahlung-kontoinhaber'),
  nachname: () => cy.getBySel('form-auszahlung-nachname'),
  vorname: () => cy.getBySel('form-auszahlung-vorname'),

  adresse: () => AddressPO,

  iban: () => cy.getBySel('form-auszahlung-iban'),
};

const fillAuszahlungEigenesKonto = (auszahlung: Auszahlung) => {
  elements.kontoinhaberSelect().click();
  getSelectOption(auszahlung.kontoinhaber).click();

  elements.iban().type(auszahlung.iban);
};

export const AuszahlungPO = {
  elements,
  fillAuszahlungEigenesKonto,
};
