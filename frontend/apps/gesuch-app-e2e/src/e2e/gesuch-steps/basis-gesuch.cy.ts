import { getStep, getStepTitle } from '@dv/shared/util-fn/e2e-helpers';
import { CockpitPO } from '../../support/po/cockpit.po';
import { format, addMonths } from 'date-fns';

const person = {
  sozialversicherungsnummer: '756.1111.1111.13',
  anrede: 'Herr',
  nachname: 'Muster',
  vorname: 'Max',
  adresse: {
    land: 'Schweiz',
    coAdresse: '',
    strasse: 'Aarbergergasse',
    hausnummer: '5a',
    plz: '3000',
    ort: 'Bern',
  },
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1990',
  nationalitaet: 'Schweiz',
  // niederlassungsstatus: Niederlassungsstatus.C,
  heimatort: 'Bern',
  zivilstand: 'Ledig',
  wohnsitz: 'Eigener Haushalt',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
  digitaleKommunikation: true,
  korrespondenzSprache: 'Deutsch',
};

// const getSozialversicherungsnummer = getFormControlFactory(
//   'form-person-sozialversicherungsnummer',
// );

describe('gesuch-app ein neues gesuch erstellen', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  it('should enter the correct person data', () => {
    // CockpitPO.getGesuchNew().click()
    CockpitPO.getGesuchEdit().first().click();
    getStep('person').click();
    getStepTitle().should('contain.text', 'Person in Ausbildung');

    // for facilitating building the test
    cy.getBySel('form-person-sozialversicherungsnummer').clear();
    cy.getBySel('form-person-nachname').clear();
    cy.getBySel('form-person-vorname').clear();
    cy.getBySel('form-address-strasse').clear();
    cy.getBySel('form-address-hausnummer').clear();
    cy.getBySel('form-address-plz').clear();
    cy.getBySel('form-address-ort').clear();
    cy.getBySel('form-person-email').clear();
    cy.getBySel('form-person-telefonnummer').clear();
    cy.getBySel('form-person-geburtsdatum').clear();
    cy.getBySel('form-person-heimatort').clear();

    cy.getBySel('form-person-sozialversicherungsnummer').should('exist');
    cy.getBySel('form-person-sozialversicherungsnummer').type(
      person.sozialversicherungsnummer,
    );

    cy.getBySel('form-person-anrede').click();
    cy.get('mat-option').contains('Herr').click();

    cy.getBySel('form-person-nachname').type(person.nachname);
    cy.getBySel('form-person-vorname').type(person.vorname);

    cy.getBySel('form-address-strasse').type(person.adresse.strasse);
    cy.getBySel('form-address-hausnummer').type(
      person.adresse.hausnummer ?? '',
    );
    cy.getBySel('form-address-plz').type(person.adresse.plz);
    cy.getBySel('form-address-ort').type(person.adresse.ort);

    cy.getBySel('form-address-land').click();
    cy.get('mat-option').contains(person.adresse.land).click();

    // is checked by default
    // cy.getBySel('form-person-identischerZivilrechtlicherWohnsitz').click();

    cy.getBySel('form-person-email').type(person.email);
    cy.getBySel('form-person-telefonnummer').type(person.telefonnummer);
    cy.getBySel('form-person-geburtsdatum').type(person.geburtsdatum);

    cy.getBySel('form-person-nationalitaet').click();
    cy.get('mat-option').contains(person.nationalitaet).click();

    cy.getBySel('form-person-heimatort').type(person.heimatort ?? '');

    cy.getBySel('form-person-zivilstand').click();
    cy.get('mat-option').contains(person.zivilstand).click();

    cy.getBySel('form-person-wohnsitz').click();
    cy.get('mat-option').contains(person.wohnsitz).click();

    cy.getBySel('form-person-quellenbesteuert').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-sozialhilfeBeitraege').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-korrespondenzSprache')
      .get('mat-radio-button')
      .contains(person.korrespondenzSprache)
      .click();

    cy.getBySel('form-person-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    // Step 2: Ausbildung
    getStepTitle().should('contain.text', 'Ausbildung');

    cy.getBySel('form-education-ausbildungsstaette').click();
    cy.get('mat-option').contains('Universtität Bern').click();

    cy.getBySel('form-education-ausbildungsgang').select('BACHELOR');
    cy.getBySel('form-education-fachrichtung').type('Informatik');

    const nextMonth = format(addMonths(new Date(), 1), 'MM.yyyy');
    cy.getBySel('form-education-beginn-der-ausbildung').type(nextMonth);

    const inTwoYears = format(addMonths(new Date(), 24), 'MM.yyyy');
    cy.getBySel('form-education-ende-der-ausbildung').type(inTwoYears);

    cy.getBySel('form-education-pensum').select('VOLLZEIT');
    cy.getBySel('button-save-continue').click();

    // Step 3: Lebenslauf
  });
});
