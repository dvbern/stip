import { format, addMonths } from 'date-fns';

const fillEducationForm = () => {
  cy.getBySel('form-education-ausbildungsstaette').click();
  cy.get('mat-option').contains('Universtität Bern').click();

  cy.getBySel('form-education-ausbildungsgang').click();
  cy.get('mat-option').contains('Bachelor').click();

  cy.getBySel('form-education-fachrichtung').type('Informatik');

  const nextMonth = format(addMonths(new Date(), 1), 'MM.yyyy');
  cy.getBySel('form-education-beginn-der-ausbildung').type(nextMonth);

  const inTwoYears = format(addMonths(new Date(), 24), 'MM.yyyy');
  cy.getBySel('form-education-ende-der-ausbildung').type(inTwoYears);

  cy.getBySel('form-education-pensum').click();
  cy.get('mat-option').contains('Vollzeit').click();
};

const getEducationForm = () => {
  return cy.getBySel('form-education-form');
};

export const EducationPO = {
  fillEducationForm,
  getEducationForm,
};
