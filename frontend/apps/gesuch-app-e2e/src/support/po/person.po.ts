import { PersonInAusbildung } from '@dv/shared/model/gesuch';
import { AddressPO } from './adresse.po';
import {
  getRadioOption,
  getSelectOption,
} from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  form: () => cy.getBySel('form-person-form'),
  sozialversicherungsnummer: () =>
    cy.getBySel('form-person-sozialversicherungsnummer'),
  anredeSelect: () => cy.getBySel('form-person-anrede'),
  nachname: () => cy.getBySel('form-person-nachname'),
  vorname: () => cy.getBySel('form-person-vorname'),

  adresse: AddressPO,

  identischerZivilrechtlicherWohnsitz: () =>
    cy.getBySel('form-person-identischerZivilrechtlicherWohnsitz'),
  email: () => cy.getBySel('form-person-email'),
  telefonnummer: () => cy.getBySel('form-person-telefonnummer'),
  geburtsdatum: () => cy.getBySel('form-person-geburtsdatum'),
  nationalitaetSelect: () => cy.getBySel('form-person-nationalitaet'),
  heimatort: () => cy.getBySel('form-person-heimatort'),
  vorumundschaftCheckbox: () => cy.getBySel('form-person-vorumundschaft'),
  zivilstandSelect: () => cy.getBySel('form-person-zivilstand'),
  wohnsitzSelect: () => cy.getBySel('form-person-wohnsitz'),
  quellenbesteuertRadio: () => cy.getBySel('form-person-quellenbesteuert'),
  sozialhilfeBeitraegeRadio: () =>
    cy.getBySel('form-person-sozialhilfeBeitraege'),
  korrespondenzSpracheRadio: () =>
    cy.getBySel('form-person-korrespondenzSprache'),
  digitaleKommunikation: () => cy.getBySel('form-person-digitaleKommunikation'),
  niederlassungsstatusSelect: () =>
    cy.getBySel('form-person-niederlassungsstatus'),
  infoNiederlassungsstatus: () =>
    cy.getBySel('info-person-niederlassungsstatus'),

  loading: () => cy.getBySel('form-person-loading'),
};

const fillPersonForm = (person: PersonInAusbildung) => {
  elements.sozialversicherungsnummer().type(person.sozialversicherungsnummer);

  elements.anredeSelect().click();
  getSelectOption(person.anrede).click();

  elements.nachname().type(person.nachname);
  elements.vorname().type(person.vorname);

  AddressPO.fillAddressForm(person.adresse);

  elements.email().type(person.email);
  elements.telefonnummer().type(person.telefonnummer);
  elements.geburtsdatum().type(person.geburtsdatum);

  elements.nationalitaetSelect().click();
  getSelectOption(person.nationalitaet).click();

  // todo: check if this default works
  elements.heimatort().type(person.heimatort ?? 'Bern');

  elements.zivilstandSelect().click();
  getSelectOption(person.zivilstand ?? 'LEDIG').click();

  elements.wohnsitzSelect().click();
  getSelectOption(person.wohnsitz).click();

  // todos: should this be abstracted even more?
  elements.quellenbesteuertRadio().within(() => {
    getRadioOption(person.quellenbesteuert).click();
  });

  elements.sozialhilfeBeitraegeRadio().within(() => {
    getRadioOption(person.sozialhilfebeitraege).click();
  });

  elements.korrespondenzSpracheRadio().within(() => {
    getRadioOption(person.korrespondenzSprache).click();
  });
};

export const PersonPO = {
  elements,
  fillPersonForm,
};
