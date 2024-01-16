const addAusbildung = () => {
  cy.getBySel('lebenslauf-add-ausbildung').click();
  fillLebenslaufForm();
};

const addTaetigkeit = () => {
  cy.getBySel('lebenslauf-add-taetigkeit').click();
  fillLebenslaufForm();
};

const fillLebenslaufForm = () => {
  cy.getBySel('lebenslauf-editor-ausbildungsart-select').click();
  cy.get('mat-option').contains('Berufsmaturität').click();

  // const fiveYearsAgo = format(addYears(new Date(), -5), 'MM.yyyy');
  cy.getBySel('lebenslauf-editor-beginn').type('08.2020');

  // const threeYearsAgo = format(addYears(new Date(), -3), 'MM.yyyy');
  cy.getBySel('lebenslauf-editor-ende').type('01.2024');

  cy.getBySel('lebenslauf-editor-wohnsitz').click();
  cy.get('mat-option').contains('Bern').click();

  cy.getBySel('lebenslauf-editor-ausbildung-abgeschlossen').click();
};

const getLebenslaufForm = () => {
  return cy.getBySel('form-lebenslauf-form');
};

export const LebenslaufPO = {
  addAusbildung,
  addTaetigkeit,
  getLebenslaufForm,
};
