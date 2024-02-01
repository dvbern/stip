import {
  getNavDashboard,
  getStepPersonInAusbildung,
  getStepTitle,
} from '@dv/shared/util-fn/e2e-helpers';

import { CockpitPO } from '../../support/po/cockpit.po';
import { PersonPO } from '../../support/po/person.po';

describe('gesuch-app gesuch form', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  it('should edit and revert person form', () => {
    CockpitPO.openGesuch();
    getStepPersonInAusbildung().click();
    getStepTitle().should('contain.text', 'Person in Ausbildung');
    PersonPO.elements.loading().should('not.exist');

    // Name auslesen
    PersonPO.elements
      .nachname()
      .invoke('val')
      .then((prevName) => {
        // Name updaten
        PersonPO.elements.nachname().focus();
        PersonPO.elements.nachname().clear();
        PersonPO.elements.nachname().type('Updated name');

        // speichern und weiter
        cy.get('form').submit();
        getStepTitle().should('contain.text', 'Ausbildung');

        // zurueck zu Person in Ausbildung
        getNavDashboard().click();
        CockpitPO.getPeriodeTitle().should('exist');
        CockpitPO.getGesuchEdit().first().click();
        getStepPersonInAusbildung().click();
        getStepTitle().should('contain.text', 'Person in Ausbildung');
        PersonPO.elements
          .nachname()
          .invoke('val')
          .then((updatedName) => {
            // CHECK: Name muss geaendert worden sein
            expect(updatedName === 'Updated name');

            // RESET: Name zuruecksetzen
            PersonPO.elements.nachname().focus();
            PersonPO.elements.nachname().clear();
            PersonPO.elements.nachname().type(prevName);
            cy.get('form').submit();
          });
      });
  });
});
