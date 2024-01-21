/* eslint-disable @nx/enforce-module-boundaries */
import {
  getButtonContinue,
  getButtonSaveContinue,
  getStepTitle,
} from '@dv/shared/util-fn/e2e-helpers';
import { CockpitPO } from '../../support/po/cockpit.po';
import { PersonPO } from '../../support/po/person.po';
import {
  EducationForm as EducationValues,
  EducationPO,
} from '../../support/po/education.po';

import { addMonths, format } from 'date-fns';
import {
  Adresse,
  Auszahlung,
  EinnahmenKosten,
  Eltern,
  Familiensituation,
  Geschwister,
  Kind,
  LebenslaufItem,
  PersonInAusbildung,
} from '@dv/shared/model/gesuch';
import { LebenslaufPO } from '../../support/po/lebenslauf.po';
import { ElternPO } from '../../support/po/eltern.po';
import { FamilyPO } from '../../support/po/familiy.po';
import { GeschwisterPO } from '../../support/po/geschwister.po';
import { KinderPO } from '../../support/po/kinder.po';
import { AuszahlungPO } from '../../support/po/auszahlung.po';
import { EinnahmenKostenPO } from '../../support/po/einnahmen-kosten.po';

const adresse: Adresse = {
  land: 'CH',
  coAdresse: '',
  strasse: 'Aarbergergasse',
  hausnummer: '5a',
  plz: '3000',
  ort: 'Bern',
};

const person: PersonInAusbildung = {
  sozialversicherungsnummer: '756.1111.1111.13',
  anrede: 'HERR',
  nachname: 'Muster',
  vorname: 'Max',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1990',
  nationalitaet: 'CH',
  // niederlassungsstatus: Niederlassungsstatus.C,
  heimatort: 'Bern',
  zivilstand: 'LEDIG',
  wohnsitz: 'FAMILIE',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
  digitaleKommunikation: true,
  korrespondenzSprache: 'DEUTSCH',
};

const nextMonth = format(addMonths(new Date(), 1), 'MM.yyyy');
const inTwoYears = format(addMonths(new Date(), 24), 'MM.yyyy');

// todo: fix typo
const education: EducationValues = {
  ausbildungsland: 'CH',
  ausbildungsstaette: 'Universtität Bern',
  ausbildungsgang: 'Bachelor',
  fachrichtung: 'Informatik',
  ausbildungBegin: nextMonth,
  ausbildungEnd: inTwoYears,
  pensum: 'VOLLZEIT',
};

const ausbildung1: LebenslaufItem = {
  bildungsart: 'VORLEHRE',
  von: '08.2006',
  bis: '12.2019',
  wohnsitz: 'BE',
  ausbildungAbgeschlossen: true,
  id: '',
};

const ausbildung2: LebenslaufItem = {
  bildungsart: 'FACHMATURITAET',
  von: '08.2020',
  bis: '01.2024',
  wohnsitz: 'BE',
  ausbildungAbgeschlossen: false,
  id: '',
};

const taetigkeit: LebenslaufItem = {
  taetigskeitsart: 'ERWERBSTAETIGKEIT',
  taetigkeitsBeschreibung: 'Serviceangestellter',
  von: '07.2019',
  bis: '07.2020',
  wohnsitz: 'BE',
  id: '',
};

const auszahlung: Auszahlung = {
  vorname: '',
  adresse,
  iban: 'CH9300762011623852957',
  nachname: '',
  kontoinhaber: 'GESUCHSTELLER',
};

const einnahmenKosten: EinnahmenKosten = {
  nettoerwerbseinkommen: 15000,
  fahrkosten: 100,
  verdienstRealisiert: false,
  wohnkosten: 1000,
  ausbildungskostenSekundarstufeZwei: 1000,
  zulagen: 250,
  personenImHaushalt: 3,
  auswaertigeMittagessenProWoche: 5,
};

const vater: Eltern = {
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
  id: '',
  elternTyp: 'VATER',
};

const mutter: Eltern = {
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
  id: '',
  elternTyp: 'VATER',
};

const bruder: Geschwister = {
  nachname: 'Muster',
  vorname: 'Simon',
  geburtsdatum: '25.12.2005',
  wohnsitz: 'FAMILIE',
  ausbildungssituation: 'VORSCHULPFLICHTIG',
  id: '',
};

const kind: Kind = {
  nachname: 'Muster',
  vorname: 'Sara',
  geburtsdatum: '25.12.2018',
  wohnsitz: 'FAMILIE',
  ausbildungssituation: 'VORSCHULPFLICHTIG',
  id: '',
};

const familienlsituation: Familiensituation = {
  elternVerheiratetZusammen: true,
};

describe('gesuch-app: Neues gesuch erstellen - Person', () => {
  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  it('should enter the correct person data', () => {
    CockpitPO.getGesuchNew().click();

    // Step 1: Person ============================================================
    getStepTitle().should('contain.text', 'Person in Ausbildung');
    PersonPO.elements.loading().should('not.exist');

    PersonPO.fillPersonForm(person);

    PersonPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    // Step 2: Ausbildung ========================================================

    getStepTitle().should('contain.text', 'Ausbildung');

    EducationPO.fillEducationForm(education);

    EducationPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    // Step 3: Lebenslauf ========================================================
    getStepTitle().should('contain.text', 'Lebenslauf');

    LebenslaufPO.elements.loading().should('not.exist');

    LebenslaufPO.addAusbildung(ausbildung1);

    LebenslaufPO.addAusbildung(ausbildung2);

    LebenslaufPO.addTaetigkeit(taetigkeit);

    LebenslaufPO.elements.loading().should('not.exist');

    getButtonContinue().click();

    // Step 4: Familiensituation  =================================================
    getStepTitle().should('contain.text', 'Familiensituation');

    FamilyPO.fillMinimalForm(familienlsituation);

    FamilyPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    // Step 5: Eltern =============================================================
    ElternPO.elements.loading().should('not.exist');

    ElternPO.addVater(vater);

    ElternPO.addMutter(mutter);

    ElternPO.elements.loading().should('not.exist');

    getButtonContinue().click();

    cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    // Step 6: Geschwister  ========================================================
    getStepTitle().should('contain.text', 'Geschwister');

    GeschwisterPO.elements.loading().should('not.exist');

    GeschwisterPO.addGeschwister(bruder);

    // cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    // cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    // cy.wait('@patchResource');
    // cy.wait('@getResource');

    GeschwisterPO.elements.loading().should('not.exist');

    cy.getBySel('button-continue').click();

    // Step 7: Ehe und Konkubinatspartner ============================================

    // step 8: Kinder ================================================================
    getStepTitle().should('contain.text', 'Kinder');

    KinderPO.elements.loading().should('not.exist');

    KinderPO.addKind(kind);

    KinderPO.elements.loading().should('not.exist');

    // cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    // cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    cy.getBySel('button-continue').click();

    // cy.wait('@patchResource');
    // cy.wait('@getResource');

    // Step 9: Auszahlung ===========================================================

    getStepTitle().should('contain.text', 'Auszahlung');
    AuszahlungPO.elements.loading().should('not.exist');

    AuszahlungPO.fillAuszahlungEigenesKonto(auszahlung);

    AuszahlungPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    // Step 10: Einnahmen und  Kosten =================================================
    getStepTitle().should('contain.text', 'Einnahmen & Kosten');
    EinnahmenKostenPO.elements.loading().should('not.exist');

    EinnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    // cy.intercept('PATCH', '/api/v1/gesuch/*').as('patchResource');
    // cy.intercept('GET', '/api/v1/gesuch/*').as('getResource');

    EinnahmenKostenPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    // cy.wait('@patchResource');
    // cy.wait('@getResource');

    EinnahmenKostenPO.elements.loading().should('not.exist');

    // Step 11: Freigabe =============================================================
    getStepTitle().should('contain.text', 'Freigabe');

    cy.getBySel('button-abschluss').click();
  });
});
