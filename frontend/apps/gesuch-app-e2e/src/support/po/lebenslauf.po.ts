import { LebenslaufItem } from '@dv/shared/model/gesuch';
import { getSelectOption } from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  addAusbildung: () => cy.getBySel('lebenslauf-add-ausbildung'),
  addTaetigkeit: () => cy.getBySel('lebenslauf-add-taetigkeit'),

  form: () => cy.getBySel('form-lebenslauf-form'),

  ausbildungsartSelect: () =>
    cy.getBySel('lebenslauf-editor-ausbildungsart-select'),
  berufsbezeichnung: () => cy.getBySel('lebenslauf-editor-berufsbezeichnung'),
  fachrichtung: () => cy.getBySel('lebenslauf-editor-fachrichtung'),
  titelDesAbschlusses: () =>
    cy.getBySel('lebenslauf-editor-titelDesAbschlusses'),
  taetigkeitsartSelect: () =>
    cy.getBySel('lebenslauf-editor-taetigkeitsart-select'),
  taetigkeitsBeschreibung: () =>
    cy.getBySel('lebenslauf-editor-taetigkeitsBeschreibung'),
  beginn: () => cy.getBySel('lebenslauf-editor-von'),
  ende: () => cy.getBySel('lebenslauf-editor-bis'),
  wohnsitzSelect: () => cy.getBySel('lebenslauf-editor-wohnsitz'),
  ausbildungAbgeschlossenCheckbox: () =>
    cy.getBySel('lebenslauf-editor-ausbildung-abgeschlossen'),

  timelineGap: () => cy.getBySel('timeline-gap-block'),

  loading: () => cy.getBySel('lebenslauf-editor-loading'),
  getButtonDelete: () => cy.getBySel('lebenslauf-editor-delete'),
  getButtonSave: () => cy.getBySel('button-save'),
  getButtonBack: () => cy.getBySel('button-back'),
};

const addAusbildung = (item: LebenslaufItem) => {
  elements.addAusbildung().click();

  elements.ausbildungsartSelect().click();
  getSelectOption(item.bildungsart ?? 'FACHMATURITAET').click();

  elements.beginn().type(item.von);
  elements.ende().type(item.bis);

  elements.wohnsitzSelect().click();
  getSelectOption(item.wohnsitz).click();

  elements.ausbildungAbgeschlossenCheckbox().click();

  elements
    .form()
    .should('have.class', 'ng-valid')
    .then(() => {
      elements.getButtonSave().click();
    });
};

const addTaetigkeit = (item: LebenslaufItem) => {
  elements.addTaetigkeit().click();

  elements.taetigkeitsartSelect().click();
  getSelectOption(item.taetigskeitsart ?? 'ERWERBSTAETIGKEIT').click();

  elements.taetigkeitsBeschreibung().type(item.taetigkeitsBeschreibung ?? '');

  elements.beginn().type(item.von);
  elements.ende().type(item.bis);

  elements.wohnsitzSelect().click();
  getSelectOption(item.wohnsitz).click();

  elements
    .form()
    .should('have.class', 'ng-valid')
    .then(() => {
      elements.getButtonSave().click();
    });
};

export const LebenslaufPO = {
  elements,
  addAusbildung,
  addTaetigkeit,
};
