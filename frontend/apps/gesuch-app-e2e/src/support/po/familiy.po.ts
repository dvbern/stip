import { Familiensituation } from '@dv/shared/model/gesuch';
import { getRadioOption } from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  loading: () => cy.getBySel('form-family-loading'),

  form: () => cy.getBySel('form-family-form'),

  elternVerheiratetZusammenRadio: () =>
    cy.getBySel('form-family-elternVerheiratetZusammen'),
  gerichtlicheAlimentenregelungRadio: () =>
    cy.getBySel('form-family-gerichtlicheAlimentenregelung'),
  werZahltAlimenteSelect: () => cy.getBySel('form-family-werZahltAlimente'),
  elternteilUnbekanntVerstorbenRadio: () =>
    cy.getBySel('form-family-elternteilUnbekanntVerstorben'),
  mutterUnbekanntVerstorbenRadio: () =>
    cy.getBySel('form-family-mutterUnbekanntVerstorben'),
  mutterUnbekanntGrundRadio: () =>
    cy.getBySel('form-family-mutterUnbekanntGrund'),
  vaterUnbekanntVerstorbenRadio: () =>
    cy.getBySel('form-family-vaterUnbekanntVerstorben'),
  vaterUnbekanntGrundRadio: () =>
    cy.getBySel('form-family-vaterUnbekanntGrund'),

  mutterWiederVerheiratetRadio: () =>
    cy.getBySel('form-family-mutterWiederVerheiratet'),
  vaterWiederverheiratetRadio: () =>
    cy.getBySel('form-family-vaterWiederverheiratet'),

  sorgerechtSelect: () => cy.getBySel('form-family-sorgerecht'),
  obhutSelect: () => cy.getBySel('form-family-obhut'),

  getStepperButtonNext: () => cy.getBySel('stepper-next'),
  getStepperButtonPrevious: () => cy.getBySel('stepper-previous'),
  getButtonSave: () => cy.getBySel('button-save-continue'),
};

const fillMinimalForm = (item: Familiensituation) => {
  elements.elternVerheiratetZusammenRadio().within(() => {
    getRadioOption(item.elternVerheiratetZusammen).click();
  });
};

export const FamilyPO = {
  elements,
  fillMinimalForm,
};
