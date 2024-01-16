import { AddressPO, Adresse } from './adresse.po';

export interface Person {
  sozialversicherungsnummer: string;
  anrede: string;
  nachname: string;
  vorname: string;
  adresse: Adresse;
  identischerZivilrechtlicherWohnsitz: boolean;
  email: string;
  telefonnummer: string;
  geburtsdatum: string;
  nationalitaet: string;
  heimatort?: string;
  zivilstand: string;
  wohnsitz: string;
  quellenbesteuert: boolean;
  sozialhilfebeitraege: boolean;
  digitaleKommunikation: boolean;
  korrespondenzSprache: string;
  iban: string;
  nettoerwerbseinkommen: string;
  fahrkosten: string;
  wohnkosten: string;
  ausbildungskostenSekundarstufeZwei: string;
  auswaertigeMittagessenProWoche: string;
  personenImHaushalt: string;
  zulagen: string;
}

const fillPersonForm = (person: Person) => {
  cy.getBySel('form-person-sozialversicherungsnummer').type(
    person.sozialversicherungsnummer,
  );

  cy.getBySel('form-person-anrede').click();
  cy.get('mat-option').contains('Herr').click();

  cy.getBySel('form-person-nachname').type(person.nachname);
  cy.getBySel('form-person-vorname').type(person.vorname);

  AddressPO.fillAddressForm(person.adresse);

  cy.getBySel('form-person-email').type(person.email);
  cy.getBySel('form-person-telefonnummer').type(person.telefonnummer);
  cy.getBySel('form-person-geburtsdatum').type(person.geburtsdatum);

  cy.getBySel('form-person-nationalitaet').click();
  cy.get('mat-option').contains(person.nationalitaet).click();

  cy.getBySel('form-person-heimatort').type(person.heimatort ?? '');

  cy.getBySel('form-person-zivilstand').click();
  cy.get('mat-option').contains(person.zivilstand).click();

  cy.getBySel('form-person-wohnsitz').click();
  cy.get('mat-option').contains(person.wohnsitz).click();

  cy.getBySel('form-person-quellenbesteuert').within(() => {
    cy.get('mat-radio-button').contains('Nein').click();
  });

  cy.getBySel('form-person-sozialhilfeBeitraege').within(() => {
    cy.get('mat-radio-button').contains('Nein').click();
  });

  cy.getBySel('form-person-korrespondenzSprache')
    .get('mat-radio-button')
    .contains(person.korrespondenzSprache)
    .click();
};

const getPersonForm = () => {
  return cy.getBySel('form-person-form');
};

export const PersonPO = {
  fillPersonForm,
  getPersonForm,
};
