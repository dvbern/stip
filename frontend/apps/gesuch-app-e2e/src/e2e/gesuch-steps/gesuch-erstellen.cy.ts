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
  Partner,
  PersonInAusbildung,
} from '@dv/shared/model/gesuch';
import { LebenslaufPO } from '../../support/po/lebenslauf.po';
import { ElternPO } from '../../support/po/eltern.po';
import { FamilyPO } from '../../support/po/familiy.po';
import { GeschwisterPO } from '../../support/po/geschwister.po';
import { KinderPO } from '../../support/po/kinder.po';
import { AuszahlungPO } from '../../support/po/auszahlung.po';
import { EinnahmenKostenPO } from '../../support/po/einnahmen-kosten.po';
import { PartnerPO } from '../../support/po/partner.po';

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
  zivilstand: 'VERHEIRATET',
  wohnsitz: 'FAMILIE',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
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

const partner: Partner = {
  adresse,
  vorname: 'Susanne',
  geburtsdatum: '16.12.1990',
  sozialversicherungsnummer: '756.2222.2222.55',
  nachname: 'Schmitt',
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
  before(() => {
    cy.viewport(1440, 1080);
  });

  beforeEach(() => {
    cy.login();
    cy.visit('/');
  });

  let entityId = '';
  let token = '';

  it('should enter the correct person data', () => {
    CockpitPO.getGesuchNew().click();

    // Step 1: Person ============================================================
    getStepTitle().should('contain.text', 'Person in Ausbildung');
    PersonPO.elements.loading().should('not.exist');

    PersonPO.fillPersonForm(person);

    cy.intercept('GET', '/api/v1/gesuch/*').as('getRequest');

    PersonPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    cy.wait('@getRequest').then((interception) => {
      entityId = interception.response?.body.id;
      token = interception.request.headers['authorization'] as string;
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

    ElternPO.elements.loading().should('not.exist');

    ElternPO.addMutter(mutter);

    ElternPO.elements.loading().should('not.exist');

    getButtonContinue().click();

    // Step 6: Geschwister  ========================================================
    getStepTitle().should('contain.text', 'Geschwister');

    GeschwisterPO.elements.loading().should('not.exist');

    GeschwisterPO.addGeschwister(bruder);

    GeschwisterPO.elements.loading().should('not.exist');

    getButtonContinue().click();

    // Step 7: Ehe und Konkubinatspartner ============================================

    getStepTitle().should('contain.text', 'Ehe- / Konkubinatspartner');

    PartnerPO.elements.loading().should('not.exist');

    PartnerPO.fillPartnerForm(partner);

    getButtonSaveContinue().click();

    PartnerPO.elements.loading().should('not.exist');

    // step 8: Kinder ================================================================
    getStepTitle().should('contain.text', 'Kinder');

    KinderPO.elements.loading().should('not.exist');

    KinderPO.addKind(kind);

    KinderPO.elements.loading().should('not.exist');

    getButtonContinue().click();

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

    EinnahmenKostenPO.elements
      .form()
      .should('have.class', 'ng-valid')
      .then(() => {
        getButtonSaveContinue().click();
      });

    EinnahmenKostenPO.elements.loading().should('not.exist');

    // Step 11: Freigabe =============================================================
    getStepTitle().should('contain.text', 'Freigabe');

    CockpitPO.getNavDashboard().click();
    // cy.getBySel('button-abschluss').click();
  });

  after(() => {
    if (entityId && token) {
      cy.request({
        method: 'DELETE',
        url: `api/v1/gesuch/${entityId}`,
        headers: {
          Authorization: token,
        },
      });
    }
  });
});
