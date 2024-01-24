import { Kind } from '@dv/shared/model/gesuch';
import {
  getSelectOption,
  getRadioOption,
} from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  loading: () => cy.getBySel('form-kinder-loading'),

  addKind: () => cy.getBySel('button-add-kind'),

  form: () => cy.getBySel('form-kind-form'),

  nachname: () => cy.getBySel('form-kind-nachname'),
  vorname: () => cy.getBySel('form-kind-vorname'),
  geburtsdatum: () => cy.getBySel('form-kind-geburtsdatum'),
  wohnsitzSelect: () => cy.getBySel('form-kind-wohnsitz'),
  ausbildungssituationRadio: () =>
    cy.getBySel('form-kind-ausbildungssituation'),

  getButtonSaveContinue: () => cy.getBySel('button-save-continue'),
  getButtonCancelBack: () => cy.getBySel('button-cancel-back'),
};

const addKind = (item: Kind) => {
  elements.addKind().click();

  elements.nachname().type(item.nachname);
  elements.vorname().type(item.vorname);
  elements.geburtsdatum().type(item.geburtsdatum);

  elements.wohnsitzSelect().click();
  getSelectOption(item.wohnsitz).click();

  elements.ausbildungssituationRadio().within(() => {
    getRadioOption(item.ausbildungssituation).click();
  });

  elements
    .form()
    .should('have.class', 'ng-valid')
    .then(() => {
      elements.getButtonSaveContinue().click();
    });
};

export const KinderPO = {
  elements,
  addKind,
};
