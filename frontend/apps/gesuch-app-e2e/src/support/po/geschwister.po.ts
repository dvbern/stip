import { Geschwister } from '@dv/shared/model/gesuch';
import {
  getRadioOption,
  getSelectOption,
} from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  loading: () => cy.getBySel('form-geschwister-loading'),
  addGeschwister: () => cy.getBySel('button-add-geschwister'),
  form: () => cy.getBySel('form-geschwister-form'),

  nachname: () => cy.getBySel('form-geschwister-nachname'),
  vorname: () => cy.getBySel('form-geschwister-vorname'),
  geburtsdatum: () => cy.getBySel('form-geschwister-geburtsdatum'),
  wohnsitzSelect: () => cy.getBySel('form-geschwister-wohnsitz'),
  ausbildungssituationRadio: () =>
    cy.getBySel('form-geschwister-ausbildungssituation'),

  getButtonSaveContinue: () => cy.getBySel('button-save-continue'),
  getButtonCancelBack: () => cy.getBySel('button-cancel-back'),
};

const addGeschwister = (item: Geschwister) => {
  elements.addGeschwister().click();

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

export const GeschwisterPO = {
  elements,
  addGeschwister,
};
