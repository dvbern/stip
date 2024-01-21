import { Eltern } from '@dv/shared/model/gesuch';
import { getRadioOption } from '@dv/shared/util-fn/e2e-helpers';
import { AddressPO } from './adresse.po';

const elements = {
  addVater: () => cy.getBySel('button-add-vater'),
  addMutter: () => cy.getBySel('button-add-mutter'),

  form: () => cy.getBySel('form-eltern-form'),

  sozialversicherungsnummer: () =>
    cy.getBySel('form-eltern-sozialversicherungsnummer'),
  nachname: () => cy.getBySel('form-eltern-nachname'),
  vorname: () => cy.getBySel('form-eltern-vorname'),

  adresse: AddressPO,

  identischerZivilrechtlicherWohnsitzCheckbox: () =>
    cy.getBySel('form-eltern-identischerZivilrechtlicherWohnsitz'),
  identischerZivilrechtlicherWohnsitzPLZ: () =>
    cy.getBySel('form-eltern-identischerZivilrechtlicherWohnsitzPLZ'),
  identischerZivilrechtlicherWohnsitzOrt: () =>
    cy.getBySel('form-eltern-identischerZivilrechtlicherWohnsitzOrt'),

  geburtsdatum: () => cy.getBySel('form-eltern-geburtsdatum'),
  telefonnummer: () => cy.getBySel('form-eltern-telefonnummer'),

  ausweisbFluechtlingRadio: () => cy.getBySel('form-eltern-ausweisFluechtling'),
  ergaenzungsleistungAusbezahltRadio: () =>
    cy.getBySel('form-eltern-ergaenzungsleistungAusbezahlt'),
  sozialhilfebeitraegeAusbezahltRadio: () =>
    cy.getBySel('form-eltern-sozialhilfebeitraegeAusbezahlt'),

  loading: () => cy.getBySel('form-eltern-loading'),
  getButtonDelete: () => cy.getBySel('form-eltern-delete'),
  getButtonSave: () => cy.getBySel('button-save'),
  getButtonBack: () => cy.getBySel('button-back'),
};

const addVater = (item: Eltern) => {
  elements.addVater().click();

  addElternTeil(item);
};

const addMutter = (item: Eltern) => {
  elements.addMutter().click();

  addElternTeil(item);
};

const addElternTeil = (item: Eltern) => {
  elements.sozialversicherungsnummer().type(item.sozialversicherungsnummer);
  elements.nachname().type(item.nachname);
  elements.vorname().type(item.vorname);

  AddressPO.fillAddressForm(item.adresse);

  // elements.identischerZivilrechtlicherWohnsitzCheckbox().click();
  // elements
  //   .identischerZivilrechtlicherWohnsitzPLZ()
  //   .type(item.identischerZivilrechtlicherWohnsitzPLZ ?? '');
  // elements
  //   .identischerZivilrechtlicherWohnsitzOrt()
  //   .type(item.identischerZivilrechtlicherWohnsitzOrt ?? '');

  elements.geburtsdatum().type(item.geburtsdatum);
  elements.telefonnummer().type(item.telefonnummer);

  elements.ausweisbFluechtlingRadio().within(() => {
    getRadioOption(item.ausweisbFluechtling).click();
  });
  elements.ergaenzungsleistungAusbezahltRadio().within(() => {
    getRadioOption(item.ergaenzungsleistungAusbezahlt).click();
  });
  elements.sozialhilfebeitraegeAusbezahltRadio().within(() => {
    getRadioOption(item.sozialhilfebeitraegeAusbezahlt).click();
  });

  elements
    .form()
    .should('have.class', 'ng-valid')
    .then(() => {
      elements.getButtonSave().click();
    });
};

export const ElternPO = {
  elements,
  addVater,
  addMutter,
};
