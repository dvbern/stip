import { Partner } from '@dv/shared/model/gesuch';
import { AddressPO } from './adresse.po';

export const elements = {
  loading: () => cy.getBySel('form-partner-loading'),

  form: () => cy.getBySel('form-partner-form'),
  sozialversicherungsnummer: () =>
    cy.getBySel('form-partner-sozialversicherungsnummer'),
  nachname: () => cy.getBySel('form-partner-nachname'),
  vorname: () => cy.getBySel('form-partner-vorname'),

  adresse: AddressPO,

  geburtsdatum: () => cy.getBySel('form-partner-geburtsdatum'),

  ausbildungMitEinkommenOderErwerbstaetigCheckbox: () =>
    cy.getBySel('form-partner-ausbildungMitEinkommenOderErwerbstaetig'),
  jahreseinkommen: () => cy.getBySel('form-partner-jahreseinkommen'),
  verpflegungskosten: () => cy.getBySel('form-partner-verpflegungskosten'),
  fahrkosten: () => cy.getBySel('form-partner-fahrkosten'),
};

const fillPartnerForm = (partner: Partner) => {
  elements.sozialversicherungsnummer().type(partner.sozialversicherungsnummer);

  elements.nachname().type(partner.nachname);
  elements.vorname().type(partner.vorname);

  AddressPO.fillAddressForm(partner.adresse);

  elements.geburtsdatum().type(partner.geburtsdatum);
};

export const PartnerPO = {
  elements,
  fillPartnerForm,
};
