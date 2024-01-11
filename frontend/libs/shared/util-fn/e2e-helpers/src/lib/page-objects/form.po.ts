export const getSubmitButton = (subject?: HTMLElement) =>
  cy.get('button[type=submit]', { withinSubject: subject });

export const getFormControlFactory = (formControlName: string) => {
  return () => {
    return cy.getBySel(formControlName);
  };
};
