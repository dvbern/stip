export const getSubmitButton = (subject?: HTMLElement) =>
  cy.get('button[type=submit]', { withinSubject: subject });

export const getButtonSaveContinue = () => cy.getBySel('button-save-continue');
export const getButtonContinue = () => cy.getBySel('button-continue');
export const getButtonBack = () => cy.getBySel('button-back');
export const getButtonSave = () => cy.getBySel('button-save');

/**
 * @argument name - The slug of the select option data-testid
 * data-testid="select-option-<name>"
 * data-testids cannot contain spaces
 */
export const getSelectOption = (name: string) => cy.getBySel(name).first();

/**
 * @argument text - The text value of the option, for translated or arbitrary values
 */
export const getSelectOptionByText = (text: string) =>
  cy.get('mat-option').contains(text, { matchCase: false });

export const getRadioOption = (name: string | boolean) => {
  if (typeof name === 'boolean') {
    return cy.getBySel(name ? 'yes' : 'no').find('input');
  }
  return cy.getBySel(name).find('input');
};

export const getFormControlFactory = (formControlName: string) => {
  return () => {
    return cy.getBySel(formControlName);
  };
};
