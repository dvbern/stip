export const getSubmitButton = (subject?: HTMLElement) =>
  cy.get('button[type=submit]', { withinSubject: subject });

export const getButtonSaveContinue = () => cy.getBySel('button-save-continue');
export const getButtonContinue = () => cy.getBySel('button-continue');
export const getButtonBack = () => cy.getBySel('button-back');
export const getButtonSave = () => cy.getBySel('button-save');

export const getSelectOption = (name: string) =>
  cy.getBySel('mat-option').contains(name, { matchCase: false });

export const getRadioOption = (
  name: string | boolean,
  subject?: HTMLElement,
) => {
  if (typeof name === 'boolean') {
    if (!subject) {
      throw new Error('Subject argument is required when name is a boolean.');
    }
    return cy
      .get('mat-radio-button', { withinSubject: subject })
      .contains(name ? 'Ja' : 'Nein');
  }
  return cy
    .get('mat-radio-button', { withinSubject: subject })
    .contains(name, { matchCase: false });
};

export const getFormControlFactory = (formControlName: string) => {
  return () => {
    return cy.getBySel(formControlName);
  };
};
