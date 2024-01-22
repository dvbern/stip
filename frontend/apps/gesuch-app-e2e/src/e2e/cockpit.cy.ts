import { CockpitPO } from '../support/po/cockpit.po';

describe('gesuch-app cockpit', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  it('should redirect to cockpit on startup and render periode', () => {
    cy.url().should('include', 'gesuch-app-feature-cockpit');
    CockpitPO.getPeriodeTitle().should('exist');
    CockpitPO.getPeriodeTitle()
      .first()
      .invoke('text')
      .should('match', new RegExp('(Frühjahr|Herbst)'));
  });
});
