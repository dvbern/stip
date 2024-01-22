import { getStepTitle } from '@dv/shared/util-fn/e2e-helpers';
import { PersonPO } from './person.po';

export namespace CockpitPO {
  export const getPeriodeTitle = () => cy.getBySel('cockpit-periode-title');
  export const getGesuchEdit = () => cy.getBySel('cockpit-gesuch-edit');
  export const getGesuchRemove = () => cy.getBySel('cockpit-gesuch-remove');
  export const getNavDashboard = () => cy.getBySel('cockpit-nav-dashboard');
  export const getGesuchNew = () => cy.getBySel('cockpit-gesuch-new');

  export const openGesuch = () => {
    CockpitPO.getGesuchEdit().first().click();
    getStepTitle().should('contain.text', 'Person in Ausbildung');
    PersonPO.elements.nachname().should('exist');
  };
}
