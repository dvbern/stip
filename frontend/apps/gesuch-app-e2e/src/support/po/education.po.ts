import { Ausbildung } from '@dv/shared/model/gesuch';
import {
  getSelectOption,
  getSelectOptionByText,
} from '@dv/shared/util-fn/e2e-helpers';

export interface EducationForm extends Ausbildung {
  ausbildungsstaette: string;
  ausbildungsgang: string;
  // todo: why is this not in the model?
  ausbildungsland: string;
}

const elements = {
  form: () => cy.getBySel('form-education-form'),

  ausbildungslandSelect: () => cy.getBySel('form-education-ausbildungsland'),
  ausbildungsstaetteSelect: () =>
    cy.getBySel('form-education-ausbildungsstaette'),
  alternativeAusbildungsstaette: () =>
    cy.getBySel('form-education-alternativeAusbildungsstaette'),
  ausbildungsgangSelect: () => cy.getBySel('form-education-ausbildungsgang'),
  alternativeAusbildungsgang: () =>
    cy.getBySel('form-education-alternativeAusbildungsgang'),
  fachrichtung: () => cy.getBySel('form-education-fachrichtung'),
  ausbildungBegin: () => cy.getBySel('form-education-beginn-der-ausbildung'),
  ausbildungEnd: () => cy.getBySel('form-education-ende-der-ausbildung'),
  pensumSelect: () => cy.getBySel('form-education-pensum'),

  ausbildungNichtGefundenCheckbox: () =>
    cy.getBySel('form-education-ausbidungNichtGefunden'),

  loading: () => cy.getBySel('education-form-loading'),
};

const fillEducationForm = (ausbildung: EducationForm) => {
  elements.ausbildungslandSelect().click();
  getSelectOption(ausbildung.ausbildungsland).click();

  elements.ausbildungsstaetteSelect().click();
  getSelectOptionByText(ausbildung.ausbildungsstaette).click();

  elements.ausbildungsgangSelect().click();
  getSelectOption(ausbildung.ausbildungsgang).click();

  elements.fachrichtung().type(ausbildung.fachrichtung);

  elements.ausbildungBegin().type(ausbildung.ausbildungBegin);
  elements.ausbildungEnd().type(ausbildung.ausbildungEnd);

  elements.pensumSelect().click();
  getSelectOption(ausbildung.pensum).click();
};

export const EducationPO = {
  elements,
  fillEducationForm,
};
