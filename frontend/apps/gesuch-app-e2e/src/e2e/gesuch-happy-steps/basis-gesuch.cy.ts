import { getStepTitle } from '@dv/shared/util-fn/e2e-helpers';
import { CockpitPO } from '../../support/po/cockpit.po';
import { Person, PersonPO } from '../../support/po/person.po';
import { EducationPO } from '../../support/po/education.po';
import { Adresse } from '../../support/po/adresse.po';
// import { Anrede } from '@dv/shared/model/gesuch';
// import { Anrede } from '../../../../../libs/shared/model/gesuch/src/lib/openapi/model/anrede';

const adresse: Adresse = {
  land: 'Schweiz',
  coAdresse: '',
  strasse: 'Aarbergergasse',
  hausnummer: '5a',
  plz: '3000',
  ort: 'Bern',
};

const person: Person = {
  sozialversicherungsnummer: '756.1111.1111.13',
  anrede: 'Herr',
  // anrede: Anrede.HERR,
  nachname: 'Muster',
  vorname: 'Max',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1990',
  nationalitaet: 'Schweiz',
  // niederlassungsstatus: Niederlassungsstatus.C,
  heimatort: 'Bern',
  zivilstand: 'Ledig',
  wohnsitz: 'Familie',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
  digitaleKommunikation: true,
  korrespondenzSprache: 'Deutsch',
  iban: 'CH9300762011623852957',
  nettoerwerbseinkommen: '15000',
  fahrkosten: '100',
  wohnkosten: '1000',
  ausbildungskostenSekundarstufeZwei: '2000',
  auswaertigeMittagessenProWoche: '5',
  personenImHaushalt: '3',
  zulagen: '250',
};

const vater = {
  sozialversicherungsnummer: '756.2222.2222.24',
  nachname: 'Muster',
  vorname: 'Maximilian',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1969',
  ausweisbFluechtling: false,
  ergaenzungsleistungAusbezahlt: false,
  sozialhilfebeitraegeAusbezahlt: false,
};

const mutter = {
  sozialversicherungsnummer: '756.3333.3333.35',
  nachname: 'Muster',
  vorname: 'Maxine',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1968',
  ausweisbFluechtling: false,
  ergaenzungsleistungAusbezahlt: false,
  sozialhilfebeitraegeAusbezahlt: false,
};

const bruder = {
  nachname: 'Muster',
  vorname: 'Simon',
  geburtsdatum: '25.12.2005',
  wohnsitz: 'Eigener Haushalt',
  ausbildungssituation: 'In Ausbildung',
};

const kind = {
  nachname: 'Muster',
  vorname: 'Sara',
  geburtsdatum: '25.12.2018',
  wohnsitz: 'Eigener Haushalt',
  ausbildungssituation: 'Schulpflichtig',
};

describe('gesuch-app: Neues gesuch erstellen - Person', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  it('should enter the correct person data', () => {
    CockpitPO.getGesuchNew().click();

    // cy.get('[data-testid=cockpit-gesuch-new]', { timeout: 20000 }).click();

    cy.getBySel('form-person-loading').should('not.exist');

    // Step 1: Person
    getStepTitle().should('contain.text', 'Person in Ausbildung');

    PersonPO.fillPersonForm(person);

    cy.getBySel('form-person-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    // Step 2: Ausbildung
    getStepTitle().should('contain.text', 'Ausbildung');

    // clear the the test for development ease
    EducationPO.fillEducationForm();

    cy.getBySel('form-education-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    // Step 3: Lebenslauf
    getStepTitle().should('contain.text', 'Lebenslauf');

    cy.getBySel('lebenslauf-add-ausbildung').click();

    cy.getBySel('lebenslauf-editor-ausbildungsart-select').click();
    cy.get('mat-option').contains('Berufsmaturität').click();

    // const fiveYearsAgo = format(addYears(new Date(), -5), 'MM.yyyy');
    cy.getBySel('lebenslauf-editor-beginn').type('08.2020');

    // const threeYearsAgo = format(addYears(new Date(), -3), 'MM.yyyy');
    cy.getBySel('lebenslauf-editor-ende').type('01.2024');

    cy.getBySel('lebenslauf-editor-wohnsitz').click();
    cy.get('mat-option').contains('Bern').click();

    cy.getBySel('lebenslauf-editor-ausbildung-abgeschlossen').click();

    cy.getBySel('form-lebenslauf-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save').click();
      });

    //  Step 3: lebenslauf Vorlehere
    cy.getBySel('lebenslauf-add-ausbildung').click();

    cy.getBySel('lebenslauf-editor-ausbildungsart-select').click();
    cy.get('mat-option').contains('Vorlehre').click();

    // const fiveYearsAgo = format(addYears(new Date(), -5), 'MM.yyyy');
    cy.getBySel('lebenslauf-editor-beginn').type('8.2006');

    // const threeYearsAgo = format(addYears(new Date(), -3), 'MM.yyyy');
    cy.getBySel('lebenslauf-editor-ende').type('12.2019');

    cy.getBySel('lebenslauf-editor-wohnsitz').click();
    cy.get('mat-option').contains('Bern').click();

    cy.getBySel('lebenslauf-editor-ausbildung-abgeschlossen').click();

    cy.getBySel('form-lebenslauf-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save').click();
      });

    // Step 3: Tätigkeit
    cy.getBySel('lebenslauf-add-taetigkeit').click();

    cy.getBySel('lebenslauf-editor-taetigkeitsart-select').click();
    cy.get('mat-option').contains('Erwerbstätigkeit').click();

    cy.getBySel('lebenslauf-editor-taetigkeits-beschreibung').type(
      'Serviceangestellter',
    );

    cy.getBySel('lebenslauf-editor-beginn').type('7.2019');

    cy.getBySel('lebenslauf-editor-ende').type('7.2020');

    cy.getBySel('lebenslauf-editor-wohnsitz').click();
    cy.get('mat-option').contains('Bern').click();

    cy.getBySel('form-lebenslauf-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save').click();
      });

    cy.getBySel('button-continue').click();

    // Step 4: Familiensituation
    getStepTitle().should('contain.text', 'Familiensituation');

    cy.getBySel('form-family-elternVerheiratetZusammen').within(() => {
      cy.get('mat-radio-button').contains('Ja').click();
    });

    cy.getBySel('form-family-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    // Step 5: Eltern
    getStepTitle().should('contain.text', 'Eltern');

    cy.getBySel('button-add-vater').click();

    cy.getBySel('form-person-sozialversicherungsnummer').should('exist');
    cy.getBySel('form-person-sozialversicherungsnummer').type(
      vater.sozialversicherungsnummer,
    );

    cy.getBySel('form-person-nachname').type(vater.nachname);
    cy.getBySel('form-person-vorname').type(vater.vorname);

    cy.getBySel('form-address-strasse').type(vater.adresse.strasse);
    cy.getBySel('form-address-hausnummer').type(vater.adresse.hausnummer ?? '');
    cy.getBySel('form-address-plz').type(vater.adresse.plz);
    cy.getBySel('form-address-ort').type(vater.adresse.ort);

    cy.getBySel('form-address-land').click();
    cy.get('mat-option').contains(vater.adresse.land).click();

    cy.getBySel('form-person-geburtsdatum').type(vater.geburtsdatum);
    cy.getBySel('form-person-telefonnummer').type(vater.telefonnummer);

    cy.getBySel('form-person-ausweisFluechtling').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-ergaenzungsleistungAusbezahlt').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-sozialhilfebeitraegeAusbezahlt').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-eltern-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    cy.getBySel('button-add-mutter').click();

    cy.getBySel('form-person-sozialversicherungsnummer').should('exist');
    cy.getBySel('form-person-sozialversicherungsnummer').type(
      mutter.sozialversicherungsnummer,
    );

    cy.getBySel('form-person-nachname').type(mutter.nachname);
    cy.getBySel('form-person-vorname').type(mutter.vorname);

    cy.getBySel('form-address-strasse').type(mutter.adresse.strasse);
    cy.getBySel('form-address-hausnummer').type(
      mutter.adresse.hausnummer ?? '',
    );
    cy.getBySel('form-address-plz').type(mutter.adresse.plz);
    cy.getBySel('form-address-ort').type(mutter.adresse.ort);

    cy.getBySel('form-address-land').click();
    cy.get('mat-option').contains(mutter.adresse.land).click();

    cy.getBySel('form-person-geburtsdatum').type(mutter.geburtsdatum);
    cy.getBySel('form-person-telefonnummer').type(mutter.telefonnummer);

    cy.getBySel('form-person-ausweisFluechtling').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-ergaenzungsleistungAusbezahlt').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-person-sozialhilfebeitraegeAusbezahlt').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-eltern-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    cy.getBySel('button-continue').click();

    cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    // Step 6: Geschwister
    cy.getBySel('form-geschwister-loading').should('not.exist');

    getStepTitle().should('contain.text', 'Geschwister');

    cy.getBySel('button-add-geschwister').click();

    cy.getBySel('form-person-nachname').type(bruder.nachname);
    cy.getBySel('form-person-vorname').type(bruder.vorname);

    cy.getBySel('form-person-geburtsdatum').type(bruder.geburtsdatum);

    cy.getBySel('form-person-wohnsitz').click();
    cy.get('mat-option').contains(bruder.wohnsitz).click();

    cy.getBySel('form-person-ausbildungssituation').within(() => {
      cy.get('mat-radio-button').contains(bruder.ausbildungssituation).click();
    });

    cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.getBySel('form-geschwister-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    cy.wait('@patchResource');
    cy.wait('@getResource');

    // cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.wait(1000);

    cy.getBySel('button-continue').click();

    // Step 7: Ehe und Konkubinatspartner
    // cy.wait('@getResource');

    getStepTitle().should('contain.text', 'Kinder');

    // step 8: Kinder
    cy.getBySel('form-kinder-loading').should('not.exist');

    cy.getBySel('button-add-kind').click();

    cy.getBySel('form-person-nachname').should('exist');
    cy.getBySel('form-person-nachname').type(kind.nachname);
    cy.getBySel('form-person-vorname').type(kind.vorname);

    cy.getBySel('form-person-geburtsdatum').type(kind.geburtsdatum);

    cy.getBySel('form-person-wohnsitz').click();
    cy.get('mat-option').contains(kind.wohnsitz).click();

    cy.getBySel('form-person-ausbildungssituation').within(() => {
      cy.get('mat-radio-button').contains(kind.ausbildungssituation).click();
    });

    cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.getBySel('form-kind-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    cy.wait('@patchResource');
    cy.wait('@getResource');

    // cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.wait(1000);
    cy.getBySel('button-continue').click();

    // cy.wait('@getResource');

    getStepTitle().should('contain.text', 'Auszahlung');

    cy.getBySel('form-auszahlung-kontoinhaber').click();
    cy.get('mat-option').contains('Gesuchsteller:in').click();

    cy.getBySel('form-auszahlung-iban').type(person.iban);

    cy.getBySel('form-auszahlung-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    // Step 10: Einnahmen und Kosten
    getStepTitle().should('contain.text', 'Einnahmen & Kosten');

    cy.getBySel('form-einnahmen-kosten-nettoerwerbseinkommen').type(
      person.nettoerwerbseinkommen,
    );

    cy.getBySel('form-einnahmen-kosten-zulagen').type(person.zulagen);

    cy.getBySel(
      'form-einnahmen-kosten-ausbildungskostenSekundarstufeZwei',
    ).type(person.ausbildungskostenSekundarstufeZwei);

    cy.getBySel('form-einnahmen-kosten-fahrkosten').type(person.fahrkosten);

    cy.getBySel('form-einnahmen-kosten-wohnkosten').type(person.wohnkosten);

    cy.getBySel('form-einnahmen-kosten-auswaertigeMittagessenProWoche').type(
      person.auswaertigeMittagessenProWoche,
    );

    cy.getBySel('form-einnahmen-kosten-personenImHaushalt').type(
      person.personenImHaushalt,
    );

    cy.getBySel('form-einnahmen-kosten-verdienstRealisiert').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.getBySel('form-einnahmen-kosten-willDarlehen').within(() => {
      cy.get('mat-radio-button').contains('Nein').click();
    });

    cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.getBySel('form-einnahmen-kosten-form')
      .should('have.class', 'ng-valid')
      .then(() => {
        cy.getBySel('button-save-continue').click();
      });

    cy.wait('@patchResource');
    cy.wait('@getResource');

    // Step 11: Freigabe
    getStepTitle().should('contain.text', 'Freigabe');

    cy.getBySel('button-abschluss').click();
  });
});
